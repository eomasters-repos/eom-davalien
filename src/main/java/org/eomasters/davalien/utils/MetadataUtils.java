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

package org.eomasters.davalien.utils;

import java.util.Arrays;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.MetadataElement;

/**
 * A class that provides utility methods for working with {@link MetadataElement} and {@link MetadataAttribute}.
 */
public class MetadataUtils {

  /**
   * Wraps a {@link MetadataElement} in a {@link MetadataWrapper}.
   *
   * @param element the element
   * @return the wrapper
   */
  public static MetadataWrapper wrap(MetadataElement element) {
    return new MetadataWrapper(element);
  }

  /**
   * This class wraps a {@link MetadataElement} in a {@link MetadataWrapper}. It provides a method for getting an
   * attribute by its path.
   */
  public static class MetadataWrapper {

    private final MetadataElement root;

    private MetadataWrapper(MetadataElement root) {
      this.root = root;
    }

    /**
     * Get an attribute by its path.
     *
     * @param path the path to the attribute
     * @return the attribute
     */
    public MetadataAttribute getElement(String path) {
      String[] tokens = path.split("/");
      String[] elemtokens = Arrays.copyOf(tokens, tokens.length - 1);
      MetadataElement elem = root;
      for (String token : elemtokens) {
        String[] split = token.matches(".*\\[\\d+]") ? token.split("\\[") : new String[]{token};
        int index = split.length > 1 ? Integer.parseInt(split[1].substring(0, split[1].length() - 1)) : -1;
        if (index >= 0) {
          try {
            elem = elem.getElementAt(index);
          } catch (IndexOutOfBoundsException e) {
            elem = null;
          }
        } else {
          String name = split[0];
          elem = elem.getElement(name);
        }
        if (elem == null) {
          return null;
        }
      }
      String attributeName = getAttributeName(path);
      return elem.getAttribute(attributeName);
    }

    public String getAttributeName(String path) {
      String[] tokens = path.split("/");
      return tokens[tokens.length - 1];
    }

  }

}
