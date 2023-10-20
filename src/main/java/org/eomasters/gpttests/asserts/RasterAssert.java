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

import static org.eomasters.gpttests.asserts.ProductAssert.fuzzyEquals;

import java.awt.Dimension;
import java.io.IOException;
import org.assertj.core.api.AbstractAssert;
import org.eomasters.gpttests.res.testdef.DataType;
import org.eomasters.gpttests.res.testdef.GeoLocation;
import org.eomasters.gpttests.res.testdef.Pixel;
import org.eomasters.gpttests.res.testdef.RasterType;
import org.esa.snap.core.datamodel.GeoCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.RasterDataNode;

@SuppressWarnings("UnusedReturnValue")
public class RasterAssert extends AbstractAssert<RasterAssert, RasterDataNode> {

  private final int index;

  public RasterAssert(RasterDataNode actual, int index) {
    super(actual, RasterAssert.class);
    this.index = index;
    isNotNull();
  }

  public RasterAssert hasName(String name) {
    if (name != null && !name.equals(actual.getName())) {
      failWithMessage("Raster[%d]: Raster name should be [%s] but was [%s]",
          index, name, actual.getName());
    }
    return this;
  }

  public RasterAssert hasDescription(String description) {
    if (description != null && !description.equals(actual.getDescription())) {
      failWithMessage("Raster[%d]: Description of raster [%s] should be [%s] but was <%s>",
          index, actual.getName(), description, actual.getDescription());
    }
    return this;
  }

  public RasterAssert hasSize(Dimension size) {
    if (size != null && !size.equals(actual.getRasterSize())) {
      failWithMessage("Raster[%d]: Size of raster [%s] should be [%.8f,%.8f] but was [%.8f,%.8f]",
          index, actual.getName(), size.getWidth(), size.getHeight(), actual.getRasterSize().getWidth(),
          actual.getRasterSize().getHeight());
    }
    return this;
  }

  public RasterAssert hasDataType(DataType typeValue) {
    DataType actualdataType = DataType.fromTypeValue(actual.getDataType());
    if (!actualdataType.equals(typeValue)) {
      failWithMessage("Raster[%d]: Data type of raster [%s] should be [%s] but was [%s]",
          index, actual.getName(), typeValue, actualdataType);
    }
    return this;
  }

  public RasterAssert hasNoDataValue(Double noDataValue) {
    if (noDataValue != null && Double.compare(actual.getNoDataValue(), noDataValue) != 0) {
      failWithMessage("Raster[%d]: No data value of raster [%s] should be [%.8f] but was [%.8f]",
          index, actual.getName(), noDataValue, actual.getNoDataValue());
    }
    return this;
  }

  public RasterAssert noDataValueIsUsed(Boolean noDataValueUsed) {
    if (noDataValueUsed != null && actual.isNoDataValueUsed() != noDataValueUsed) {
      failWithMessage("Raster[%d]: No-data-value-used of raster [%s] should be [%s] but was [%s]",
          index, actual.getName(), noDataValueUsed, actual.isNoDataValueUsed());
    }
    return this;
  }

  public RasterAssert hasValidPixelExpression(String validPixelExpression) {
    if (validPixelExpression != null && !validPixelExpression.equals(actual.getValidPixelExpression())) {
      failWithMessage("Raster[%d]: Valid-pixel expression of raster [%s] should be [%s] but was [%s]",
          index, actual.getName(), validPixelExpression,
          actual.getValidPixelExpression());
    }
    return this;
  }

  public RasterAssert rasterIsOfType(RasterType rasterType) {
    RasterType actualRasterType = RasterType.get(actual);
    if (rasterType != null && actualRasterType != rasterType) {
      failWithMessage("Raster[%d]: Raster type of raster [%s] should be [%s] but was [%s]",
          index, actual.getName(), rasterType, actualRasterType);
    }
    return this;
  }

  public RasterAssert hasPixels(Pixel[] pixels) {
    for (int i = 0; i < pixels.length; i++) {
      Pixel pixel = pixels[i];
      PixelPos location = pixel.getPosition();
      try {
        double[] actPixelValue = actual.readPixels((int) location.getX(), (int) location.getY(), 1, 1, new double[1]);
        if (!fuzzyEquals(actPixelValue[0], pixel.getValue(), pixel.getEps())) {
          failWithMessage(
              "Raster[%d] - Pixel[%d]: For pixel position [%.8f,%.8f] expected value [%.8f] but was [%.8f], with eps %e",
              index, i, location.getX(), location.getY(), pixel.getValue(), actPixelValue[0], pixel.getEps());
        }
      } catch (IOException e) {
        throw new RuntimeException(String.format("Raster[%d] - Pixel[%d]: Failed to read pixel value", index, i), e);
      }

    }
    return this;
  }

  public RasterAssert hasGeoLocations(GeoLocation[] geoLocations) {
    GeoCoding geoCoding = actual.getGeoCoding();
    if (geoCoding == null) {
      failWithMessage("Raster[%d]: Raster [%s] has no geocoding", index, actual.getName());
    }
    if (geoLocations != null) {
      for (int i = 0; i < geoLocations.length; i++) {
        GeoLocation geoLocation = geoLocations[i];
        assert geoCoding != null;
        GeoPos actualGP = geoCoding.getGeoPos(geoLocation.getPixelPos(), null);
        GeoPos expectedGP = geoLocation.getGeoPos();
        if (!fuzzyEquals(expectedGP.getLat(), actualGP.getLat(), geoLocation.getFwdEps()) ||
            !fuzzyEquals(expectedGP.getLon(), actualGP.getLon(), geoLocation.getFwdEps())) {
          failWithMessage(
              "Raster[%d] - Geolocation[%d]: Geo position for pixel position [%.8f,%.8f] should be [%.8f,%.8f] but was [%.8f,%.8f], with fwdEps %e",
              index, i, geoLocation.getPixelPos().x, geoLocation.getPixelPos().y,
              expectedGP.getLat(), expectedGP.getLon(),
              actualGP.getLat(), actualGP.getLon(),
              geoLocation.getFwdEps());
        }

        PixelPos actualPP = geoCoding.getPixelPos(geoLocation.getGeoPos(), null);
        PixelPos expectedPP = geoLocation.getPixelPos();
        if (!fuzzyEquals(expectedPP.getX(), actualPP.getX(), geoLocation.getInvEps()) ||
            !fuzzyEquals(expectedPP.getX(), actualPP.getX(), geoLocation.getInvEps())) {
          failWithMessage(
              "Raster[%d] - Geolocation[%d]: Pixel position for geo position [%.8f,%.8f] should be [%.8f,%.8f] but was [%.8f,%.8f], with invEps %e",
              index, i, geoLocation.getGeoPos().lat, geoLocation.getGeoPos().lon,
              expectedPP.getX(), expectedPP.getY(),
              actualPP.getX(), actualPP.getY(),
              geoLocation.getInvEps());
        }
      }
    }
    return this;
  }
}
