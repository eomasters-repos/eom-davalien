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

import static org.eomasters.gpttests.asserts.ProductAssertions.assertThat;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;
import org.eomasters.gpttests.res.testdef.Coding;
import org.eomasters.gpttests.res.testdef.Coding.Sample;
import org.eomasters.gpttests.res.testdef.GeoLocation;
import org.eomasters.gpttests.res.testdef.Metadata;
import org.eomasters.gpttests.res.testdef.Pixel;
import org.eomasters.gpttests.res.testdef.Raster;
import org.eomasters.gpttests.res.testdef.Vector;
import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.IndexCoding;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData.UTC;
import org.esa.snap.core.datamodel.RasterDataNode;
import org.esa.snap.core.datamodel.SampleCoding;

@SuppressWarnings("UnusedReturnValue")
public class ProductAssert extends AbstractAssert<ProductAssert, Product> {

  public ProductAssert(Product actual) {
    super(actual, ProductAssert.class);
    isNotNull();
  }

  public ProductAssert hasName(String name) {
    if (name != null && !actual.getName().equals(name)) {
      failWithMessage("Product name expected to be [%s] but was [%s]", name, actual.getName());
    }
    return this;
  }

  public ProductAssert hasProductType(String productType) {
    if (productType != null && !actual.getProductType().equals(productType)) {
      failWithMessage("Product type expected to be [%s] but was [%s]", productType, actual.getProductType());
    }
    return this;
  }

  public ProductAssert hasDescription(String description) {
    if (description != null && !actual.getDescription().equals(description)) {
      failWithMessage("Description expected to be [%s] but was [%s]", description, actual.getDescription());
    }
    return this;
  }

  public ProductAssert hasSceneSize(Dimension sceneSize) {
    if (sceneSize != null && !actual.getSceneRasterSize().equals(sceneSize)) {
      failWithMessage("Scene size expected to be [%.8f,%.8f] but was [%.8f,%.8f]",
          sceneSize.getWidth(), sceneSize.getHeight(), actual.getSceneRasterSize().getWidth(),
          actual.getSceneRasterSize().getHeight());
    }
    return this;
  }

  public ProductAssert hasStartTime(UTC startTime) {
    if (startTime != null) {
      String actStartString = actual.getStartTime().format();
      String expStartString = startTime.format();
      if (!actStartString.equals(expStartString)) {
        failWithMessage("Start time expected to be [%s] but was [%s]", expStartString, actStartString);
      }
    }
    return this;
  }

  public ProductAssert hasEndTime(UTC endTime) {
    if (endTime != null) {
      String actEndString = actual.getEndTime().format();
      String expEndString = endTime.format();
      if (!actEndString.equals(expEndString)) {
        failWithMessage("End time expected to be [%s] but was [%s]", expEndString, actEndString);
      }
    }
    return this;
  }

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

  public ProductAssert hasGeoLocations(GeoLocation[] expected) {
    if (expected != null) {
      for (int i = 0; i < expected.length; i++) {
        GeoLocation geoLocation = expected[i];
        GeoPos actualGP = actual.getSceneGeoCoding().getGeoPos(geoLocation.getPixelPos(), null);
        GeoPos expectedGP = geoLocation.getGeoPos();
        if (!fuzzyEquals(expectedGP.getLat(), actualGP.getLat(), geoLocation.getEps()) ||
            !fuzzyEquals(expectedGP.getLon(), actualGP.getLon(), geoLocation.getEps())) {
          failWithMessage(
              "Geolocation[%d]: For pixel position [%.8f,%.8f] expected geo position [%.8f,%.8f] but was [%.8f,%.8f], with eps %e",
              i, geoLocation.getPixelPos().x, geoLocation.getPixelPos().y,
              expectedGP.getLat(), expectedGP.getLon(),
              actualGP.getLat(), actualGP.getLon(),
              geoLocation.getEps());
        }

        PixelPos actualPP = actual.getSceneGeoCoding().getPixelPos(geoLocation.getGeoPos(), null);
        PixelPos expectedPP = geoLocation.getPixelPos();
        if (!fuzzyEquals(expectedPP.getX(), actualPP.getX(), geoLocation.getEps()) ||
            !fuzzyEquals(expectedPP.getX(), actualPP.getX(), geoLocation.getEps())) {
          failWithMessage(
              "Geolocation[%d]: For geo position [%.8f,%.8f] expected pixel position [%.8f,%.8f] but was [%.8f,%.8f], with eps %e",
              i, geoLocation.getGeoPos().lat, geoLocation.getGeoPos().lon,
              expectedPP.getX(), expectedPP.getY(),
              actualPP.getX(), actualPP.getY(),
              geoLocation.getEps());
        }
      }
    }
    return this;
  }

  public ProductAssert hasRasters(Raster[] expRasters) {
    if (expRasters == null) {
      return this;
    }
    if (actual.getRasterDataNodes().size() != expRasters.length) {
      failWithMessage("Expected product to have [%d] rasters but were [%s]",
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
      assertThat(actRaster, i).hasName(expRaster.getName())
                              .hasDescription(expRaster.getDescription())
                              .hasSize(expRaster.getSize())
                              .hasDataType(expRaster.getDataType())
                              .hasNoDataValue(expRaster.getNoDataValue())
                              .noDataValueIsUsed(expRaster.isNoDataValueUsed())
                              .hasValidPixelExpression(expRaster.getValidPixelExpression())
                              .rasterIsOfType(expRaster.getRasterType())
                              .hasPixels(pixels)
                              .hasGeoLocations(geoLocations);

    }
    return this;
  }

  public ProductAssert hasMetadata(Metadata[] metadata) {
    if (metadata != null) {
      for (int i = 0; i < metadata.length; i++) {
        Metadata elem = metadata[i];
        assertThat(actual.getMetadataRoot(), i).has(elem);
      }
    }
    return this;
  }

  public ProductAssert hasVectors(Vector[] vectorData) {
    if (vectorData != null) {
      for (int i = 0; i < vectorData.length; i++) {
        Vector vector = vectorData[i];
        assertThat(actual.getVectorDataGroup(), i).has(vector);
      }
    }
    return this;
  }

  public static boolean fuzzyEquals(double exp, double act, double eps) {
    return Math.abs(exp - act) < eps;
  }

}
