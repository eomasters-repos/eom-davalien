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

import static org.eomasters.davalien.asserts.ProductAssertions.assertThat;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assert;
import org.eomasters.davalien.res.testdef.Coding;
import org.eomasters.davalien.res.testdef.Coding.Sample;
import org.eomasters.davalien.res.testdef.GeoLocation;
import org.eomasters.davalien.res.testdef.Metadata;
import org.eomasters.davalien.res.testdef.Pixel;
import org.eomasters.davalien.res.testdef.Raster;
import org.eomasters.davalien.res.testdef.Vector;
import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.GeoCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.IndexCoding;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData.UTC;
import org.esa.snap.core.datamodel.RasterDataNode;
import org.esa.snap.core.datamodel.SampleCoding;


/**
 * {@link Assert} implementation for a {@link Product}.
 */
@SuppressWarnings("UnusedReturnValue")
public class ProductAssert extends AbstractAssert<ProductAssert, Product> {

  /**
   * Creates an assert for the given {@link Product}.
   *
   * @param actual the {@link Product} to be verified
   */
  public ProductAssert(Product actual) {
    super(actual, ProductAssert.class);
    isNotNull();
  }

  /**
   * Checks if the actual {@link Product} has the given name.
   *
   * @param name the name to be checked
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasName(String name) {
    if (name != null && !name.equals(actual.getName())) {
      failWithMessage("Product name expected to be [%s] but was [%s]", name, actual.getName());
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given product type.
   *
   * @param productType the product type
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasProductType(String productType) {
    if (productType != null && !productType.equals(actual.getProductType())) {
      failWithMessage("Product type expected to be [%s] but was [%s]", productType, actual.getProductType());
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given description.
   *
   * @param description the description
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasDescription(String description) {
    if (description != null && !description.equals(actual.getDescription())) {
      failWithMessage("Description expected to be [%s] but was [%s]", description, actual.getDescription());
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given scene size.
   *
   * @param sceneSize the scene size
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasSceneSize(Dimension sceneSize) {
    if (sceneSize != null && !sceneSize.equals(actual.getSceneRasterSize())) {
      failWithMessage("Scene size expected to be [%.8f,%.8f] but was [%.8f,%.8f]",
          sceneSize.getWidth(), sceneSize.getHeight(), actual.getSceneRasterSize().getWidth(),
          actual.getSceneRasterSize().getHeight());
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given start time.
   *
   * @param startTime the start time
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasStartTime(UTC startTime) {
    if (startTime != null) {
      UTC startTimeUtc = actual.getStartTime();
      String actStartString = null;
      if (startTimeUtc != null) {
        actStartString = startTimeUtc.format();
      } else {
        failWithMessage("Start time expected to be [%s] but was [null]", startTime.format());
      }
      String expStartString = startTime.format();
      if (!expStartString.equals(actStartString)) {
        failWithMessage("Start time expected to be [%s] but was [%s]", expStartString, actStartString);
      }
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given end time.
   *
   * @param endTime the end time
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasEndTime(UTC endTime) {
    if (endTime != null) {
      UTC endTimeUtc = actual.getEndTime();
      String actEndString = null;
      if (endTimeUtc != null) {
        actEndString = endTimeUtc.format();
      } else {
        failWithMessage("End time expected to be [%s] but was [null]", endTime.format());
      }
      String expEndString = endTime.format();
      if (!expEndString.equals(actEndString)) {
        failWithMessage("End time expected to be [%s] but was [%s]", expEndString, actEndString);
      }
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given sample codings.
   *
   * @param codings the sample codings
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasSampleCodings(Coding[] codings) {
    if (codings != null) {
      List<SampleCoding> sampleCodingList = new ArrayList<>();
      sampleCodingList.addAll(List.of(actual.getIndexCodingGroup().toArray(new IndexCoding[0])));
      sampleCodingList.addAll(List.of(actual.getFlagCodingGroup().toArray(new FlagCoding[0])));
      Map<String, SampleCoding> sampleCodingMap = sampleCodingList.stream()
                                                                  .collect(Collectors.toMap(SampleCoding::getName,
                                                                      Function.identity()));

      for (int i = 0; i < codings.length; i++) {
        Coding coding = codings[i];
        SampleCoding sampleCoding = sampleCodingMap.get(coding.getName());
        if (sampleCoding == null) {
          failWithMessage("SampleCoding[%d]: Expected sample-coding [%s] was not found",
              i, coding.getName());
        } else {
          Sample[] samples = coding.getSamples();
          for (Sample sample : samples) {
            MetadataAttribute attribute = sampleCoding.getAttribute(sample.getName());
            if (attribute == null) {
              failWithMessage("SampleCoding[%d]: Expected sample [%s] not found in sample-coding [%s]",
                  i, sample.getName(), coding.getName());
            } else {
              if (!Objects.equals(attribute.getDescription(), sample.getDescription())) {
                failWithMessage("SampleCoding[%d]: Description of sample [%s] should be [%s] but was [%s]",
                    i, sample.getName(), sample.getDescription(), attribute.getDescription());
              }
              if (attribute.getData().getElemInt() != sample.getValue()) {
                failWithMessage("SampleCoding[%d]: Value of sample [%s]should be [%d] but was [%d]",
                    i, sample.getName(), sample.getValue(), attribute.getData().getElemInt());
              }
            }
          }
        }
      }
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given geolocations.
   *
   * @param expected the geolocations
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasGeoLocations(GeoLocation[] expected) {
    GeoCoding sceneGeoCoding = actual.getSceneGeoCoding();
    if (sceneGeoCoding == null) {
      failWithMessage("Geolocations: No scene geocoding found");
    }
    if (expected != null) {
      for (int i = 0; i < expected.length; i++) {
        GeoLocation geoLocation = expected[i];
        assert sceneGeoCoding != null;
        GeoPos actualGp = sceneGeoCoding.getGeoPos(geoLocation.getPixelPos(), null);
        GeoPos expectedGp = geoLocation.getGeoPos();
        if (!AssertionUtils.fuzzyEquals(expectedGp.getLat(), actualGp.getLat(), geoLocation.getFwdEps())
            || !AssertionUtils.fuzzyEquals(expectedGp.getLon(), actualGp.getLon(), geoLocation.getFwdEps())) {
          failWithMessage(
              "Product Geolocation[%d]: For pixel position [%.8f,%.8f] expected geo position [%.8f,%.8f] "
                  + "but was [%.8f,%.8f], with fwdEps %e",
              i, geoLocation.getPixelPos().x, geoLocation.getPixelPos().y,
              expectedGp.getLat(), expectedGp.getLon(),
              actualGp.getLat(), actualGp.getLon(),
              geoLocation.getFwdEps());
        }

        PixelPos actualPp = sceneGeoCoding.getPixelPos(geoLocation.getGeoPos(), null);
        PixelPos expectedPp = geoLocation.getPixelPos();
        if (!AssertionUtils.fuzzyEquals(expectedPp.getX(), actualPp.getX(), geoLocation.getInvEps())
            || !AssertionUtils.fuzzyEquals(expectedPp.getX(), actualPp.getX(), geoLocation.getInvEps())) {
          failWithMessage(
              "Product Geolocation[%d]: For geo position [%.8f,%.8f] expected pixel position [%.8f,%.8f] "
                  + "but was [%.8f,%.8f], with invEps %e",
              i, geoLocation.getGeoPos().lat, geoLocation.getGeoPos().lon,
              expectedPp.getX(), expectedPp.getY(),
              actualPp.getX(), actualPp.getY(),
              geoLocation.getInvEps());
        }
      }
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given rasters.
   *
   * @param expRasters the expected rasters
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasRasters(Raster[] expRasters) {
    if (expRasters == null) {
      return this;
    }
    if (actual.getRasterDataNodes().size() < expRasters.length) {
      failWithMessage("Expected product to have at least [%d] rasters but were only [%s]",
          expRasters.length, actual.getRasterDataNodes().size());
    }

    for (int i = 0; i < expRasters.length; i++) {
      Raster expRaster = expRasters[i];
      RasterDataNode actRaster = actual.getRasterDataNode(expRaster.getName());
      if (actRaster == null) {
        failWithMessage("Raster[%d]: No raster found with name [%s] ", i, expRaster.getName());
      }
      Pixel[] pixels = expRaster.getPixels();
      GeoLocation[] geoLocations = expRaster.getGeoLocations();
      assertThat(actRaster).hasName(expRaster.getName(), i)
                           .hasDescription(expRaster.getDescription())
                           .hasSize(expRaster.getSize())
                           .hasDataType(expRaster.getDataType())
                           .hasNoDataValue(expRaster.getNoDataValue())
                           .noDataValueIsUsed(expRaster.isNoDataValueUsed())
                           .hasValidPixelExpression(expRaster.getValidPixelExpression())
                           .rasterIsOfType(expRaster.getRasterType(), i)
                           .rasterHasMinimmum(expRaster.getMinimum())
                           .rasterHasMaximum(expRaster.getMaximum())
                           .rasterHasHistogram(expRaster.getHistogramBins())
                           .hasPixels(pixels)
                           .hasGeoLocations(geoLocations, i);
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given metadata.
   *
   * @param metadata the metadata
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasMetadata(Metadata[] metadata) {
    if (metadata != null) {
      for (int i = 0; i < metadata.length; i++) {
        Metadata elem = metadata[i];
        assertThat(actual.getMetadataRoot()).has(elem, i);
      }
    }
    return this;
  }

  /**
   * Checks if the actual {@link Product} has the given vectors.
   *
   * @param vectorData the vectors
   * @return the current {@link ProductAssert}
   */
  public ProductAssert hasVectors(Vector[] vectorData) {
    if (vectorData != null) {
      for (int i = 0; i < vectorData.length; i++) {
        Vector vector = vectorData[i];
        assertThat(actual.getVectorDataGroup()).has(vector, i);
      }
    }
    return this;
  }

}
