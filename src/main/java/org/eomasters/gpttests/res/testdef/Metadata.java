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

package org.eomasters.gpttests.res.testdef;

import java.util.Objects;

public class Metadata {

  private String path;
  private String value;

  private Metadata() {
  }

  public Metadata(String path, String value) {
    this.path = path;
    this.value = value;
  }

  public String getPath() {
    return path;
  }

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
    return "Metadata{" +
        "path='" + path + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
