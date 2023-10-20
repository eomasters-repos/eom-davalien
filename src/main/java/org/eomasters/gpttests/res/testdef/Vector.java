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

public class Vector {

  private String name;
  private String description;
  private Integer numFeatures;

  public Vector(String name, String description, int count) {
    this.name = name;
    this.description = description;
    this.numFeatures = count;
  }

  private Vector() {
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Integer getNumFeatures() {
    return numFeatures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vector vector = (Vector) o;
    return getNumFeatures() == vector.getNumFeatures() && Objects.equals(getName(), vector.getName())
        && Objects.equals(getDescription(), vector.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getDescription(), getNumFeatures());
  }

  @Override
  public String toString() {
    return String.format("Vector{name='%s', description='%s', numFeatures=%d}", name, description, numFeatures);
  }
}
