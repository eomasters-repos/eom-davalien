/*-
 * ========================LICENSE_START=================================
 * EOMasters DAVALIEN - The DAta VALIdation ENvironment for quality assurance of EO data.
 * -> https://www.eomasters.org/davalien
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

import com.bc.ceres.core.ProgressMonitor;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.eomasters.davalien.res.testdef.Coding.Sample;
import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.GeoCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.IndexCoding;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.esa.snap.core.datamodel.MetadataElement;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductNode;
import org.esa.snap.core.datamodel.ProductNodeGroup;
import org.esa.snap.core.datamodel.RasterDataNode;
import org.esa.snap.core.datamodel.SampleCoding;
import org.esa.snap.core.datamodel.Stx;
import org.esa.snap.core.datamodel.VectorDataNode;

public class DefaultContentDefinition implements ContentDefinition {

  private static final int NUM_GEOLOCATIONS = 3;
  private static final int NUM_PIXELS = 3;
  private static final int MAX_RASTERS = 5;

  private final Product product;

  public DefaultContentDefinition(Product product) {
    this.product = product;
  }

  @Override
  public Raster[] getRasters() throws IOException {
    Random random = new Random(4);
    List<Raster> rasters = new ArrayList<>();
    List<Integer> usedIndices = new ArrayList<>();
    List<RasterDataNode> rasterDataNodes = product.getRasterDataNodes();
    while (rasters.size() < Math.min(MAX_RASTERS, rasterDataNodes.size())) {
      int index = random.nextInt(rasterDataNodes.size());
      if (!usedIndices.contains(index)) {
        usedIndices.add(index);
        RasterDataNode rasterDataNode = rasterDataNodes.get(index);
        rasters.add(createRaster(rasterDataNode));
      }
    }
    return rasters.toArray(new Raster[0]);
  }

  @Override
  public Vector[] getVectors() throws IOException {
    ProductNodeGroup<VectorDataNode> vectorDataGroup = product.getVectorDataGroup();
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

  @Override
  public Coding[] getCodings() {
    List<SampleCoding> sampleCodingList = new ArrayList<>();
    sampleCodingList.addAll(List.of(product.getFlagCodingGroup().toArray(new FlagCoding[0])));
    sampleCodingList.addAll(List.of(product.getIndexCodingGroup().toArray(new IndexCoding[0])));
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


  @Override
  public Metadata[] getMetadata() {
    MetadataElement metadataRoot = product.getMetadataRoot();
    Random random = new Random(5);
    if (metadataRoot.getNumElements() > 0 || metadataRoot.getNumAttributes() > 0) {
      Set<Metadata> metadataList = new HashSet<>();
      for (int i = 0; i < 3; i++) {
        metadataList.add(findRandomMetadata(metadataRoot, random));
      }
      return metadataList.toArray(new Metadata[0]);
    }
    return null;
  }


  @Override
  public GeoLocation[] getGeoLocationsForNode(ProductNode node) {
    if (node instanceof Product) {
      Product product = (Product) node;
      return getGeoLocations(product.getSceneGeoCoding(), product.getSceneRasterSize());
    } else if (node instanceof RasterDataNode) {
      RasterDataNode raster = (RasterDataNode) node;
      if (raster.getGeoCoding() != raster.getProduct().getSceneGeoCoding()) {
        return getGeoLocations(raster.getGeoCoding(), raster.getRasterSize());
      }
    }
    return null;
  }

  private Raster createRaster(RasterDataNode rdn) throws IOException {
    Raster raster = new Raster(rdn.getName(), rdn.getDescription());
    raster.setSize(rdn.getRasterSize());
    raster.setDataType(DataType.fromTypeValue(rdn.getDataType()));
    raster.setRasterType(RasterType.get(rdn));
    raster.setNoDataValue(rdn.getNoDataValue());
    raster.setNoDataValueUsed(rdn.isNoDataValueUsed());
    raster.setValidPixelExpression(rdn.getValidPixelExpression());
    raster.setPixels(createPixels(rdn));
    Stx stx = rdn.getStx(true, ProgressMonitor.NULL);
    raster.setHistogramBins(stx.getHistogram().getBins(0));
    raster.setMinimum(stx.getMinimum());
    raster.setMaximum(stx.getMaximum());
    raster.setGeoLocations(getGeoLocationsForNode(rdn));
    return raster;
  }

  private static GeoLocation[] getGeoLocations(GeoCoding geoCoding, Dimension dimension) {
    Random random = new Random(1);
    GeoLocation[] geoLocations = new GeoLocation[NUM_GEOLOCATIONS];
    for (int i = 0; i < geoLocations.length; i++) {
      PixelPos pos = randomPixelPos(random, dimension);
      GeoPos geoPos = geoCoding.getGeoPos(pos, null);
      geoLocations[i] = new GeoLocation(pos, geoPos);
    }
    return geoLocations;
  }

  private static Pixel[] createPixels(RasterDataNode rdn) throws IOException {
    Random random = new Random(2);
    Pixel[] pixels = new Pixel[NUM_PIXELS];
    for (int i = 0; i < pixels.length; i++) {
      PixelPos pos = randomPixelPos(random, rdn.getRasterSize());
      double[] pix = new double[1];
      rdn.readPixels((int) pos.x, (int) pos.y, 1, 1, pix);
      pixels[i] = new Pixel(pos, pix[0]);
    }
    return pixels;
  }

  private static PixelPos randomPixelPos(Random random, Dimension dimension) {
    return new PixelPos(random.nextInt(dimension.width) + 0.5, random.nextInt(dimension.height) + 0.5);
  }

  private static Metadata findRandomMetadata(MetadataElement element, Random random) {
    if (element.getNumElements() > 0) {
      return findRandomMetadata(element.getElementAt(random.nextInt(element.getNumElements())), random);
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
      int elementIndex = element.getParentElement().getElementIndex(element);
      String[] elementNames = element.getParentElement().getElementNames();
      final String currentName = element.getName();
      int count = (int) Arrays.stream(elementNames).filter(name -> name.equals(currentName)).count();
      stringBuilder.insert(0, element.getName() + (count > 1 ? "[" + elementIndex + "]" : "") + "/");
      element = element.getParentElement();
    } while (!element.getName().equals("metadata"));
    return stringBuilder.toString();
  }


}
