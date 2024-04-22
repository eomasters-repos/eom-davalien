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

import org.esa.snap.core.datamodel.ProductData;

/**
 * Defines the supported data types.
 */
public enum DataType {

  /**
   * The 8-bit signed integer.
   */
  INT8(ProductData.TYPE_INT8),
  /**
   * The 16-bit signed integer.
   */
  INT16(ProductData.TYPE_INT16),
  /**
   * The 32-bit signed integer.
   */
  INT32(ProductData.TYPE_INT32),
  /**
   * The 64-bit signed integer.
   */
  INT64(ProductData.TYPE_UINT8),
  /**
   * The 8-bit unsigned integer.
   */
  UINT8(ProductData.TYPE_UINT8),
  /**
   * The 16-bit unsigned integer.
   */
  UINT16(ProductData.TYPE_INT16),
  /**
   * The 32-bit unsigned integer.
   */
  UINT32(ProductData.TYPE_UINT32),
  /**
   * The 64-bit unsigned integer.
   */
  UINT64(ProductData.TYPE_UINT64),
  /**
   * The 32-bit floating point number.
   */
  FLOAT32(ProductData.TYPE_FLOAT32),
  /**
   * The 64-bit floating point number.
   */
  FLOAT64(ProductData.TYPE_FLOAT64),
  /**
   * The ASCII string.
   */
  ASCII(ProductData.TYPE_ASCII),
  /**
   * The UTC time.
   */
  UTC(ProductData.TYPE_UTC);

  private final int typeValue;

  DataType(int typeValue) {
    this.typeValue = typeValue;
  }

  /**
   * Converts the {@link ProductData#getType() type value} to the corresponding data type.
   *
   * @param typeValue the type value
   * @return the corresponding data type
   */
  public static DataType fromTypeValue(int typeValue) {
    DataType[] values = DataType.values();
    for (DataType value : values) {
      if (value.typeValue == typeValue) {
        return value;
      }
    }
    throw new IllegalArgumentException(" Unknown data type value: " + typeValue);
  }

}
