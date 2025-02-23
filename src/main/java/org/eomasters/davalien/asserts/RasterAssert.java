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

package org.eomasters.davalien.asserts;

import static org.eomasters.davalien.asserts.AssertionUtils.fuzzyEquals;

import com.bc.ceres.core.ProgressMonitor;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assert;
import org.eomasters.davalien.res.testdef.DataType;
import org.eomasters.davalien.res.testdef.GeoLocation;
import org.eomasters.davalien.res.testdef.Pixel;
import org.eomasters.davalien.res.testdef.RasterType;
import org.esa.snap.core.datamodel.GeoCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.RasterDataNode;
import org.esa.snap.core.datamodel.Stx;

/**
 * {@link Assert} implementation for a {@link RasterDataNode}.
 */
@SuppressWarnings("UnusedReturnValue")
public class RasterAssert extends AbstractAssert<RasterAssert, RasterDataNode> {

  /**
   * Creates an assert for the given {@link RasterDataNode }.
   *
   * @param actual the actual value to verify
   */
  public RasterAssert(RasterDataNode actual) {
    super(actual, RasterAssert.class);
    isNotNull();
  }

  /**
   * Checks if the actual RasterDataNode has the given name.
   *
   * @param name            the expected name of the RasterDataNode
   * @param indexOfExpected the index of the expected RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasName(String name, int indexOfExpected) {
    if (name != null && !name.equals(actual.getName())) {
      failWithMessage("Raster[%d]: Name should be [%s] but was [%s]",
          indexOfExpected, actual.getName(), name);
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given description.
   *
   * @param description the expected description of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasDescription(String description) {
    if (description != null && !description.equals(actual.getDescription())) {
      failWithMessage("Raster[%s]: Description should be [%s] but was <%s>",
          actual.getName(), description, actual.getDescription());
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given size.
   *
   * @param size the expected size of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasSize(Dimension size) {
    if (size != null && !size.equals(actual.getRasterSize())) {
      failWithMessage("Raster[%s]: Size should be [%.8f,%.8f] but was [%.8f,%.8f]",
          actual.getName(), size.getWidth(), size.getHeight(), actual.getRasterSize().getWidth(),
          actual.getRasterSize().getHeight());
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given data type.
   *
   * @param typeValue the expected data type of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasDataType(DataType typeValue) {
    DataType actualdataType = DataType.fromTypeValue(actual.getDataType());
    if (!actualdataType.equals(typeValue)) {
      failWithMessage("Raster[%s]: Data type should be [%s] but was [%s]",
          actual.getName(), typeValue, actualdataType);
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given no data value.
   *
   * @param noDataValue the expected no data value of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasNoDataValue(Double noDataValue) {
    if (noDataValue != null && Double.compare(actual.getNoDataValue(), noDataValue) != 0) {
      failWithMessage("Raster[%s]: No data value should be [%.8f] but was [%.8f]",
          actual.getName(), noDataValue, actual.getNoDataValue());
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given no data value used.
   *
   * @param noDataValueUsed the expected no data value used of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert noDataValueIsUsed(Boolean noDataValueUsed) {
    if (noDataValueUsed != null && actual.isNoDataValueUsed() != noDataValueUsed) {
      failWithMessage("Raster[%s]: No-data-value-used should be [%s] but was [%s]",
          actual.getName(), noDataValueUsed, actual.isNoDataValueUsed());
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given valid pixel expression.
   *
   * @param validPixelExpression the expected valid pixel expression of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasValidPixelExpression(String validPixelExpression) {
    if (validPixelExpression != null && !validPixelExpression.equals(actual.getValidPixelExpression())) {
      failWithMessage("Raster[%s]: Valid-pixel expression should be [%s] but was [%s]",
          actual.getName(), validPixelExpression,
          actual.getValidPixelExpression());
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given raster type.
   *
   * @param rasterType the expected raster type of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert rasterIsOfType(RasterType rasterType, int indexOfExpected) {
    RasterType actualRasterType = RasterType.get(actual);
    if (rasterType != null && actualRasterType != rasterType) {
      failWithMessage("Raster[%d]: Raster type of raster [%s] should be [%s] but was [%s]",
          indexOfExpected, actual.getName(), rasterType, actualRasterType);
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given pixels.
   *
   * @param pixels the expected pixels of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasPixels(Pixel[] pixels) {
    for (int i = 0; i < pixels.length; i++) {
      Pixel pixel = pixels[i];
      PixelPos location = pixel.getPosition();
      try {
        double[] actPixelValue = actual.readPixels((int) location.getX(), (int) location.getY(), 1, 1, new double[1]);
        if (!fuzzyEquals(actPixelValue[0], pixel.getValue(), pixel.getEps())) {
          failWithMessage(
              "Raster[%s] - Pixel[%d]: For pixel position [%.8f,%.8f] expected value [%.8f] but was [%.8f], "
                  + "with eps %e",
              actual.getName(), i, location.getX(), location.getY(), pixel.getValue(), actPixelValue[0],
              pixel.getEps());
        }
      } catch (IOException e) {
        throw new RuntimeException(
            String.format("Raster[%S] - Pixel[%d]: Failed to read pixel value", actual.getName(), i), e);
      }

    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given geolocation.
   *
   * @param geoLocations the expected geolocation of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert hasGeoLocations(GeoLocation[] geoLocations, int indexOfExpected) {
    GeoCoding geoCoding = actual.getGeoCoding();
    if (geoCoding == null) {
      failWithMessage("Raster [%s] has no geocoding", indexOfExpected, actual.getName());
    }
    if (geoLocations != null) {
      for (int i = 0; i < geoLocations.length; i++) {
        GeoLocation geoLocation = geoLocations[i];
        assert geoCoding != null;
        GeoPos actualGp = geoCoding.getGeoPos(geoLocation.getPixelPos(), null);
        GeoPos expectedGp = geoLocation.getGeoPos();
        if (!fuzzyEquals(expectedGp.getLat(), actualGp.getLat(), geoLocation.getFwdEps())
            || !fuzzyEquals(expectedGp.getLon(), actualGp.getLon(), geoLocation.getFwdEps())) {
          failWithMessage(
              "Raster[%s] - Geolocation[%d]: Geo position for pixel position [%.8f,%.8f] "
                  + "should be [%.8f,%.8f] but was [%.8f,%.8f], with fwdEps %e",
              actual.getName(), i, geoLocation.getPixelPos().x, geoLocation.getPixelPos().y,
              expectedGp.getLat(), expectedGp.getLon(),
              actualGp.getLat(), actualGp.getLon(),
              geoLocation.getFwdEps());
        }

        PixelPos actualPp = geoCoding.getPixelPos(geoLocation.getGeoPos(), null);
        PixelPos expectedPp = geoLocation.getPixelPos();
        if (!fuzzyEquals(expectedPp.getX(), actualPp.getX(), geoLocation.getInvEps())
            || !fuzzyEquals(expectedPp.getX(), actualPp.getX(), geoLocation.getInvEps())) {
          failWithMessage(
              "Raster[%s] - Geolocation[%d]: Pixel position for geo position [%.8f,%.8f] "
                  + "should be [%.8f,%.8f] but was [%.8f,%.8f], with invEps %e",
              actual.getName(), i, geoLocation.getGeoPos().lat, geoLocation.getGeoPos().lon,
              expectedPp.getX(), expectedPp.getY(),
              actualPp.getX(), actualPp.getY(),
              geoLocation.getInvEps());
        }
      }
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given minimum value.
   *
   * @param minimum the expected minimum of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert rasterHasMinimmum(Double minimum) {
    if (minimum != null) {
      Stx stx = actual.getStx(true, ProgressMonitor.NULL);
      if (!fuzzyEquals(stx.getMinimum(), minimum, 1e-8)) {
        failWithMessage("Raster[%s]: Minimum should be [%.8f] but was [%.8f]",
            actual.getName(), minimum, stx.getMinimum());
      }
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given maximum value.
   *
   * @param maximum the expected maximum of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert rasterHasMaximum(Double maximum) {
    if (maximum != null) {
      Stx stx = actual.getStx(true, ProgressMonitor.NULL);
      if (!fuzzyEquals(stx.getMaximum(), maximum, 1e-8)) {
        failWithMessage("Raster[%s]: Maximum should be [%.8f] but was [%.8f]",
            actual.getName(), maximum, stx.getMaximum());
      }
    }
    return this;
  }

  /**
   * Checks if the actual RasterDataNode has the given histogram bins.
   *
   * @param expectedBins the expected histogram bins of the RasterDataNode
   * @return the current {@link RasterAssert}
   */
  public RasterAssert rasterHasHistogram(int[] expectedBins) {
    if (expectedBins != null) {
      Stx stx = actual.getStx(true, ProgressMonitor.NULL);
      int[] actualBins = stx.getHistogram().getBins(0);
      if (!Arrays.equals(expectedBins, actualBins)) {
        failWithMessage("Raster[%s]: Histogram bins are not equal. Expected: %s, actual: %s",
            actual.getName(), Arrays.toString(expectedBins), Arrays.toString(actualBins));
      }
    }
    return this;
  }

}
