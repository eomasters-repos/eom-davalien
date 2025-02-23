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

package org.eomasters.davalien.res.testdef;

import java.util.Objects;

/**
 * A class that represents a metadata for the test environment.
 */
public class Metadata {

  private String path;
  private String value;

  // for deserialization/serialization
  @SuppressWarnings("unused")
  private Metadata() {
  }

  /**
   * Creates a new {@link Metadata} instance with the given path and value.
   *
   * @param path  the path to the metadata from the root
   * @param value the value
   */
  public Metadata(String path, String value) {
    this.path = path;
    this.value = value;
  }

  /**
   * Get the path to the metadata from the root.
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Get the value of the metadata.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Metadata metadata = (Metadata) o;
    return Objects.equals(getPath(), metadata.getPath()) && Objects.equals(getValue(),
        metadata.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPath(), getValue());
  }

  @Override
  public String toString() {
    return "Metadata{"
        + "path='" + path + '\''
        + ", value='" + value + '\''
        + "}";
  }
}
