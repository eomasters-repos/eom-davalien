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

package org.eomasters.davalien.res.testdef;

import java.util.Arrays;
import java.util.Objects;

public class Coding {

  private String name;
  private Sample[] samples;

  public Coding(String name, Sample... samples) {
    this.name = name;
    this.samples = samples;
  }

  private Coding() {
  }

  public String getName() {
    return name;
  }

  public Sample[] getSamples() {
    return samples;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coding coding = (Coding) o;
    return Objects.equals(getName(), coding.getName()) && Arrays.equals(getSamples(),
        coding.getSamples());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getName());
    result = 31 * result + Arrays.hashCode(getSamples());
    return result;
  }

  @Override
  public String toString() {
    return "Coding{" +
        "name='" + name + '\'' +
        ", samples=" + Arrays.toString(samples) +
        '}';
  }

  public static class Sample {
    private String name;
    private String description;
    private long value;

    public Sample(String name, String description, long value) {
      this.name = name;
      this.description = description;
      this.value = value;
    }

    private Sample() {
    }

    public String getName() {
      return name;
    }

    public String getDescription() {
      return description;
    }

    public long getValue() {
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
      Sample sample = (Sample) o;
      return getValue() == sample.getValue() && Objects.equals(getName(), sample.getName())
          && Objects.equals(getDescription(), sample.getDescription());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getName(), getDescription(), getValue());
    }

    @Override
    public String toString() {
      return "Sample{" +
          "name='" + name + '\'' +
          ", description='" + description + '\'' +
          ", sampleValue=" + value +
          '}';
    }
  }
}
