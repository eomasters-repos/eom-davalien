/*-
 * ========================LICENSE_START=================================
 * EOMasters GPT Test Environment - This projects provides a test environment for operators you have developed.
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 - 2024 Marco Peters
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

package org.eomasters.davalien;

import java.awt.Desktop;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eomasters.davalien.TestResult.Status;
import org.eomasters.davalien.res.JsonHelper;
import org.eomasters.davalien.res.Resources;
import org.eomasters.davalien.res.testdef.ProductContent;
import org.eomasters.davalien.res.testdef.TestDefinition;
import org.eomasters.davalien.utils.CopyDirContentTreeVisitor;
import org.eomasters.davalien.utils.DeleteTreeVisitor;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.main.CommandLineTool;

/**
 * The DAta VALIdataion ENvironment - DAVALIEN.
 */
public class Davalien {

  public static final Logger LOGGER = Logger.getLogger(Davalien.class.getName());
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

  /**
   * Create a new validation environment.
   *
   * @param envPath   the environment path
   * @param testNames test names which should be executed
   * @param tags      tags of tests which should be executed
   */
  public Davalien(String envPath, String[] testNames, String[] tags) {
    this.envPath = Paths.get(envPath);
    this.testNames = testNames != null ? List.of(testNames) : null;
    this.tags = tags != null ? List.of(tags) : null;
  }

  /**
   * Get the environment path.
   *
   * @return the environment path
   */
  public Path getEnvPath() {
    return envPath;
  }

  /**
   * Get the test names.
   *
   * @return the test names
   */
  public List<String> getTestNames() {
    return testNames;
  }

  /**
   * Get the tags.
   *
   * @return the tags
   */
  public List<String> getTags() {
    return tags;
  }

  /**
   * Get the date.
   *
   * @return the date
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Get the all test definitions.
   *
   * @return the all test definitions
   */
  public List<TestDefinition> getAllTestDefinitions() {
    return allTestDefinitions;
  }

  /**
   * Returns the target format for the processed data products.
   *
   * @return the target format
   */
  public String getDefaultTargetFormat() {
    return config.getDefaultTargetFormat();
  }

  /**
   * Initialise the validation environment.
   *
   * @throws DavalienException in case of an error
   */
  public void init() throws DavalienException {
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
      throw new DavalienException("Not able to initialise validation tests", e);
    }
  }

  /**
   * Execute the validation tests.
   *
   * @return the test results
   */
  public List<TestResult> execute() {
    ArrayList<TestResult> testResults = new ArrayList<>();
    if (!testInstants.isEmpty()) {
      runGptTests(testInstants);
      testResults = createTestResults(testInstants);
    }
    return testResults;
  }

  /**
   * Create the report.
   *
   * @param testResults the test results
   * @throws IOException if an I/O error occurs
   */
  public void createReport(List<TestResult> testResults) throws IOException {
    if (testResults.isEmpty()) {
      System.out.printf("No tests executed. Filters[Test Names: %s, Tags: %s]%n",
          testNames != null ? String.join(",", testNames) : "-",
          tags != null ? String.join(",", tags) : "-");
    } else {
      TestReport testReport = new TestReport(testResults, this);
      System.out.println("Test Results:");
      System.out.printf("Tests executed: %d (success=%d / error=%d / failure=%d)%n", testReport.getTestResults().size(),
          testReport.getNumSuccessTests(), testReport.getNumErrorTests(), testReport.getNumFailureTests());
      System.out.println("For details see the results directory: " + runResultsDir);
      String reportFileName = "validation_report";
      toJsonFile(testReport, runResultsDir.resolve(reportFileName + ".json"));
      Path htmlReportFile = runResultsDir.resolve(reportFileName + ".html");
      toHtmlFile(testReport, htmlReportFile);
      if (Desktop.isDesktopSupported() && config.isOpenReport()) {
        Desktop.getDesktop().open(htmlReportFile.toFile());
      }
    }
  }

  private void rollResults(Path resultsDir) throws IOException {
    if (Files.isDirectory(resultsDir)) {
      try (Stream<Path> resultDirList = Files.list(resultsDir)) {
        Stream<Path> resultDirs = resultDirList.filter(path -> Files.isDirectory(path)
            && path.getFileName().toString().matches("\\d{8}_\\d{6}"));

        List<Path> sorted = resultDirs.sorted(
                                          Comparator.comparing(p -> LocalDateTime.parse(p.getFileName().toString(),
                                              DATE_FORMAT)))
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

  private ArrayList<TestResult> createTestResults(List<TestInst> tests) {
    ArrayList<TestResult> testResults = new ArrayList<>();
    for (TestInst test : tests) {
      String testName = test.getName();
      TestResult result = new TestResult(testName, test.getDescription(), test.getDuration(), test.getResultPath());
      testResults.add(result);

      Throwable exception = test.getException();
      if (exception != null) {
        result.setException(exception);
        continue;
      }
      ProductContent expectation = test.getTestDef().getExpectation();
      if (expectation == null) {
        result.setException(new RuntimeException("No expectation found for test: " + testName));
      } else {
        try {
          Product testProduct = ProductIO.readProduct(test.getResultPath().toFile());
          ProductValidator.testProduct(testProduct, expectation, result);
          if (result.getStatus().equals(Status.SUCCESS) && config.isDeleteResultAfterSuccess()) {
            Files.walkFileTree(test.getResultDir(), new DeleteTreeVisitor());
          } else {
            Files.walkFileTree(test.getResultDir(),
                new CopyDirContentTreeVisitor(test.getResultDir(), resultProductDir));
            result.setTargetPath(resultProductDir.resolve(test.getResultPath().getFileName()));
          }
        } catch (Exception e) {
          result.setException(new Exception("Error executing test: " + testName, e));
        }
      }
    }
    return testResults;
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
        test.setDuration((end.toEpochMilli() - start.toEpochMilli()) / 1000f);
      } catch (Throwable t) {
        test.setException(t);
      }
      System.gc();
    });
  }

  static List<TestDefinition> filterTestDefinitions(List<TestDefinition> allTestDefinitions, List<String> testNames,
      List<String> tags) {

    Stream<TestDefinition> activeTestDefinitions = allTestDefinitions.stream();
    if ((testNames != null && !testNames.isEmpty()) || (tags != null && !tags.isEmpty())) {
      activeTestDefinitions = activeTestDefinitions
          .filter(testDefinition -> isFiltered(testDefinition, testNames, tags));
    }
    return activeTestDefinitions.sorted(Comparator.comparing(TestDefinition::getTestName)).collect(Collectors.toList());

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
