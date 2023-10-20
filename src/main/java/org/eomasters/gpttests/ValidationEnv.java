/*-
 * ========================LICENSE_START=================================
 * EOMasters GPT Test Environment - This projects provides a test environment for operators you have developed.
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 Marco Peters
 * ======================================================================
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * -> http://www.gnu.org/licenses/gpl-3.0.html
 * =========================LICENSE_END==================================
 */

package org.eomasters.gpttests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eomasters.gpttests.res.JsonHelper;
import org.eomasters.gpttests.res.Resources;
import org.eomasters.gpttests.res.testdef.ProductContent;
import org.eomasters.gpttests.res.testdef.TestDefinition;
import org.eomasters.gpttests.utils.CopyDirContentTreeVisitor;
import org.eomasters.gpttests.utils.DeleteTreeVisitor;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.main.CommandLineTool;

public class ValidationEnv {

  private static final String TESTS_DIR = "tests";
  private static final String RESULTS_DIR = "results";
  private static final String PRODUCTS_DIR = "products";
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
  private final Path envPath;
  private final List<String> testNames;
  private final List<String> tags;
  private EnvConfig config;
  private List<TestDefinition> allTestDefinitions;
  private Path runResultsDir;
  private Path resultProductDir;
  private Resources resources;
  private LocalDateTime date;
  private List<TestInst> testInstants;

  public ValidationEnv(String envPath, String[] testNames, String[] tags) {
    this.envPath = Paths.get(envPath);
    this.testNames = testNames != null ? List.of(testNames) : null;
    this.tags = tags != null ? List.of(tags) : null;
  }

  public Path getEnvPath() {
    return envPath;
  }

  public List<String> getTestNames() {
    return testNames;
  }

