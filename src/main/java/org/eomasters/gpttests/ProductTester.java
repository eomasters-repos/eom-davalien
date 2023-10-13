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

import static org.eomasters.gpttests.asserts.ProductAssertions.assertThat;

import org.eomasters.gpttests.asserts.ProductAssert;
import org.eomasters.gpttests.res.testdef.ProductContent;
import org.esa.snap.core.datamodel.Product;

public class ProductTester {

  static void testProduct(Product testProduct, ProductContent expectedContent, TestResult testResult) {
    ProductAssert productAssert = assertThat(testProduct);
    runTest(() -> productAssert.hasName(expectedContent.getName()), testResult);
    runTest(() -> productAssert.hasProductType(expectedContent.getProductType()), testResult);
    runTest(() -> productAssert.hasDescription(expectedContent.getDescription()), testResult);
    runTest(() -> productAssert.hasSceneSize(expectedContent.getSceneSize()), testResult);
    runTest(() -> productAssert.hasStartTime(expectedContent.getStartTime()), testResult);
    runTest(() -> productAssert.hasEndTime(expectedContent.getEndTime()), testResult);
    runTest(() -> productAssert.hasSampleCodings(expectedContent.getSampleCodings()), testResult);
    runTest(() -> productAssert.hasGeoLocations(expectedContent.getGeoLocations()), testResult);
    runTest(() -> productAssert.hasRasters(expectedContent.getRasters()), testResult);
    runTest(() -> productAssert.hasVectors(expectedContent.getVectors()), testResult);
    runTest(() -> productAssert.hasMetadata(expectedContent.getMetadata()), testResult);
  }

  private static void runTest(InnerTest test, TestResult testResult) {
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
