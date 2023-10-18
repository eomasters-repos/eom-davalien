/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;
import org.eomasters.gpttests.res.testdef.Coding.Sample;
import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.IndexCoding;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.MetadataElement;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.ProductData.UTC;
import org.esa.snap.core.datamodel.TiePointGrid;
import org.esa.snap.rcp.util.TestProducts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ProductContentFactoryTest {

  private static Product testProduct;

  @BeforeAll
  static void beforeAll() throws ParseException {
    testProduct = TestProducts.createProduct1();
    TiePointGrid[] grids = testProduct.getTiePointGrids();
    for (TiePointGrid grid : grids) {
      grid.setData(ProductData.createInstance(createRandomPoints(grid.getGridWidth() * grid.getGridHeight())));
    }
    testProduct.getBandGroup().remove(testProduct.getBandGroup().get("Band_B"));
    testProduct.getMaskGroup().remove(testProduct.getMaskGroup().get("Mask_B"));
    testProduct.setDescription("The description of the test product");
    testProduct.setStartTime(UTC.parse("01-JAN-2019 00:00:00"));
    testProduct.setEndTime(UTC.parse("01-JAN-2019 00:12:00"));
    testProduct.getMetadataRoot().getElement("Global_Attributes").addAttribute(new MetadataAttribute("attribute1",
        ProductData.createInstance("value1"), true));
    MetadataElement subElement = new MetadataElement("SubElement");
    subElement.addAttribute(new MetadataAttribute("attribute1", ProductData.createInstance("value1"), true));
    testProduct.getMetadataRoot().getElement("Local_Attributes").addElement(subElement);
    FlagCoding flagCoding = new FlagCoding("FlagCoding1");
    flagCoding.addFlag("Flag1", 1, "Flag1 description");
    testProduct.getFlagCodingGroup().add(flagCoding);
    IndexCoding indexCoding = new IndexCoding("IndexCoding1");
    indexCoding.addIndex("Index1", 2, "Index1 description");
    testProduct.getIndexCodingGroup().add(indexCoding);
  }

  @Test
  public void testCreation() throws IOException, ParseException {
    ProductContent pc = ProductContentFactory.create(testProduct, new Random(123546789));
    assertEquals("Test_Product_1", pc.getName());
    assertEquals("Test_Type_1", pc.getProductType());
    assertEquals("The description of the test product", pc.getDescription());
    assertEquals(UTC.parse("01-JAN-2019 00:00:00").toString(), pc.getStartTime().toString());
    assertEquals(UTC.parse("01-JAN-2019 00:12:00").toString(), pc.getEndTime().toString());
    assertEquals(new Dimension(2048, 1024), pc.getSceneSize());
    assertArrayEquals(new Metadata[]{new Metadata("Global_Attributes/attribute1", "value1")}, pc.getMetadata());
    assertArrayEquals(
        new GeoLocation[]{
            new GeoLocation(new PixelPos(189.5, 333.5), new GeoPos(-28.147249526715008, -74.54058482980994)),
            new GeoLocation(new PixelPos(670.5, 635.5), new GeoPos(-19.735983137651782, -67.30875379743314)),
            new GeoLocation(new PixelPos(924.5, 425.5), new GeoPos(-22.188298181097036, -61.35729458577718))},
        pc.getGeoLocations());
    Coding flagCoding = new Coding("FlagCoding1", new Sample("Flag1", "Flag1 description", 1));
    Coding indexCoding = new Coding("IndexCoding1", new Sample("Index1", "Index1 description", 2));
    assertArrayEquals(new Coding[]{flagCoding, indexCoding}, pc.getSampleCodings());
    Raster bandA = new Raster("Band_A", null);
    bandA.setDataType(DataType.FLOAT32);
    bandA.setRasterType(RasterType.VIRTUAL);
    bandA.setNoDataValue(0.0);
    bandA.setNoDataValueUsed(false);
    bandA.setValidPixelExpression(null);
    bandA.setSize(new Dimension(2048, 1024));
    bandA.setPixels(new Pixel[]{new Pixel(new PixelPos(843.5, 462.5), 0.8196410536766052),
        new Pixel(new PixelPos(1679.5, 7.5), 0.6214187741279602),
        new Pixel(new PixelPos(869.5, 207.5), 0.9486163258552551)});
    Raster maskA = new Raster("Mask_A", "I am Mask A");
    maskA.setDataType(DataType.INT64);
    maskA.setRasterType(RasterType.MASK);
    maskA.setNoDataValue(0.0);
    maskA.setNoDataValueUsed(false);
    maskA.setValidPixelExpression(null);
    maskA.setSize(new Dimension(2048, 1024));
    maskA.setPixels(new Pixel[]{new Pixel(new PixelPos(1830.5, 11.5), 0),
        new Pixel(new PixelPos(931.5, 249.5), 0),
        new Pixel(new PixelPos(1760.5, 448.5), 0)});
    Raster gridA = new Raster("Grid_A", null);
    gridA.setDataType(DataType.FLOAT32);
    gridA.setRasterType(RasterType.TIE_POINT);
    gridA.setNoDataValue(0.0);
    gridA.setNoDataValueUsed(false);
    gridA.setValidPixelExpression(null);
    gridA.setSize(new Dimension(2048, 1024));
    gridA.setPixels(new Pixel[]{new Pixel(new PixelPos(1334.5, 977.5), 0.9908178506757395),
        new Pixel(new PixelPos(1314.5, 628.5), 0.09566003576219373),
        new Pixel(new PixelPos(1149.5, 339.5), 0.18507748550661063)});
    Raster gridB = new Raster("Grid_B", null);
    gridB.setDataType(DataType.FLOAT32);
    gridB.setRasterType(RasterType.TIE_POINT);
    gridB.setNoDataValue(0.0);
    gridB.setNoDataValueUsed(false);
    gridB.setValidPixelExpression(null);
    gridB.setSize(new Dimension(2048, 1024));
    gridB.setPixels(new Pixel[]{new Pixel(new PixelPos(382.5, 109.5), 0.3192272532688776),
        new Pixel(new PixelPos(1373.5, 315.5), 1.418094261418446),
        new Pixel(new PixelPos(1611.5, 507.5), 0.11737141071535007)});

    assertArrayEquals(new Raster[]{bandA, maskA, gridA, gridB}, pc.getRasters());
    assertArrayEquals(new Vector[]{new Vector("pins", "", 0),
        new Vector("ground_control_points", "", 0)}, pc.getVectors());

  }

  private static float[] createRandomPoints(int n) {
    Random random = new Random(987);
    float[] pnts = new float[n];
    for (int i = 0; i < pnts.length; i++) {
      pnts[i] = (float) random.nextGaussian();
    }
    return pnts;
  }
}
