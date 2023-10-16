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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.eomasters.gpttests.TestResult.STATUS;
import org.eomasters.gpttests.res.JsonHelper;
import org.eomasters.gpttests.res.Resources;
import org.eomasters.gpttests.res.testdef.ProductContent;
import org.eomasters.gpttests.res.testdef.TestDefinition;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.main.CommandLineTool;

public class GptTestEnv {

  protected static final String TESTS_DIR = "tests";
  protected static final String RESULTS_DIR = "results";
  protected static final String PRODUCTS_DIR = "products";
  private final Path envPath;
  private final List<String> testNames;
  private final List<String> tags;
  private List<TestDefinition> allTestDefinitions;
  private Path resultsDir;
  private Path resultProductDir;
  private Resources resources;

  public GptTestEnv(String envPath, String[] testNames, String[] tags) {
    this.envPath = Paths.get(envPath);
    this.testNames = List.of(testNames);
    this.tags = List.of(tags);
  }

  public void init() throws IOException {
    resources = Resources.create(envPath);
    allTestDefinitions = resources.getTestDefinitions(envPath.resolve(TESTS_DIR));
    resultsDir = envPath.resolve(RESULTS_DIR).resolve(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
    resultProductDir = resultsDir.resolve(PRODUCTS_DIR);
    Files.createDirectories(resultProductDir);

  }

  public List<TestResult> execute() {
    List<TestDefinition> selectedTestDefs = filterTestDefinitions(allTestDefinitions, testNames, tags);
    ArrayList<TestResult> testResults = new ArrayList<>();
    if (!selectedTestDefs.isEmpty()) {
      List<Test> activeTests = createTests(selectedTestDefs);
      List<TestExpectation> expectations = createTestExpectations(selectedTestDefs);
      runGptTests(activeTests);
      testResults = compareResults(activeTests, expectations);
    }
    return testResults;
  }

  public void createReports(List<TestResult> testResults) throws IOException{
    if (testResults.isEmpty()) {
      System.out.println("No tests executed.");
    } else {
      System.out.println("Test Results:");
      long successNum = testResults.stream().filter(testResult -> testResult.getStatus().equals(STATUS.SUCCESS)).count();
      long errorNum = testResults.stream().filter(testResult -> testResult.getStatus().equals(STATUS.ERROR)).count();
      long failureNum = testResults.stream().filter(testResult -> testResult.getStatus().equals(STATUS.FAILURE)).count();
      System.out.printf("Tests executed: %d (success=%d / error=%d / failure=%d)", testResults.size(), successNum, errorNum, failureNum);
      System.out.println("For details see the results directory: " + resultsDir);
      String reportFileName = "test-results";
      toJsonFile(testResults, resultsDir.resolve(reportFileName + ".json"));
      toHtmlFile(testResults, resultsDir.resolve(reportFileName + ".html"));
      for (TestResult testResult : testResults) {
        System.out.println(testResult);
      }
    }
  }

  private void toHtmlFile(List<TestResult> testResults, Path file) {
    // https://developer.mozilla.org/en-US/docs/Web/HTML/Element/details
  }

  private void toJsonFile(List<TestResult> testResults, Path file) throws IOException {
    String jsonString = JsonHelper.toJson(testResults);
    Files.writeString(file, jsonString);
  }

  private ArrayList<TestResult> compareResults(List<Test> tests, List<TestExpectation> expectations) {
    ArrayList<TestResult> testResults = new ArrayList<>();
    for (Test test : tests) {
      String testName = test.getName();
      TestResult result = new TestResult(testName);
      testResults.add(result);
      TestExpectation expectation = findExpectation(expectations, testName);
      if (expectation == null) {
        result.setException(new RuntimeException("No expectation found for test: " + testName));
      } else {
        try {
          ProductContent expectedContent = expectation.getExpectedContent();
          Product testProduct = ProductIO.readProduct(test.getTargetPath().toFile());
          ProductTester.testProduct(testProduct, expectedContent, result);
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
    return selectedTestDefs.stream().map(def -> Test.create(def, resources, resultProductDir))
                           .collect(Collectors.toList());
  }

  private void runGptTests(List<Test> activeTests) {
    CommandLineTool commandLineTool = new CommandLineTool();
    activeTests.forEach(testDef -> {
      try {
        commandLineTool.run(testDef.getParamList().toArray(new String[0]));
        System.gc();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
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
