/*-
 * ========================LICENSE_START=================================
 * EOMasters GPT Test Environment - This projects provides a test environment for operators you have developed.
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

package org.eomasters.davalien.res.testdef;

import java.util.List;
import java.util.Objects;

/**
 * A class that represents a test definition for the test environment.
 */
public class TestDefinition {

  public static final String GPT_CALL_REMINDER = "{Define the GPT Call here}";

  private String testName;
  private String description;
  private String gptCall = GPT_CALL_REMINDER;
  private String[] tags;
  private ProductContent expectation;

  // for deserialization/serialization
  @SuppressWarnings("unused")
  private TestDefinition() {
  }

  /**
   * Create a new instance.
   *
   * @param name        The name of the test
   * @param expectation The expectation
   */
  public TestDefinition(String name, ProductContent expectation) {
    this.testName = name;
    this.expectation = expectation;
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
   * Get the tags of the test.
   *
   * @return The tags
   */
  public List<String> getTags() {
    return tags != null ? List.of(tags) : List.of();
  }

  /**
   * Get the gpt command line call of the test.
   *
   * @return the gpt call
   */
  public String getGptCall() {
    return gptCall;
  }

  /**
   * Get the expectation of the test.
   *
   * @return the expectation
   */
  public ProductContent getExpectation() {
    return expectation;
  }

  /**
   * Set the name of the test.
   *
   * @param testName the name of the test
   */
  public void setTestName(String testName) {
    this.testName = testName;
  }

  /**
   * Set the description of the test.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the tags of the test.
   *
   * @param tags the tags to set
   */
  public void setTags(List<String> tags) {
    this.tags = tags.toArray(new String[0]);
  }

  /**
   * Set the gpt command line call of the test.
   *
   * @param gptCall the gpt call to set
   */
  @SuppressWarnings("unused")
  public void setGptCall(String gptCall) {
    this.gptCall = gptCall;
  }

  /**
   * Set the expectation of the test.
   *
   * @param expectation the expectation
   */
  @SuppressWarnings("unused")
  public void setExpectation(ProductContent expectation) {
    this.expectation = expectation;
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
