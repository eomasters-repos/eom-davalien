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

package org.eomasters.davalien.res.testdef;

import java.util.List;
import java.util.Objects;

public class TestDefinition {

  public static final String GPT_CALL_REMINDER = "{Define the GPT Call here}";

  private String testName;
  private String description;
  private String gptCall = GPT_CALL_REMINDER;
  private String[] tags;
  private ProductContent expectation;

  private TestDefinition() {
  }
  public TestDefinition(String name, ProductContent expectation) {
    this.testName = name;
    this.expectation = expectation;
  }

  public TestDefinition(String name, String[] tags) {
    this.testName = name;
    this.tags = tags;
  }

  public String getTestName() {
    return testName;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getTags() {
    return tags != null ? List.of(tags) : List.of();
  }

  public String getGptCall() {
    return gptCall;
  }

  public ProductContent getExpectation() {
    return expectation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TestDefinition that = (TestDefinition) o;
    return Objects.equals(getTestName(), that.getTestName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTestName());
  }
}
