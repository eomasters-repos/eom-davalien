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

import org.assertj.core.api.AbstractAssert;
import org.eomasters.davalien.res.testdef.Metadata;
import org.eomasters.davalien.utils.MetadataUtils;
import org.eomasters.davalien.utils.MetadataUtils.MetadataWrapper;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.MetadataElement;

public class MetadataAssert extends AbstractAssert<MetadataAssert, MetadataElement> {

  private final MetadataWrapper wrap;
  private final int index;

  public MetadataAssert(MetadataElement actual, int index) {
    super(actual, MetadataAssert.class);
    this.index = index;
    isNotNull();
    wrap = MetadataUtils.wrap(actual);
  }

  public void has(Metadata metadata) {
    if (metadata != null) {
      String path = metadata.getPath();
      String expValue = metadata.getValue();

      MetadataAttribute metadataAttribute = wrap.getElement(path);

      if (metadataAttribute == null) {
        failWithMessage("Metadata[%d]: No attribute for path [%s] found", index, path);
      } else {
        String actValue = metadataAttribute.getData().getElemString();
        if (!expValue.equals(actValue)) {
          failWithMessage("Metadata[%d]: Value of metadata attribute [%s] should be [%s] but was [%s]]",
              index, path, expValue, actValue);
        }
      }
    }
  }
}
