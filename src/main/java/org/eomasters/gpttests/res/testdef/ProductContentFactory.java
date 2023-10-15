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

package org.eomasters.gpttests.res.testdef;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.eomasters.gpttests.res.testdef.Coding.Sample;
import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.GeoCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.IndexCoding;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.MetadataElement;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductNodeGroup;
import org.esa.snap.core.datamodel.RasterDataNode;
import org.esa.snap.core.datamodel.SampleCoding;
import org.esa.snap.core.datamodel.VectorDataNode;

public class ProductContentFactory {

  public static ProductContent create(Product product, Random random) throws IOException {
    ProductContent pc = new ProductContent();
    pc.setName(product.getName());
    pc.setDescription(product.getDescription());
    pc.setProductType(product.getProductType());
    pc.setSceneSize(product.getSceneRasterSize());
    pc.setStartTime(product.getStartTime());
    pc.setEndTime(product.getEndTime());
    pc.setGeoLocations(create(product.getSceneGeoCoding(), product.getSceneRasterSize(), random));
    pc.setRasters(create(product.getRasterDataNodes(), random));
    pc.setVectorData(create(product.getVectorDataGroup()));
    pc.setMetadata(create(product.getMetadataRoot(), random));
    pc.setSampleCoding(create(product.getFlagCodingGroup(), product.getIndexCodingGroup()));
    return pc;
  }

  private static Coding[] create(ProductNodeGroup<FlagCoding> flagCodingGroup,
      ProductNodeGroup<IndexCoding> indexCodingGroup) {
    List<SampleCoding> sampleCodingList = new ArrayList<>();
    sampleCodingList.addAll(List.of(flagCodingGroup.toArray(new FlagCoding[0])));
    sampleCodingList.addAll(List.of(indexCodingGroup.toArray(new IndexCoding[0])));
    Coding[] codings = null;
    if (!sampleCodingList.isEmpty()) {
      codings = new Coding[sampleCodingList.size()];
      for (int i = 0; i < sampleCodingList.size(); i++) {
        SampleCoding sampleCoding = sampleCodingList.get(i);
        Sample[] samples = new Sample[sampleCoding.getSampleCount()];
        for (int j = 0; j < samples.length; j++) {
          String description = sampleCoding.getAttribute(sampleCoding.getSampleName(j)).getDescription();
          samples[j] = new Sample(sampleCoding.getSampleName(j), description, sampleCoding.getSampleValue(j));
        }
        codings[i] = new Coding(sampleCoding.getName(), samples);
      }
    }
    return codings;
  }

  private static Metadata[] create(MetadataElement metadataRoot, Random random) {
    Metadata[] metadata = new Metadata[2];
    if (metadataRoot.getNumElements() > 0 || metadataRoot.getNumAttributes() > 0) {
      for (int i = 0; i < metadata.length; i++) {
        metadata[i] = findRandomMetadata(metadataRoot, random);
      }
    }
    return metadata;
  }

  private static Metadata findRandomMetadata(MetadataElement element, Random random) {
    if (element.getNumElements() > 0) {
      findRandomMetadata(element.getElementAt(random.nextInt(element.getNumElements())), random);
    } else if (element.getNumAttributes() > 0) {
      MetadataAttribute attributeAt = element.getAttributeAt(random.nextInt(element.getNumAttributes()));
      String path = createPath(element, attributeAt);
      return new Metadata(path, attributeAt.getData().toString());
    }
    return null;
  }

  private static String createPath(MetadataElement element, MetadataAttribute attributeAt) {
    StringBuilder stringBuilder = new StringBuilder(attributeAt.getName());
    do {
      stringBuilder.insert(0, element.getName() + "/");
      element = element.getParentElement();
    } while (!element.getName().equals("metadata"));
    return stringBuilder.toString();
  }

  private static Vector[] create(ProductNodeGroup<VectorDataNode> vectorDataGroup) throws IOException {
    Vector[] vectors = null;
    if (vectorDataGroup.getNodeCount() > 0) {
      vectors = new Vector[vectorDataGroup.getNodeCount()];
      for (int i = 0; i < vectors.length; i++) {
        VectorDataNode vectorDataNode = vectorDataGroup.get(i);
        vectors[i] = new Vector(vectorDataNode.getName(), vectorDataNode.getDescription(),
            vectorDataNode.getFeatureCollection().getCount());
      }
    }
    return vectors;
  }

  private static Raster[] create(List<RasterDataNode> rasterDataNodes, Random random) throws IOException {
    Raster[] rasters = new Raster[rasterDataNodes.size()];
    for (int i = 0; i < rasterDataNodes.size(); i++) {
      RasterDataNode rasterDataNode = rasterDataNodes.get(i);
      rasters[i] = create(rasterDataNode, random);
    }
    return rasters;
  }

  private static Raster create(RasterDataNode rdn, Random random) throws IOException {
    Raster raster = new Raster();
    raster.setName(rdn.getName());
    raster.setDescription(rdn.getDescription());
    raster.setSize(rdn.getRasterSize());
    raster.setDataType(DataType.fromTypeValue(rdn.getDataType()));
    raster.setRasterType(RasterType.get(rdn));
    raster.setNoDataValue(rdn.getNoDataValue());
    raster.setNoDataValueUsed(rdn.isNoDataValueUsed());
    raster.setValidPixelExpression(rdn.getValidPixelExpression());
    raster.setPixels(createPixels(rdn, random));
    GeoCoding geoCoding = rdn.getGeoCoding();
    // Only create geoLocations if the geocoding of the raster is not the same  as the one of the product
    if (geoCoding != rdn.getProduct().getSceneGeoCoding()) {
      raster.setGeoLocations(create(geoCoding, rdn.getRasterSize(), random));
    }
    return raster;
  }

  private static Pixel[] createPixels(RasterDataNode rdn, Random random) throws IOException {
    Pixel[] pixels = new Pixel[5];
    for (int i = 0; i < pixels.length; i++) {
      PixelPos pos = new PixelPos(random.nextInt(rdn.getRasterWidth()), random.nextInt(rdn.getRasterHeight()));
      double[] pix = new double[1];
      rdn.readPixels((int) pos.x, (int) pos.y, 1, 1, pix);
      pixels[i] = new Pixel(pos, pix[0]);
    }
    return pixels;
  }

  private static GeoLocation[] create(GeoCoding geoCoding, Dimension dimension, Random random) {
    GeoLocation[] geoLocations = new GeoLocation[3];
    for (int i = 0; i < geoLocations.length; i++) {
      PixelPos pos = new PixelPos(random.nextInt(dimension.width), random.nextInt(dimension.height));
      GeoPos geoPos = geoCoding.getGeoPos(pos, null);
      geoLocations[i] = new GeoLocation(pos, geoPos);
    }
    return geoLocations;
  }
}
