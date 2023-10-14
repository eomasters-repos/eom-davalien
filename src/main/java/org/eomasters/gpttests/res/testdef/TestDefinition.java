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

package org.eomasters.gpttests.res.testdef;

public class TestDefinition {

  private String testName;
  private String[] tags;
  private String gptCall = "";
  private ProductContent productContent;

  private TestDefinition() {
  }
  public TestDefinition(String name, ProductContent productContent) {
    this.testName = name;
    this.productContent = productContent;
  }

  public String getTestName() {
    return testName;
  }

  public String[] getTags() {
    return tags;
  }

  public String getGptCall() {
    return gptCall;
  }

  public ProductContent getProductContent() {
    return productContent;
  }
}
