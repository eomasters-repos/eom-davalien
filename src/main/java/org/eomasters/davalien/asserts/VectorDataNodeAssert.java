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

package org.eomasters.davalien.asserts;

import java.io.IOException;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assert;
import org.eomasters.davalien.res.testdef.Vector;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductNodeGroup;
import org.esa.snap.core.datamodel.VectorDataNode;

/**
 * {@link Assert} implementation for a {@link VectorDataNode}.
 */
@SuppressWarnings("UnusedReturnValue")
public class VectorDataNodeAssert extends AbstractAssert<VectorDataNodeAssert, ProductNodeGroup<VectorDataNode>> {

  /**
   * Creates an assert for the given  {@link ProductNodeGroup ProductNodeGroup<VectorDataNode>}.
   *
   * @param actual the actual value to verify
   */
  public VectorDataNodeAssert(ProductNodeGroup<VectorDataNode> actual) {
    super(actual, VectorDataNodeAssert.class);
    isNotNull();
  }

  /**
   * Checks if the actual {@link Product} has the given name.
   *
   * @param vector          the vector
   * @param indexOfExpected the index of the expected vector
   * @return the current {@link VectorDataNodeAssert}
   */
  public VectorDataNodeAssert has(Vector vector, int indexOfExpected) {
    if (vector != null) {
      String name = vector.getName();
      if (!actual.contains(name)) {
        failWithMessage("Vector[%d]: No vector with name [%s] found", indexOfExpected, name);
      }
      VectorDataNode vectorNode = actual.get(name);
      if (vector.getDescription() != null && !vector.getDescription().equals(vectorNode.getDescription())) {
        failWithMessage("Vector[%d]: Description of vector [%s] should be [%s] but was [%s]",
            indexOfExpected, name, vector.getDescription(), vectorNode.getDescription());
      }
      try {
        int count = vectorNode.getFeatureCollection().getCount();
        if (vector.getNumFeatures() != null && vector.getNumFeatures().compareTo(count) != 0) {
          failWithMessage("Vector[%d]: Number of features of vector [%s] should be [%d] but was [%d]",
              indexOfExpected, name, vector.getNumFeatures(), vectorNode.getFeatureCollection().size());
        }
      } catch (IOException e) {
        String msg = String.format(
            "Vector[%d]: Not able to retrieve features from vector node [%s] which shall have [%d] features(s)",
            indexOfExpected, name, vector.getNumFeatures());
        throw new IllegalStateException(msg, e);
      }
    }
    return this;
  }
}
