/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 - 2025 Marco Peters
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

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.eomasters.davalien.TestResult.Status;

/**
 * Represents a test report of DAVALIEN.
 */
public class TestReport {

  private final Path envPath;
  private final LocalDateTime creationTime;
  private final List<String> testNames;
  private final List<String> tags;
  private final int numTestsExecuted;
  private final long numSuccessTests;
  private final long numErrorTests;
  private final long numFailureTests;
  private final List<TestResult> testResults;

  /**
   * Create a new instance.
   *
   * @param testResults the test results
   * @param davalien    the Davalien instance
   */
  public TestReport(List<TestResult> testResults, Davalien davalien) {
    this.creationTime = davalien.getDate();
    this.numTestsExecuted = davalien.getAllTestDefinitions().size();
    this.envPath = davalien.getEnvPath();
    this.testNames = davalien.getTestNames();
    this.tags = davalien.getTags();
    this.testResults = testResults;
    numSuccessTests = testResults.stream().filter(testResult -> testResult.getStatus().equals(Status.SUCCESS)).count();
    numErrorTests = testResults.stream().filter(testResult -> testResult.getStatus().equals(Status.ERROR)).count();
    numFailureTests = testResults.stream().filter(testResult -> testResult.getStatus().equals(Status.FAILURE)).count();
  }

  /**
   * Get the test results.
   *
   * @return the test results
   */
  public List<TestResult> getTestResults() {
    return testResults;
  }

  /**
   * Get the path to the environment.
   *
   * @return the envPath
   */
  public Path getEnvPath() {
    return envPath;
  }

  /**
   * Get the creation time.
   *
   * @return the creationTime
   */
  public LocalDateTime getCreationTime() {
    return creationTime;
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
   * Get the number of tests executed.
   *
   * @return the number of tests executed
   */
  public int getNumTestsExecuted() {
    return numTestsExecuted;
  }

  /**
   * Get the number of success tests.
   *
   * @return the number of success tests
   */
  public long getNumSuccessTests() {
    return numSuccessTests;
  }

  /**
   * Get the number of error tests.
   *
   * @return the number of error tests
   */
  public long getNumErrorTests() {
    return numErrorTests;
  }

  /**
   * Get the number of failure tests.
   *
   * @return the number of failure tests
   */
  public long getNumFailureTests() {
    return numFailureTests;
  }
}
