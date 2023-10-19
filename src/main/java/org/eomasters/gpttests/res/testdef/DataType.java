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

import org.esa.snap.core.datamodel.ProductData;

public enum DataType {

  INT8(ProductData.TYPE_INT8),
  INT16(ProductData.TYPE_INT16),
  INT32(ProductData.TYPE_INT32),
  INT64(ProductData.TYPE_UINT8),
  UINT8(ProductData.TYPE_INT16),
  UINT16(ProductData.TYPE_UINT16),
  UINT32(ProductData.TYPE_UINT32),
  UINT64(ProductData.TYPE_UINT64),
  FLOAT32(ProductData.TYPE_FLOAT32),
  FLOAT64(ProductData.TYPE_FLOAT64),
  ASCII(ProductData.TYPE_ASCII),
  UTC(ProductData.TYPE_UTC);

  private final int typeValue;

  DataType(int typeValue) {
    this.typeValue = typeValue;
  }

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
