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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestResult {

  public enum STATUS {
    SUCCESS, FAILURE, ERROR
  }

  private String testName;
  private final String description;
  private STATUS status = STATUS.SUCCESS;
  private final float duration;
  private Path targetPath;
  private Throwable exception;
  private List<AssertionError> errors;

  public TestResult(String name, String description, float duration, Path targetPath) {
    this.testName = name;
    this.description = description;
    this.duration = duration;
    this.targetPath = targetPath;
  }

  public String getTestName() {
    return testName;
  }

  public String getDescription() {
    return description;
  }

  public STATUS getStatus() {
    return status;
  }

  public float getDuration() {
    return duration;
  }

  public Path getTargetPath() {
    return targetPath;
  }

  public Throwable getException() {
    return exception;
  }

  public List<AssertionError> getErrors() {
    return errors;
  }

  public void setTargetPath(Path targetPath) {
    this.targetPath = targetPath;
  }

  public void setException(Throwable e) {
    this.exception = e;
    this.status = STATUS.FAILURE;
  }

  public void addError(AssertionError e) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(e);
    this.status = STATUS.ERROR;
  }
}
