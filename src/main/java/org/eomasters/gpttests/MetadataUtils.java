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

import java.util.Arrays;
import org.esa.snap.core.datamodel.MetadataElement;

public class MetadataUtils {

  public static MetadataWrapper wrap(MetadataElement root) {
    return new MetadataWrapper(root);
  }

  public static class MetadataWrapper {

    private final MetadataElement root;

    public MetadataWrapper(MetadataElement root) {
      this.root = root;
    }

    public String get(String path) {
      String[] tokens = path.split("/");
      String[] elemtokens = Arrays.copyOf(tokens, tokens.length - 1);
      String attrToken = tokens[tokens.length - 1];
      MetadataElement elem = root;
      for (String token : elemtokens) {
        String[] split = token.matches(".*\\[\\d+\\]") ? token.split("\\[") : new String[]{token};
        int index = split.length > 1 ? Integer.parseInt(split[1].substring(0, split[1].length() - 1)) : -1;
        if (index >= 0) {
          elem = elem.getElementAt(index);
        } else {
          String name = split[0];
          elem = elem.getElement(name);
        }
      }
      return elem.getAttributeString(attrToken);
    }
  }
}
