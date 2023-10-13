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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.eomasters.gpttests.res.JsonHelper;
import org.eomasters.gpttests.res.Resources;
import org.eomasters.gpttests.res.testdef.ProductContent;
import org.eomasters.gpttests.res.testdef.TestDefinition;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.main.CommandLineTool;

public class GptTestEnv {

  private final Path envPath;
  private final String[] testNames;
  private final String[] tags;
  private List<TestDefinition> allTestDefinitions;
  private Path resultsDir;
  private Path resultProductDir;
  private Resources resources;

  public GptTestEnv(String envPath, String[] testNames, String[] tags) {
    this.envPath = Paths.get(envPath);
    this.testNames = testNames;
    this.tags = tags;
  }

  public void init() throws IOException {
    allTestDefinitions = JsonHelper.getTestDefinitions(FileUtils.getMandatoryFile(envPath, "test-definitions.json"));
    resources = Resources.create(envPath);
    resultsDir = envPath.resolve("results");
    resultProductDir = resultsDir.resolve("products");
    Files.createDirectories(resultProductDir);
  }

  public void execute() {
    List<TestDefinition> selectedTestDefs = filterTestDefinitions(allTestDefinitions, testNames, tags);
    List<Test> activeTests = createTests(selectedTestDefs);
    List<TestExpectation> expectations = createTestExpectations(selectedTestDefs);
    List<TestResult> testResults = runGptTests(activeTests);
    compareResults(testResults, expectations);
    // reportResults(testResults);
  }

  private void compareResults(List<TestResult> testResults, List<TestExpectation> expectations) {
    for (TestResult testResult : testResults) {
      TestExpectation expectation = findExpectation(testResult, expectations);
      if (expectation == null) {
        testResult.setException(new RuntimeException("No expectation found for test: " + testResult.getTestName()));
      } else {
        try {
          ProductContent expectedContent = expectation.getExpectedContent();
          Product testProduct = ProductIO.readProduct(testResult.getTargetPath().toFile());
          ProductTester.testProduct(testProduct, expectedContent, testResult);
        } catch (IOException e) {
          testResult.setException(new Exception("Error executing test: " + testResult.getTestName(), e));
        }
      }
    }
  }

  private TestExpectation findExpectation(TestResult testResult, List<TestExpectation> expectations) {
    return expectations.stream().filter(expectation -> expectation.getTestName().equals(testResult.getTestName()))
                       .findFirst().orElse(null);
  }

  private List<TestExpectation> createTestExpectations(List<TestDefinition> selectedTestDefs) {
    return selectedTestDefs.stream().map(TestExpectation::create).collect(Collectors.toList());
  }

  private List<Test> createTests(List<TestDefinition> selectedTestDefs) {
    return selectedTestDefs.stream().map(def -> Test.create(def, resources, resultProductDir))
                           .collect(Collectors.toList());
  }

  private List<TestResult> runGptTests(List<Test> activeTests) {
    CommandLineTool commandLineTool = new CommandLineTool();
    return activeTests.stream().map(testDef -> {
      TestResult testResult = new TestResult(testDef.getName());
      testResult.setTargetPath(testDef.getTargetPath());
      try {
        commandLineTool.run(testDef.getParamList().toArray(new String[0]));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return testResult;
    }).collect(Collectors.toList());
  }

  private List<TestDefinition> filterTestDefinitions(List<TestDefinition> allTestDefinitions, String[] testNames,
      String[] tags) {
    List<String> testNameList = Arrays.asList(testNames);
    return allTestDefinitions.stream().filter(testDefinition -> {
      if (testNameList.contains(testDefinition.getName())) {
        List<String> defTags = Arrays.asList(testDefinition.getTags());
        return Arrays.stream(tags).anyMatch(defTags::contains);
      }
      return false;
    }).collect(Collectors.toList());

  }


}
