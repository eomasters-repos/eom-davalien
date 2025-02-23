/*-
 * ========================LICENSE_START=================================
 * EOMasters DAVALIEN - The DAta VALIdation ENvironment for quality assurance of EO data.
 * -> https://www.eomasters.org/davalien
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

import static org.junit.jupiter.api.Assertions.*;

import org.esa.snap.core.datamodel.ProductData;
import org.junit.jupiter.api.Test;

class DataTypeTest {

  @Test
  void fromTypeValue() {
    assertEquals(DataType.INT8, DataType.fromTypeValue(ProductData.TYPE_INT8));
    assertEquals(DataType.INT16, DataType.fromTypeValue(ProductData.TYPE_INT16));
    assertEquals(DataType.INT32, DataType.fromTypeValue(ProductData.TYPE_INT32));
    assertEquals(DataType.INT64, DataType.fromTypeValue(ProductData.TYPE_INT64));
    assertEquals(DataType.UINT8, DataType.fromTypeValue(ProductData.TYPE_UINT8));
    assertEquals(DataType.UINT16, DataType.fromTypeValue(ProductData.TYPE_UINT16));
    assertEquals(DataType.UINT32, DataType.fromTypeValue(ProductData.TYPE_UINT32));
    assertEquals(DataType.UINT64, DataType.fromTypeValue(ProductData.TYPE_UINT64));
    assertEquals(DataType.FLOAT32, DataType.fromTypeValue(ProductData.TYPE_FLOAT32));
    assertEquals(DataType.FLOAT64, DataType.fromTypeValue(ProductData.TYPE_FLOAT64));
    assertEquals(DataType.ASCII, DataType.fromTypeValue(ProductData.TYPE_ASCII));
    assertEquals(DataType.UTC, DataType.fromTypeValue(ProductData.TYPE_UTC));
  }

  @Test
  void fromTypeValueInvalid() {
    assertThrows(IllegalArgumentException.class, () -> DataType.fromTypeValue(99999));
  }
}
