/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
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

import java.nio.file.Path;
import java.util.List;
import org.eomasters.gpttests.TestResult.STATUS;

public class TestReport {

  private final List<TestResult> testResults;
  private final Path envPath;
  private final String date;
  private final List<String> testNames;
  private final List<String> tags;
  private final int numAllTests;
  private final long numSuccessTests;
  private final long numErrorTests;
  private final long numFailureTests;

  public TestReport(List<TestResult> testResults, GptTestEnv gptTestEnv) {
    this.testResults = testResults;
    this.numAllTests = gptTestEnv.getAllTestDefinitions().size();
    this.envPath = gptTestEnv.getEnvPath();
    this.testNames = gptTestEnv.getTestNames();
    this.tags = gptTestEnv.getTags();
    this.date = gptTestEnv.getDateString();
    numSuccessTests = testResults.stream().filter(testResult -> testResult.getStatus().equals(STATUS.SUCCESS)).count();
    numErrorTests = testResults.stream().filter(testResult -> testResult.getStatus().equals(STATUS.ERROR)).count();
    numFailureTests = testResults.stream().filter(testResult -> testResult.getStatus().equals(STATUS.FAILURE)).count();
  }

  public List<TestResult> getTestResults() {
    return testResults;
  }

  public Path getEnvPath() {
    return envPath;
  }

  public String getDate() {
    return date;
  }

  public List<String> getTestNames() {
    return testNames;
  }

  public List<String> getTags() {
    return tags;
  }

  public int getNumAllTests() {
    return numAllTests;
  }

  public long getNumSuccessTests() {
    return numSuccessTests;
  }

  public long getNumErrorTests() {
    return numErrorTests;
  }

  public long getNumFailureTests() {
    return numFailureTests;
  }
}
