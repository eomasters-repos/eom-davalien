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

package org.eomasters.gpttests.asserts;

import java.io.IOException;
import org.assertj.core.api.AbstractAssert;
import org.eomasters.gpttests.res.testdef.Vector;
import org.esa.snap.core.datamodel.ProductNodeGroup;
import org.esa.snap.core.datamodel.VectorDataNode;

public class VectorDataNodeAssert extends AbstractAssert<VectorDataNodeAssert, ProductNodeGroup<VectorDataNode>> {

  public VectorDataNodeAssert(ProductNodeGroup<VectorDataNode> actual) {
    super(actual, VectorDataNodeAssert.class);
    isNotNull();
  }

  public VectorDataNodeAssert has(Vector vector) {
    if (vector != null) {
      String name = vector.getName();
      if (!actual.contains(name)) {
        failWithMessage("Expected vector <%s> to be present but was not", name);
      }
      VectorDataNode vectorNode = actual.get(name);
      if (!vectorNode.getDescription().equals(vector.getDescription())) {
        failWithMessage("Expected vector <%s> to have description <%s> but was <%s>", name, vector.getDescription(),
            vectorNode.getDescription());
      }
      int count = -1;
      try {
        count = vectorNode.getFeatureCollection().getCount();
      } catch (IOException e) {
        failWithMessage("Failed to retrieve number of features from vector node <%s> which shall have <%s> features(s)",
            name, vector.getNumFeatures());
      }
      if (count != vector.getNumFeatures()) {
        failWithMessage("Expected vector <%s> to have <%s> features(s) but was <%s>", name, vector.getNumFeatures(),
            count);
      }
    }
    return this;
  }
}
