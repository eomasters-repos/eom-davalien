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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a test result.
 */
public class TestResult {

  /**
   * The status of the test.
   */
  public enum Status {
    /**
     * The test was successful.
     */
    SUCCESS,

    /**
     * The test failed.
     */
    FAILURE,
    /**
     * The test had an error.
     */
    ERROR
  }

  private final String testName;
  private final String description;
  private Status status = Status.SUCCESS;
  private final float duration;
  private Path targetPath;
  private Throwable exception;
  private List<AssertionError> errors;

  /**
   * Create a new instance. The status is set to SUCCESS by default.
   *
   * @param name        the name of the test
   * @param description the description
   * @param duration    the duration
   * @param targetPath  the target path
   */
  public TestResult(String name, String description, float duration, Path targetPath) {
    this.testName = name;
    this.description = description;
    this.duration = duration;
    this.targetPath = targetPath;
  }

  /**
   * Get the name of the test.
   *
   * @return The name of the test
   */
  public String getTestName() {
    return testName;
  }

  /**
   * Get the description of the test.
   *
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the status of the test.
   *
   * @return The status of the test.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Get the duration of the test execution.
   *
   * @return the duration
   */
  public float getDuration() {
    return duration;
  }

  /**
   * Get the target path of the test.
   *
   * @return the targetPath
   */
  public Path getTargetPath() {
    return targetPath;
  }

  /**
   * Get the exception thrown during the test, if any.
   *
   * @return the exception
   */
  public Throwable getException() {
    return exception;
  }

  /**
   * Get the errors occurred during the test, if any.
   *
   * @return the errors
   */
  public List<AssertionError> getErrors() {
    return errors;
  }

  /**
   * Set the target path of the test.
   *
   * @param targetPath the targetPath to set
   */
  public void setTargetPath(Path targetPath) {
    this.targetPath = targetPath;
  }

  /**
   * Set the exception thrown during the test.
   *
   * @param e the exception
   */
  public void setException(Throwable e) {
    this.exception = e;
    this.status = Status.FAILURE;
  }

  /**
   * Add an error to the list of errors.
   *
   * @param e the error
   */
  public void addError(AssertionError e) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(e);
    this.status = Status.ERROR;
  }
}
