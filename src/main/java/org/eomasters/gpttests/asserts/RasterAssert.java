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

package org.eomasters.gpttests.asserts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import java.awt.Dimension;
import org.assertj.core.api.AbstractAssert;
import org.eomasters.gpttests.res.testdef.GeoLocation;
import org.eomasters.gpttests.res.testdef.Pixel;
import org.eomasters.gpttests.res.testdef.RasterType;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.RasterDataNode;

public class RasterAssert extends AbstractAssert<RasterAssert, RasterDataNode> {

  public RasterAssert(RasterDataNode actual) {
    super(actual, RasterAssert.class);
    isNotNull();
  }

  public RasterAssert hasName(String name) {
    if (name != null && !actual.getName().equals(name)) {
      failWithMessage("Expected raster name to be <%s> but was <%s>", name, actual.getName());
    }
    return this;
  }

  public RasterAssert hasDescription(String description) {
    if (description != null && !actual.getDescription().equals(description)) {
      failWithMessage("Expected raster description to be <%s> but was <%s>", description, actual.getDescription());
    }
    return this;
  }

  public RasterAssert hasSize(Dimension size) {
    if (size != null && !actual.getRasterSize().equals(size)) {
      failWithMessage("Expected raster size to be <%s> but was <%s>", size, actual.getRasterSize());
    }
    return this;
  }

  public RasterAssert hasDataType(int typeValue) {
    if (typeValue != -1 && actual.getDataType() != typeValue) {
      failWithMessage("Expected raster data type to be <%s> but was <%s>", typeValue, actual.getDataType());
    }
    return this;
  }

  public RasterAssert hasNoDataValue(Double noDataValue) {
    if (noDataValue != null && Double.compare(actual.getNoDataValue(), noDataValue) != 0) {
      failWithMessage("Expected raster no data value to be <%s> but was <%s>", noDataValue, actual.getNoDataValue());
    }
    return this;
  }

  public RasterAssert noDataValueIsUsed(Boolean noDataValueUsed) {
    if (noDataValueUsed != null && actual.isNoDataValueUsed() != noDataValueUsed) {
      failWithMessage("Expected raster no data value used to be <%s> but was <%s>", noDataValueUsed,
          actual.isNoDataValueUsed());
    }
    return this;
  }

  public RasterAssert hasValidPixelExpression(String validPixelExpression) {
    if (validPixelExpression != null && !actual.getValidPixelExpression().equals(validPixelExpression)) {
      failWithMessage("Expected raster valid pixel expression to be <%s> but was <%s>", validPixelExpression,
          actual.getValidPixelExpression());
    }
    return this;
  }

  public RasterAssert rasterIsOfType(RasterType rasterType) {
    if (rasterType != null && RasterType.get(actual) != rasterType) {
      failWithMessage("Expected raster type to be <%s> but was <%s>", rasterType, RasterType.get(actual));
    }
    return this;
  }

  public RasterAssert hasPixels(Pixel[] pixels) {
    assertThat(pixels).allSatisfy(pixel -> {
      PixelPos location = pixel.getPosition();
      double[] pixelValue = actual.readPixels((int) location.getX(), (int) location.getY(), 1, 1, new double[1]);
      assertThat(pixelValue[0]).isEqualTo(pixel.getValue(), offset(pixel.getEps()));
    });
    return this;
  }

  public RasterAssert hasGeoLocations(GeoLocation[] geoLocations) {
    if (geoLocations != null) {
      assertThat(geoLocations).allSatisfy(geo -> {
        GeoPos actualGP = actual.getGeoCoding().getGeoPos(geo.getPixelPos(), null);
        GeoPos expectedGP = geo.getGeoPos();
        assertThat(actualGP.lat).isEqualTo(expectedGP.lat, offset(geo.getEps()));
        assertThat(actualGP.lon).isEqualTo(expectedGP.lon, offset(geo.getEps()));
        PixelPos actualPP = actual.getGeoCoding().getPixelPos(geo.getGeoPos(), null);
        PixelPos expectedPP = geo.getPixelPos();
        assertThat(actualPP.getX()).isEqualTo(expectedPP.getX(), offset(geo.getEps()));
        assertThat(actualPP.getY()).isEqualTo(expectedPP.getY(), offset(geo.getEps()));
      });
    }
    return this;
  }
}
