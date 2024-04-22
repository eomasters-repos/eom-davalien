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

package org.eomasters.davalien.res.testdef;

import org.esa.snap.core.datamodel.RasterDataNode;

/**
 * Defines the supported raster types.
 */
public enum RasterType {
  /**
   * Raster of the type {@link org.esa.snap.core.datamodel.Band band}.
   */
  BAND,
  /**
   * Raster of the type {@link org.esa.snap.core.datamodel.TiePointGrid tie point grid}.
   */
  TIE_POINT,
  /**
   * Raster of the type {@link org.esa.snap.core.datamodel.Mask mask}.
   */
  MASK,
  /**
   * Raster of the type {@link org.esa.snap.core.datamodel.VirtualBand virtual band}.
   */
  VIRTUAL,
  /**
   * Raster of the type {@link org.esa.snap.core.datamodel.FilterBand filter band}.
   */
  FILTER;

  /**
   * Get the raster type of the given raster.
   *
   * @param raster the raster
   * @return the raster type
   */
  public static RasterType get(RasterDataNode raster) {
    if (raster instanceof org.esa.snap.core.datamodel.TiePointGrid) {
      return TIE_POINT;
    } else if (raster instanceof org.esa.snap.core.datamodel.Mask) {
      return MASK;
    } else if (raster instanceof org.esa.snap.core.datamodel.VirtualBand) {
      return VIRTUAL;
    } else if (raster instanceof org.esa.snap.core.datamodel.FilterBand) {
      return FILTER;
    } else if (raster instanceof org.esa.snap.core.datamodel.Band) {
      return BAND;
    } else {
      throw new IllegalArgumentException(
          String.format("Unknown type [%s] of raster '%s':", raster.getClass(), raster.getName()));
    }
  }
}
