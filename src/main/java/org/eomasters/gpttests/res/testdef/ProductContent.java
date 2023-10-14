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
import java.util.List;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.ProductData.UTC;

public class ProductContent {

  private String name;
  private String description;
  private String productType;
  private Dimension sceneSize;
  private ProductData.UTC startTime;
  private ProductData.UTC endTime;
  private GeoLocation[] geoLocations;
  private Raster[] rasters;
  private Vector[] vectorData;
  private Metadata[] metadata;
  private Coding[] sampleCoding;


  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getProductType() {
    return productType;
  }

  public Dimension getSceneSize() {
    return sceneSize;
  }

  public UTC getStartTime() {
    return startTime;
  }

  public UTC getEndTime() {
    return endTime;
  }

  public Coding[] getSampleCodings() {
    return sampleCoding;
  }

  public Metadata[] getMetadata() {
    return metadata;
  }

  public List<Raster> getRasters() {
    return List.of(rasters);
  }

  public Vector[] getVectors() {
    return vectorData;
  }

  public GeoLocation[] getGeoLocations() {
    return geoLocations;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public void setSceneSize(Dimension sceneSize) {
    this.sceneSize = sceneSize;
  }

  public void setStartTime(UTC startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(UTC endTime) {
    this.endTime = endTime;
  }

  public void setGeoLocations(GeoLocation[] geoLocations) {
    this.geoLocations = geoLocations;
  }

  public void setRasters(Raster[] rasters) {
    this.rasters = rasters;
  }

  public void setVectorData(Vector[] vectorData) {
    this.vectorData = vectorData;
  }

  public void setMetadata(Metadata[] metadata) {
    this.metadata = metadata;
  }

  public void setSampleCoding(Coding[] sampleCoding) {
    this.sampleCoding = sampleCoding;
  }
}
