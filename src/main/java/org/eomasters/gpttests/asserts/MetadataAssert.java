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

import org.assertj.core.api.AbstractAssert;
import org.eomasters.gpttests.MetadataUtils;
import org.eomasters.gpttests.MetadataUtils.MetadataWrapper;
import org.eomasters.gpttests.res.testdef.Metadata;
import org.esa.snap.core.datamodel.MetadataElement;

public class MetadataAssert extends AbstractAssert<MetadataAssert, MetadataElement> {

  private final MetadataWrapper wrap;

  public MetadataAssert(MetadataElement actual) {
    super(actual, MetadataAssert.class);
    isNotNull();
    wrap = MetadataUtils.wrap(actual);
  }

  public void has(Metadata metadata) {
    if (metadata != null) {
      String path = metadata.getPath();
      String value = metadata.getValue();
      String s = wrap.get(path);
      if (!value.equals(s)) {
        failWithMessage("Expected metadata <%s> to be <%s> but was <%s>", path, value, s);
      }
    }
  }
}