  public List<String> getTags() {
    return tags;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public List<TestDefinition> getAllTestDefinitions() {
    return allTestDefinitions;
  }

  public void init() throws ValidationEnvException {
    try {
      config = JsonHelper.getConfig(this.envPath.resolve("config.json"));
    resources = Resources.create(envPath);
    allTestDefinitions = resources.getTestDefinitions(envPath.resolve(TESTS_DIR));
    date = LocalDateTime.now();
    Path resultsDir = envPath.resolve(RESULTS_DIR);
    rollResults(resultsDir);
    runResultsDir = resultsDir.resolve(date.format(DATE_FORMAT));
    resultProductDir = runResultsDir.resolve(PRODUCTS_DIR);
    Files.createDirectories(resultProductDir);

    List<TestDefinition> selectedTestDefs = filterTestDefinitions(allTestDefinitions, testNames, tags);
    testInstants = new ArrayList<>();
    if (!selectedTestDefs.isEmpty()) {
      testInstants = createTests(selectedTestDefs);
    }
    } catch (Exception e) {
      throw new ValidationEnvException("Not able to initialise validation tests", e);
    }
  }

  public List<TestResult> execute() {
    List<TestDefinition> selectedTestDefs = filterTestDefinitions(allTestDefinitions, testNames, tags);
    ArrayList<TestResult> testResults = new ArrayList<>();
    if (!testInstants.isEmpty()) {
      runGptTests(testInstants);
      testResults = compareResults(testInstants, selectedTestDefs);
    }
    return testResults;
  }

  public void createReports(List<TestResult> testResults) throws IOException {
    if (testResults.isEmpty()) {
      System.out.println("No tests executed.");
    } else {
      TestReport testReport = new TestReport(testResults, this);
      System.out.println("Test Results:");
      System.out.printf("Tests executed: %d (success=%d / error=%d / failure=%d)%n", testReport.getTestResults().size(),
          testReport.getNumSuccessTests(), testReport.getNumErrorTests(), testReport.getNumFailureTests());
      System.out.println("For details see the results directory: " + runResultsDir);
      String reportFileName = "validation_report";
      toJsonFile(testReport, runResultsDir.resolve(reportFileName + ".json"));
      toHtmlFile(testReport, runResultsDir.resolve(reportFileName + ".html"));
    }
  }

  private void rollResults(Path resultsDir) throws IOException {
    if (Files.isDirectory(resultsDir)) {
      try (Stream<Path> resultDirList = Files.list(resultsDir)) {
        Stream<Path> resultDirs = resultDirList.filter(path -> Files.isDirectory(path) && path.getFileName()
                                                                                              .toString()
                                                                                              .matches(
                                                                                                  "\\d{8}_\\d{6}"));
        List<Path> sorted = resultDirs.sorted(
                                          Comparator.comparing(p -> LocalDateTime.parse(p.getFileName().toString(), DATE_FORMAT)))
                                      .collect(Collectors.toList());
        if (sorted.size() >= config.getRollingResults()) {
          for (int i = 0; i <= sorted.size() - config.getRollingResults(); i++) {
            Path path = sorted.get(i);
            Files.walkFileTree(path, new DeleteTreeVisitor());
          }
        }
      }
    }
  }

  private void toHtmlFile(TestReport testReport, Path file) throws IOException {
    String htmlString = HtmlReport.create(testReport);
    Files.writeString(file, htmlString);
  }

  private void toJsonFile(TestReport testReport, Path file) throws IOException {
    String jsonString = JsonHelper.toJson(testReport);
    Files.writeString(file, jsonString);
  }

  private ArrayList<TestResult> compareResults(List<TestInst> tests, List<TestDefinition> testDefinitions) {
    ArrayList<TestResult> testResults = new ArrayList<>();
    for (TestInst test : tests) {
      String testName = test.getName();
      TestResult result = new TestResult(testName, test.getDescription(), test.getExecutionTime(),
          test.getTargetPath());
      testResults.add(result);

      Throwable exception = test.getException();
      if (exception != null) {
        result.setException(exception);
        continue;
      }
      TestDefinition expectation = findExpectation(testDefinitions, testName);
      if (expectation == null) {
        result.setException(new RuntimeException("No expectation found for test: " + testName));
      } else {
        try {
          ProductContent expectedContent = expectation.getExpectation();
          Product testProduct = ProductIO.readProduct(test.getTargetPath().toFile());
          ProductValidator.testProduct(testProduct, expectedContent, result);
          if (result.getStatus().equals(TestResult.STATUS.SUCCESS) && config.isDeleteResultAfterSuccess()) {
            Files.walkFileTree(test.getTempProductDir(), new DeleteTreeVisitor());
            result.setTargetPath(null);
          } else {
            Files.walkFileTree(test.getTempProductDir(),
                new CopyDirContentTreeVisitor(test.getTempProductDir(), resultProductDir));
            result.setTargetPath(resultProductDir.resolve(test.getTargetPath().getFileName()));
          }
        } catch (Exception e) {
          result.setException(new Exception("Error executing test: " + testName, e));
        }
      }
    }
    return testResults;
  }

  private TestDefinition findExpectation(List<TestDefinition> testDefinitions, String testName) {
    return testDefinitions.stream().filter(definition -> definition.getTestName().equals(testName))
                       .findFirst().orElse(null);
  }

  private List<TestInst> createTests(List<TestDefinition> selectedTestDefs) throws Exception {
    ArrayList<TestInst> testList = new ArrayList<>();

    for (TestDefinition def : selectedTestDefs) {
      try {
        Path tempDirectory = Files.createTempDirectory(def.getTestName());
        testList.add(TestInst.create(this, def, resources, tempDirectory));
      } catch (IOException e) {
        throw new Exception("Error creating temp directory for test: " + def.getTestName(), e);
      }
    }
    return testList;
  }

  private void runGptTests(List<TestInst> activeTests) {
    CommandLineTool commandLineTool = new CommandLineTool();
    activeTests.forEach(test -> {
      try {
        Instant start = Instant.now();
        commandLineTool.run(test.getParamList().toArray(new String[0]));
        Instant end = Instant.now();
        test.setExecutionTime((end.toEpochMilli() - start.toEpochMilli()) / 1000f);
      } catch (Throwable t) {
        test.setException(t);
      }
      System.gc();
    });
  }

  static List<TestDefinition> filterTestDefinitions(List<TestDefinition> allTestDefinitions, List<String> testNames,
      List<String> tags) {

    if ((testNames == null || testNames.isEmpty()) && (tags == null || tags.isEmpty())) {
      return allTestDefinitions;
    }

    return allTestDefinitions.stream().filter(testDefinition -> isFiltered(testDefinition, testNames, tags)).collect(
                          Collectors.toList());
  }

  private static boolean isFiltered(TestDefinition testDefinition, List<String> testNames, List<String> tags) {
    boolean filtered = false;
    if (testNames != null && !testNames.isEmpty()) {
      if (testNames.contains(testDefinition.getTestName())) {
        filtered = true;
      }
    }
    if (!filtered && tags != null && !tags.isEmpty()) {
      if (tags.stream().anyMatch(testDefinition.getTags()::contains)) {
        filtered = true;
      }
    }
    return filtered;
  }

}
