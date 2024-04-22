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
import org.assertj.core.api.Assert;
import org.eomasters.davalien.res.testdef.Metadata;
import org.eomasters.davalien.utils.MetadataUtils;
import org.eomasters.davalien.utils.MetadataUtils.MetadataWrapper;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.MetadataElement;

/**
 * {@link Assert} implementation for {@link MetadataElement metadata }.
 */
public class MetadataAssert extends AbstractAssert<MetadataAssert, MetadataElement> {

  private final MetadataWrapper wrap;

  /**
   * Creates an assert for the given {@link MetadataElement metadata}.
   *
   * @param actual the {@link MetadataElement metadata} to verify
   */
  public MetadataAssert(MetadataElement actual) {
    super(actual, MetadataAssert.class);
    isNotNull();
    wrap = MetadataUtils.wrap(actual);
  }

  /**
   * Checks if the {@link MetadataElement} has the given value.
   *
   * @param metadata the {@link Metadata metadata}
   * @param indexOfExpected the index of the expected metadata element
   */
  public void has(Metadata metadata, int indexOfExpected) {
    if (metadata != null) {
      String path = metadata.getPath();
      String expValue = metadata.getValue();

      MetadataAttribute metadataAttribute = wrap.getElement(path);

      if (metadataAttribute == null) {
        failWithMessage("Metadata[%d]: No attribute for path [%s] found", indexOfExpected, path);
      } else {
        String actValue = metadataAttribute.getData().getElemString();
        if (!expValue.equals(actValue)) {
          failWithMessage("Metadata[%d]: Value of metadata attribute [%s] should be [%s] but was [%s]]",
              indexOfExpected, path, expValue, actValue);
        }
      }
    }
  }
}
