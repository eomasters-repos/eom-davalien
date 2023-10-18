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
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.main.CommandLineTool;

public class GptTestEnv {

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

  public GptTestEnv(String envPath, String[] testNames, String[] tags) {
    this.envPath = Paths.get(envPath);
    this.testNames = List.of(testNames);
    this.tags = List.of(tags);
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

  public void init() throws IOException {
    config = JsonHelper.getConfig(this.envPath.resolve("config.json"));
    resources = Resources.create(envPath);
    allTestDefinitions = resources.getTestDefinitions(envPath.resolve(TESTS_DIR));
    date = LocalDateTime.now();
    Path resultsDir = envPath.resolve(RESULTS_DIR);
    rollResults(resultsDir);
    runResultsDir = resultsDir.resolve(date.format(DATE_FORMAT));
    resultProductDir = runResultsDir.resolve(PRODUCTS_DIR);
    Files.createDirectories(resultProductDir);

  }

  private void rollResults(Path resultsDir) throws IOException {
    try (Stream<Path> resultDirList = Files.list(resultsDir)) {
      Stream<Path> resultDirs = resultDirList.filter(path -> Files.isDirectory(path) && path.getFileName()
                                                                                            .toString()
                                                                                            .matches("\\d{8}_\\d{6}"));
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

  public List<TestResult> execute() {
    List<TestDefinition> selectedTestDefs = filterTestDefinitions(allTestDefinitions, testNames, tags);
    ArrayList<TestResult> testResults = new ArrayList<>();
    if (!selectedTestDefs.isEmpty()) {
      List<Test> activeTests = createTests(selectedTestDefs);
      runGptTests(activeTests);
      List<TestExpectation> expectations = createTestExpectations(selectedTestDefs);
      testResults = compareResults(activeTests, expectations);
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

  private void toHtmlFile(TestReport testReport, Path file) throws IOException {
    String htmlString = HtmlReport.create(testReport);
    Files.writeString(file, htmlString);
  }

  private void toJsonFile(TestReport testReport, Path file) throws IOException {
    String jsonString = JsonHelper.toJson(testReport);
    Files.writeString(file, jsonString);
  }

  private ArrayList<TestResult> compareResults(List<Test> tests, List<TestExpectation> expectations) {
    ArrayList<TestResult> testResults = new ArrayList<>();
    for (Test test : tests) {
      String testName = test.getName();
      TestResult result = new TestResult(testName, test.getExecutionTime(), test.getTargetPath());
      testResults.add(result);

      Throwable exception = test.getException();
      if (exception != null) {
        result.setException(exception);
        continue;
      }
      TestExpectation expectation = findExpectation(expectations, testName);
      if (expectation == null) {
        result.setException(new RuntimeException("No expectation found for test: " + testName));
      } else {
        try {
          ProductContent expectedContent = expectation.getExpectedContent();
          Product testProduct = ProductIO.readProduct(test.getTargetPath().toFile());
          ProductTester.testProduct(testProduct, expectedContent, result);
          if(result.getStatus().equals(TestResult.STATUS.SUCCESS) && config.isDeleteResultAfterSuccess()) {
            Files.walkFileTree(test.getTempProductDir(), new DeleteTreeVisitor());
            result.setTargetPath(null);
          } else {
            Files.walkFileTree(test.getTempProductDir(), new CopyDirContentTreeVisitor(test.getTempProductDir(), resultProductDir));
            result.setTargetPath(resultProductDir.resolve(test.getTargetPath().getFileName()));
          }
        } catch (Exception e) {
          result.setException(new Exception("Error executing test: " + testName, e));
        }
      }
    }
    return testResults;
  }

  private TestExpectation findExpectation(List<TestExpectation> expectations, String testName) {
    return expectations.stream().filter(expectation -> expectation.getTestName().equals(testName))
                       .findFirst().orElse(null);
  }

  private List<TestExpectation> createTestExpectations(List<TestDefinition> selectedTestDefs) {
    return selectedTestDefs.stream().map(TestExpectation::create).collect(Collectors.toList());
  }

  private List<Test> createTests(List<TestDefinition> selectedTestDefs) {
    return selectedTestDefs.stream().map(def -> {
                             try {
                               Path tempDirectory = Files.createTempDirectory(def.getTestName());
                               return Test.create(def, resources, tempDirectory);
                             } catch (IOException e) {
                               throw new RuntimeException(e);
                             }
                           })
                           .collect(Collectors.toList());
  }

  private void runGptTests(List<Test> activeTests) {
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

  private List<TestDefinition> filterTestDefinitions(List<TestDefinition> allTestDefinitions, List<String> testNames,
      List<String> tags) {
    List<TestDefinition> filteredTests = new ArrayList<>(allTestDefinitions);

    if (!testNames.isEmpty()) {
      filteredTests.removeIf(testDefinition -> !testNames.contains(testDefinition.getTestName()));
    }

    if (!tags.isEmpty()) {
      filteredTests.removeIf(testDefinition -> tags.stream().noneMatch(testDefinition.getTags()::contains));
    }
    return filteredTests;

  }

}
