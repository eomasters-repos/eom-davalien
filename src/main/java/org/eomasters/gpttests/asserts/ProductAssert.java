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

public class ProductAssert extends AbstractAssert<ProductAssert, Product> {

  public ProductAssert(Product actual) {
    super(actual, ProductAssert.class);
    isNotNull();
  }

  public ProductAssert hasName(String name) {
    if (name != null && !actual.getName().equals(name)) {
      failWithMessage("Expected product name to be <%s> but was <%s>", name, actual.getName());
    }
    return this;
  }

  public ProductAssert hasProductType(String productType) {
    if (productType != null && !actual.getProductType().equals(productType)) {
      failWithMessage("Expected product type to be <%s> but was <%s>", productType, actual.getProductType());
    }
    return this;
  }

  public ProductAssert hasDescription(String description) {
    if (description != null && !actual.getDescription().equals(description)) {
      failWithMessage("Expected product description to be <%s> but was <%s>", description, actual.getDescription());
    }
    return this;
  }

  public ProductAssert hasSceneSize(Dimension sceneSize) {
    if (sceneSize != null && !actual.getSceneRasterSize().equals(sceneSize)) {
      failWithMessage("Expected product scene size to be <%s> but was <%s>", sceneSize, actual.getSceneRasterSize());
    }
    return this;
  }

  public ProductAssert hasStartTime(UTC startTime) {
    if (startTime != null && !actual.getStartTime().equals(startTime)) {
      failWithMessage("Expected product start time to be <%s> but was <%s>", startTime, actual.getStartTime());
    }
    return this;
  }

  public ProductAssert hasEndTime(UTC endTime) {
    if (endTime != null && !actual.getEndTime().equals(endTime)) {
      failWithMessage("Expected product end time to be <%s> but was <%s>", endTime, actual.getEndTime());
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
      assertThat(codings).allSatisfy(coding -> {
        SampleCoding sampleCoding = sampleCodingMap.get(coding.getName());
        if (sampleCoding == null) {
          failWithMessage("Expected product to have sample coding <%s> but was not found", coding.getName());
        } else {
          Sample[] samples = coding.getSamples();
          for (Sample sample : samples) {
            MetadataAttribute attribute = sampleCoding.getAttribute(sample.getName());
            if (attribute == null) {
              failWithMessage("Expected sample <%s> to be found in sample coding <%s> but was not found",
                  sample.getName(), coding.getName());
            } else {
              if (!Objects.equals(attribute.getDescription(), sample.getDescription())) {
                failWithMessage("Expected sample <%s> to have description <%s> but was <%s>", sample.getName(),
                    sample.getDescription(), attribute.getDescription());
              }
              if (attribute.getData().getElemInt() != sample.getSampleValue()) {
                failWithMessage("Expected sample <%s> to have value <%s> but was <%s>", sample.getName(),
                    sample.getSampleValue(), attribute.getData().getElemInt());
              }
            }
          }
        }
      });
    }
    return this;
  }

  public ProductAssert hasGeoLocations(GeoLocation[] geoLocations) {
    if (geoLocations != null) {
      assertThat(geoLocations).allSatisfy(geo -> {
        GeoPos actualGP = actual.getSceneGeoCoding().getGeoPos(geo.getPixelPos(), null);
        GeoPos expectedGP = geo.getGeoPos();
        assertThat(actualGP.lat).isEqualTo(expectedGP.lat, offset(geo.getEps()));
        assertThat(actualGP.lon).isEqualTo(expectedGP.lon, offset(geo.getEps()));
        PixelPos actualPP = actual.getSceneGeoCoding().getPixelPos(geo.getGeoPos(), null);
        PixelPos expectedPP = geo.getPixelPos();
        assertThat(actualPP.getX()).isEqualTo(expectedPP.getX(), offset(geo.getEps()));
        assertThat(actualPP.getY()).isEqualTo(expectedPP.getY(), offset(geo.getEps()));
      });
    }
    return this;
  }

  public ProductAssert hasRasters(List<Raster> expRasterList) {
    if (expRasterList == null) {
      return this;
    }
    if (actual.getRasterDataNodes().size() != expRasterList.size()) {
      failWithMessage("Expected product to have <%s> rasters but were <%s>", expRasterList.size(),
          actual.getRasterDataNodes().size());
    }

    List<RasterDataNode> rasterDataNodes = actual.getRasterDataNodes();
    assertThat(rasterDataNodes).allSatisfy(raster -> {
      Raster expectedRaster = expRasterList.stream()
                                           .filter(current -> current.getName().equals(raster.getName()))
                                           .findFirst()
                                           .orElseThrow(() -> new AssertionError(
                                               "Raster with name " + raster.getName() + " not found"));
      Pixel[] pixels = expectedRaster.getPixels();
      GeoLocation[] geoLocations = expectedRaster.getGeoLocations();
      assertThat(raster).hasName(expectedRaster.getName())
                        .hasDescription(expectedRaster.getDescription())
                        .hasSize(expectedRaster.getSize())
                        .hasDataType(expectedRaster.getDataType().getTypeValue())
                        .hasNoDataValue(expectedRaster.getNoDataValue())
                        .noDataValueIsUsed(expectedRaster.isNoDataValueUsed())
                        .hasValidPixelExpression(expectedRaster.getValidPixelExpression())
                        .rasterIsOfType(expectedRaster.getRasterType())
                        .hasPixels(pixels)
                        .hasGeoLocations(geoLocations);
    });
    return this;
  }

  public ProductAssert hasMetadata(Metadata[] metadata) {
    if (metadata != null) {
      for (Metadata elem : metadata) {
        assertThat(actual.getMetadataRoot()).has(elem);
      }
    }
    return this;
  }

  public ProductAssert hasVectors(Vector[] vectorData) {
    if (vectorData != null) {
      for (Vector vector : vectorData) {
        assertThat(actual.getVectorDataGroup()).has(vector);
      }
    }
    return this;
  }


}
