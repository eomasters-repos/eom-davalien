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