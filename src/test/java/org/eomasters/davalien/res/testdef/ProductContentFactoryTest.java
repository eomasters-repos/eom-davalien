/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.text.ParseException;
import java.util.Random;
import org.eomasters.davalien.DavalienException;
import org.eomasters.davalien.res.testdef.Coding.Sample;
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
    int seed = 987;
    for (TiePointGrid grid : grids) {
      grid.setData(ProductData.createInstance(createRandomPoints(grid.getGridWidth() * grid.getGridHeight(),
          new Random(seed++))));
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
  public void testCreation() throws DavalienException, ParseException {
    ProductContent pc = ProductContentFactory.create(testProduct);
    assertEquals("Test_Product_1", pc.getName());
    assertEquals("Test_Type_1", pc.getProductType());
    assertEquals("The description of the test product", pc.getDescription());
    assertEquals(UTC.parse("01-JAN-2019 00:00:00").toString(), pc.getStartTime().toString());
    assertEquals(UTC.parse("01-JAN-2019 00:12:00").toString(), pc.getEndTime().toString());
    assertEquals(new Dimension(2048, 1024), pc.getSceneSize());
    assertArrayEquals(new Metadata[]{new Metadata("Local_Attributes/SubElement/attribute1", "value1"),
        new Metadata("Global_Attributes/attribute1", "value1")}, pc.getMetadata());
    assertArrayEquals(
        new GeoLocation[]{
            new GeoLocation(new PixelPos(1496.5, 102.5), new GeoPos(-24.913613176747123, -48.82007781758462)),
            new GeoLocation(new PixelPos(839.5, 417.5), new GeoPos(-22.828179225613027, -62.897127396761285)),
            new GeoLocation(new PixelPos(425.5, 37.5), new GeoPos(-32.30812515189281, -68.42860450548574))},
        pc.getGeoLocations());
    Coding flagCoding = new Coding("FlagCoding1", new Sample("Flag1", "Flag1 description", 1));
    Coding indexCoding = new Coding("IndexCoding1", new Sample("Index1", "Index1 description", 2));
    assertArrayEquals(new Coding[]{flagCoding, indexCoding}, pc.getSampleCodings());
    Raster gridA = new Raster("Grid_A", null);
    gridA.setDataType(DataType.FLOAT32);
    gridA.setRasterType(RasterType.TIE_POINT);
    gridA.setNoDataValue(0.0);
    gridA.setNoDataValueUsed(false);
    gridA.setValidPixelExpression(null);
    gridA.setSize(new Dimension(2048, 1024));
    gridA.setPixels(new Pixel[]{new Pixel(new PixelPos(1497.5, 300.5), -0.7569640792371501),
        new Pixel(new PixelPos(1846.5, 4.5), -0.4758454715870357),
        new Pixel(new PixelPos(1017.5, 875.5), 0.35383669314433064)});
    gridA.setMinimum(-3.850964069366455);
    gridA.setMaximum(5.2165021896362305);
    gridA.setHistogramBins(
        new int[]{16, 15, 32, 31, 35, 35, 34, 35, 37, 37, 34, 40, 35, 42, 34, 43, 36, 45, 35, 47, 36, 48, 36, 50, 36,
            53, 36, 55, 37, 56, 40, 55, 39, 59, 42, 57, 45, 56, 47, 57, 52, 54, 54, 54, 55, 58, 57, 53, 66, 52, 66, 53,
            73, 51, 77, 57, 82, 53, 94, 57, 98, 69, 95, 71, 104, 78, 115, 80, 124, 88, 136, 92, 141, 104, 155, 114, 173,
            124, 197, 128, 198, 155, 204, 178, 191, 199, 223, 222, 231, 267, 247, 281, 264, 287, 317, 300, 340, 352,
            367, 377, 416, 429, 448, 493, 488, 549, 551, 586, 635, 680, 681, 744, 750, 822, 846, 895, 938, 972, 1065,
            1086, 1129, 1203, 1356, 1361, 1415, 1403, 1529, 1544, 1631, 1712, 1810, 2125, 2264, 2364, 2617, 2587, 2731,
            2844, 2863, 2865, 2945, 3049, 3170, 3252, 3389, 3500, 3618, 3705, 3932, 3978, 4387, 4276, 4435, 4510, 4726,
            4887, 5206, 5613, 6096, 6499, 6570, 7057, 7527, 7782, 8068, 8327, 8607, 8911, 9398, 9633, 9950, 10004,
            10459, 10973, 11539, 12113, 12872, 13293, 14656, 14334, 14537, 14675, 14837, 15043, 15409, 15928, 17143,
            18033, 17931, 18317, 18277, 18590, 18770, 18829, 19241, 19905, 20724, 21168, 22026, 21343, 21032, 21175,
            21343, 21133, 21025, 21635, 21512, 21311, 21419, 21724, 21389, 21349, 21491, 21853, 21792, 21738, 21733,
            22291, 22293, 22003, 21907, 21450, 21049, 21184, 21343, 21023, 20769, 20675, 20431, 19697, 19572, 19569,
            19918, 19391, 19026, 18438, 18160, 18138, 18123, 17833, 16754, 15880, 15414, 15072, 14850, 14303, 13855,
            13648, 13342, 13327, 13268, 13357, 13219, 12700, 12247, 11981, 11921, 11333, 11038, 10801, 10201, 10074,
            9927, 9371, 8752, 8512, 8065, 7620, 7322, 7164, 6886, 6865, 6865, 6626, 6248, 5996, 5762, 5564, 5372, 5159,
            5234, 4799, 4621, 4396, 4221, 4067, 3925, 3882, 3714, 3517, 3424, 3224, 3146, 3112, 2998, 2902, 2929, 3041,
            2772, 2575, 2382, 2350, 2153, 2140, 1975, 1941, 1842, 1759, 1714, 1631, 1580, 1517, 1416, 1387, 1361, 1301,
            1213, 1217, 1132, 1119, 1113, 1052, 1005, 1001, 948, 936, 893, 874, 846, 810, 772, 749, 748, 675, 665, 636,
            637, 577, 611, 527, 551, 490, 434, 406, 408, 385, 371, 372, 355, 347, 337, 332, 318, 306, 306, 294, 289,
            279, 265, 252, 264, 246, 238, 245, 221, 218, 233, 207, 218, 202, 205, 196, 196, 181, 186, 187, 171, 183,
            162, 171, 152, 172, 143, 153, 137, 144, 138, 140, 126, 134, 122, 120, 115, 117, 114, 110, 104, 102, 106, 86,
            106, 77, 104, 71, 92, 71, 81, 76, 78, 75, 69, 72, 69, 66, 74, 62, 66, 66, 59, 65, 57, 64, 53, 58, 58, 59,
            54, 51, 49, 59, 45, 55, 45, 45, 55, 43, 55, 43, 48, 50, 45, 47, 50, 41, 51, 42, 43, 49, 39, 51, 39, 44, 44,
            42, 38, 42, 43, 44, 39, 40, 43, 34, 43, 37, 39, 39, 38, 37, 38, 37, 35, 42, 30, 39, 36, 32, 40, 28, 32, 28,
            24, 27, 19, 20, 19, 15, 11, 15, 6, 7, 5, 3, 2, 3, 2, 3, 2, 2, 2, 2, 2, 1, 1, 3, 0, 1, 2, 0, 1, 1, 0, 1});
    Raster gridB = new Raster("Grid_B", null);
    gridB.setDataType(DataType.FLOAT32);
    gridB.setRasterType(RasterType.TIE_POINT);
    gridB.setNoDataValue(0.0);
    gridB.setNoDataValueUsed(false);
    gridB.setValidPixelExpression(null);
    gridB.setSize(new Dimension(2048, 1024));
    gridB.setPixels(new Pixel[]{new Pixel(new PixelPos(1497.5, 300.5), -2.091343596108345),
        new Pixel(new PixelPos(1846.5, 4.5), 0.2556064154314299),
        new Pixel(new PixelPos(1017.5, 875.5), 0.3860352358879027)});
    gridB.setMinimum(-4.946466445922852);
    gridB.setMaximum(8.427169799804688);
    gridB.setHistogramBins(
        new int[]{2, 1, 2, 2, 2, 3, 3, 3, 4, 3, 5, 4, 5, 6, 5, 6, 6, 7, 6, 8, 9, 8, 8, 10, 9, 9, 11, 10, 10, 11, 13, 12,
            12, 13, 11, 17, 11, 15, 16, 14, 15, 18, 12, 21, 17, 21, 22, 22, 25, 31, 30, 41, 35, 47, 47, 54, 53, 58, 69,
            68, 67, 79, 78, 85, 82, 104, 99, 95, 104, 107, 118, 125, 121, 127, 142, 132, 149, 156, 161, 168, 167, 184,
            185, 195, 191, 212, 210, 234, 247, 252, 267, 275, 287, 286, 323, 327, 326, 367, 368, 389, 422, 426, 446,
            463, 493, 505, 534, 524, 560, 594, 625, 652, 707, 723, 796, 825, 876, 936, 992, 1084, 1186, 1347, 1441,
            1551, 1681, 1808, 1844, 2040, 2164, 2290, 2418, 2579, 2703, 2856, 3071, 3314, 3508, 3749, 4036, 4351, 4668,
            5078, 5669, 6382, 6600, 7189, 7357, 7845, 8268, 8898, 9134, 9709, 10340, 11114, 11711, 12072, 12409, 12927,
            13842, 14373, 15424, 16311, 16487, 17169, 17609, 18291, 19023, 20148, 21232, 22411, 23719, 24404, 24606,
            24931, 25485, 25758, 25687, 25940, 26129, 26313, 26446, 27079, 27867, 27791, 28041, 28590, 29144, 29608,
            29840, 30039, 29738, 29967, 30324, 30624, 30618, 31436, 31250, 30502, 30449, 30246, 29974, 30455, 30175,
            29573, 27313, 26200, 26059, 25461, 25048, 25023, 23956, 23404, 23711, 22992, 22776, 22404, 21269, 20476,
            18526, 16944, 16194, 16134, 15233, 13782, 12892, 12136, 11590, 11151, 10705, 10349, 10416, 9884, 9330, 8811,
            8565, 8029, 7530, 6805, 6397, 5976, 5700, 5276, 4935, 4581, 4242, 3991, 3725, 3571, 3396, 3217, 3071, 2858,
            2798, 2604, 2465, 2376, 2212, 2112, 2016, 1798, 1576, 1503, 1404, 1324, 1247, 1175, 1121, 1053, 1031, 989,
            963, 921, 882, 823, 818, 757, 746, 717, 688, 679, 643, 616, 621, 570, 544, 545, 502, 504, 458, 439, 434,
            407, 368, 376, 353, 339, 309, 321, 310, 306, 297, 286, 280, 275, 271, 264, 265, 247, 255, 234, 241, 236,
            231, 230, 228, 212, 212, 207, 211, 181, 202, 191, 185, 182, 177, 168, 174, 163, 162, 158, 159, 153, 142,
            148, 142, 130, 140, 127, 126, 130, 101, 113, 96, 89, 85, 80, 64, 75, 75, 67, 74, 65, 65, 73, 61, 65, 71, 59,
            66, 60, 63, 56, 60, 64, 60, 49, 64, 53, 51, 61, 49, 57, 59, 40, 56, 49, 45, 57, 44, 48, 45, 48, 50, 38, 49,
            36, 47, 46, 32, 44, 37, 41, 43, 30, 42, 34, 36, 39, 30, 38, 28, 35, 31, 30, 39, 25, 32, 28, 31, 31, 27, 35,
            23, 31, 25, 29, 27, 27, 32, 20, 31, 22, 28, 23, 25, 30, 18, 28, 19, 27, 20, 24, 22, 21, 29, 12, 28, 15, 25,
            17, 22, 20, 19, 22, 14, 24, 12, 23, 13, 21, 12, 21, 13, 19, 13, 17, 18, 12, 20, 10, 18, 9, 16, 11, 15, 10,
            15, 11, 13, 11, 11, 12, 10, 12, 9, 12, 8, 11, 6, 11, 7, 9, 8, 8, 7, 8, 7, 7, 7, 5, 7, 5, 6, 5, 6, 3, 5, 4,
            4, 3, 4, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1});
    Raster bandA = new Raster("Band_A", null);
    bandA.setDataType(DataType.FLOAT32);
    bandA.setRasterType(RasterType.VIRTUAL);
    bandA.setNoDataValue(0.0);
    bandA.setNoDataValueUsed(false);
    bandA.setValidPixelExpression(null);
    bandA.setSize(new Dimension(2048, 1024));
    bandA.setPixels(new Pixel[]{new Pixel(new PixelPos(1497.5, 300.5), 0.9872199892997742),
        new Pixel(new PixelPos(1846.5, 4.5), -0.621017336845398),
        new Pixel(new PixelPos(1017.5, 875.5), -0.015127663500607014)});
    bandA.setMinimum(-1.0);
    bandA.setMaximum(1.0);
    bandA.setHistogramBins(
        new int[]{59460, 24746, 19062, 15924, 14086, 12790, 11766, 10922, 10170, 9914, 9226, 8984, 8368, 8372, 7698,
            7806, 7422, 7344, 6996, 6806, 6890, 6420, 6426, 6356, 6042, 6214, 5926, 5734, 5776, 5616, 5572, 5492, 5329,
            5247, 5484, 5126, 5029, 5069, 5124, 4832, 4852, 4847, 4881, 4539, 4723, 4732, 4527, 4513, 4590, 4474, 4416,
            4223, 4540, 4273, 4214, 4166, 4384, 4203, 3982, 4159, 4142, 4124, 3868, 4053, 3961, 4036, 3926, 3836, 3792,
            4049, 3746, 3739, 3822, 3804, 3808, 3706, 3559, 3775, 3658, 3722, 3565, 3595, 3556, 3699, 3535, 3534, 3490,
            3630, 3436, 3493, 3388, 3381, 3650, 3336, 3465, 3227, 3457, 3430, 3351, 3353, 3241, 3378, 3336, 3345, 3210,
            3271, 3252, 3196, 3368, 3198, 3175, 3164, 3243, 3222, 3250, 3184, 3046, 3140, 3303, 3074, 3198, 2917, 3230,
            3032, 3196, 3118, 3043, 3016, 3119, 3042, 3126, 2986, 3024, 2916, 3069, 3126, 3015, 2962, 2994, 2898, 3048,
            2986, 3093, 2794, 2991, 2956, 2986, 2958, 2976, 2852, 2907, 2938, 2985, 2918, 2856, 2894, 2844, 2880, 3073,
            2790, 2930, 2847, 2784, 2934, 2906, 2858, 2880, 2777, 2738, 2963, 2832, 2896, 2822, 2660, 2862, 2827, 2880,
            2888, 2763, 2728, 2844, 2734, 2834, 2896, 2771, 2780, 2711, 2790, 2758, 2872, 2794, 2806, 2600, 2841, 2730,
            2821, 2856, 2704, 2688, 2728, 2730, 2858, 2826, 2664, 2728, 2688, 2758, 2704, 2870, 2690, 2750, 2702, 2652,
            2806, 2738, 2830, 2688, 2632, 2652, 2844, 2762, 2698, 2766, 2570, 2720, 2748, 2714, 2820, 2770, 2600, 2734,
            2690, 2578, 3000, 2552, 2800, 2686, 2568, 2810, 2746, 2760, 2700, 2748, 2556, 2712, 2720, 2772, 2810, 2648,
            2644, 2616, 2820, 2718, 2734, 2760, 2610, 2744, 2654, 2688, 2784, 2712, 2616, 2616, 2738, 2582, 2846, 2630,
            2668, 2656, 2572, 2760, 2584, 2764, 2684, 2688, 2554, 2704, 2618, 2772, 2760, 2558, 2658, 2622, 2718, 2748,
            2658, 2682, 2550, 2756, 2598, 2636, 2838, 2658, 2684, 2604, 2600, 2740, 2792, 2564, 2826, 2508, 2754, 2728,
            2696, 2624, 2774, 2686, 2582, 2842, 2516, 2878, 2690, 2668, 2696, 2698, 2654, 2874, 2716, 2702, 2694, 2652,
            2770, 2836, 2664, 2808, 2680, 2662, 2812, 2808, 2672, 2876, 2686, 2676, 2796, 2686, 2968, 2772, 2720, 2784,
            2762, 2730, 3002, 2730, 2774, 2802, 2764, 2854, 2878, 2846, 2832, 2760, 2822, 2888, 2798, 2994, 2816, 2782,
            2948, 2804, 2948, 2972, 2768, 2938, 2860, 2978, 2944, 2878, 2922, 2886, 2930, 3078, 2950, 2956, 2908, 2900,
            3004, 3082, 2978, 3020, 2944, 3034, 3034, 3112, 3050, 2970, 3066, 3028, 3142, 3158, 3026, 3100, 3020, 3136,
            3200, 3204, 3068, 3082, 3244, 3254, 3208, 3188, 3114, 3254, 3204, 3208, 3246, 3114, 3248, 3140, 3384, 3218,
            3180, 3306, 3250, 3412, 3234, 3336, 3150, 3442, 3372, 3380, 3270, 3378, 3408, 3452, 3434, 3392, 3478, 3476,
            3466, 3380, 3566, 3454, 3684, 3458, 3568, 3544, 3646, 3616, 3724, 3552, 3774, 3726, 3728, 3610, 3816, 3810,
            3882, 3746, 3884, 3848, 4030, 3844, 3988, 3968, 4078, 4138, 3916, 4148, 4194, 4172, 4106, 4282, 4394, 4216,
            4446, 4430, 4466, 4488, 4516, 4578, 4750, 4518, 4892, 4844, 4760, 4920, 5110, 4910, 5150, 5314, 5156, 5326,
            5562, 5532, 5614, 5748, 5772, 6092, 6030, 6200, 6350, 6468, 6654, 6910, 6906, 7338, 7474, 7862, 7952, 8480,
            8678, 9244, 9814, 10170, 11212, 11956, 13200, 15118, 17968, 23158, 56082});
    Raster maskA = new Raster("Mask_A", "I am Mask A");
    maskA.setDataType(DataType.UINT8);
    maskA.setRasterType(RasterType.MASK);
    maskA.setNoDataValue(0.0);
    maskA.setNoDataValueUsed(false);
    maskA.setValidPixelExpression(null);
    maskA.setSize(new Dimension(2048, 1024));
    maskA.setPixels(new Pixel[]{new Pixel(new PixelPos(1497.5, 300.5), 255),
        new Pixel(new PixelPos(1846.5, 4.5), 0),
        new Pixel(new PixelPos(1017.5, 875.5), 0)});
    maskA.setMinimum(0.0);
    maskA.setMaximum(255.0);
    maskA.setHistogramBins(
        new int[]{1421504, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 675648, 0});

    assertArrayEquals(new Raster[]{gridA, gridB, bandA, maskA}, pc.getRasters());
    assertArrayEquals(new Vector[]{new Vector("pins", "", 0),
        new Vector("ground_control_points", "", 0)}, pc.getVectors());

  }

  private static float[] createRandomPoints(int n, Random random) {
    float[] pnts = new float[n];
    for (int i = 0; i < pnts.length; i++) {
      pnts[i] = (float) random.nextGaussian();
    }
    return pnts;
  }
}
