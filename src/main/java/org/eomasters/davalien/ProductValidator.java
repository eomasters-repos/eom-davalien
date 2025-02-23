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

package org.eomasters.davalien;

import static org.eomasters.davalien.asserts.ProductAssertions.assertThat;

import org.eomasters.davalien.asserts.ProductAssert;
import org.eomasters.davalien.res.testdef.ProductContent;
import org.esa.snap.core.datamodel.Product;

/**
 * Utility class for validating a {@link Product}.
 */
class ProductValidator {

  /**
   * Validates a {@link Product} against a {@link ProductContent}.
   *
   * @param testProduct     the {@link Product} to validate
   * @param expectedContent the {@link ProductContent} to validate against
   * @param testResult      the {@link TestResult} to add errors to
   */
  static void testProduct(Product testProduct, ProductContent expectedContent, TestResult testResult) {
    ProductAssert productAssert = assertThat(testProduct);
    runValidation(() -> productAssert.hasName(expectedContent.getName()), testResult);
    runValidation(() -> productAssert.hasProductType(expectedContent.getProductType()), testResult);
    runValidation(() -> productAssert.hasDescription(expectedContent.getDescription()), testResult);
    runValidation(() -> productAssert.hasSceneSize(expectedContent.getSceneSize()), testResult);
    runValidation(() -> productAssert.hasStartTime(expectedContent.getStartTime()), testResult);
    runValidation(() -> productAssert.hasEndTime(expectedContent.getEndTime()), testResult);
    runValidation(() -> productAssert.hasSampleCodings(expectedContent.getSampleCodings()), testResult);
    runValidation(() -> productAssert.hasGeoLocations(expectedContent.getGeoLocations()), testResult);
    runValidation(() -> productAssert.hasRasters(expectedContent.getRasters()), testResult);
    runValidation(() -> productAssert.hasVectors(expectedContent.getVectors()), testResult);
    runValidation(() -> productAssert.hasMetadata(expectedContent.getMetadata()), testResult);
  }

  private static void runValidation(InnerTest test, TestResult testResult) {
    try {
      test.run();
    } catch (AssertionError e) {
      testResult.addError(e);
    }
  }

  private interface InnerTest {

    void run() throws AssertionError;
  }

}
