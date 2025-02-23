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
import org.esa.snap.core.datamodel.PixelPos;

/**
 * Represents a pixel in the image.
 */
public class Pixel {

  PixelPos position;
  double value;
  double eps = 1.0e-8;

  /**
   * Creates a new pixel with the given position and value.
   *
   * @param position The pixel position
   * @param value    The pixel value
   */
  public Pixel(PixelPos position, double value) {
    this.position = position;
    this.value = value;
  }

  // for deserialization/serialization
  @SuppressWarnings("unused")
  private Pixel() {
  }

  /**
   * Get the pixel position.
   *
   * @return the pixel position
   */
  public PixelPos getPosition() {
    return position;
  }

  /**
   * Get the pixel value.
   *
   * @return the pixel value
   */
  public double getValue() {
    return value;
  }

  /**
   * Get the allowed error value.
   *
   * @return the allowed error
   */
  public double getEps() {
    return eps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pixel pixel = (Pixel) o;
    return Double.compare(getValue(), pixel.getValue()) == 0
        && Double.compare(getEps(), pixel.getEps()) == 0 && Objects.equals(getPosition(),
        pixel.getPosition());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPosition(), getValue(), getEps());
  }

  @Override
  public String toString() {
    return String.format("Pixel{position=[%s,%s], value=%s, eps=%s}", position.x, position.y, value, eps);
  }
}
