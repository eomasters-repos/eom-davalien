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

package org.eomasters.davalien.res.testdef;

import java.awt.Dimension;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.ProductData.UTC;

/**
 * Class representing the content of a product. It is abstracting from an actual product.
 */
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


  /**
   * Get the name of the product.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the description of the product.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the type of the product.
   *
   * @return the product type
   */
  public String getProductType() {
    return productType;
  }

  /**
   * Get the scene size of the product.
   *
   * @return the scene size
   */
  public Dimension getSceneSize() {
    return sceneSize;
  }

  /**
   * Get the start time of the product.
   *
   * @return the start time
   */
  public UTC getStartTime() {
    return startTime;
  }

  /**
   * Get the end time of the product.
   *
   * @return the end time
   */
  public UTC getEndTime() {
    return endTime;
  }

  /**
   * Get the sample codings of the product.
   *
   * @return the sample codings
   */
  public Coding[] getSampleCodings() {
    return sampleCoding;
  }

  /**
   * Get the metadata of the product.
   *
   * @return the metadata
   */
  public Metadata[] getMetadata() {
    return metadata;
  }

  /**
   * Get the rasters of the product.
   *
   * @return the rasters
   */
  public Raster[] getRasters() {
    return rasters;
  }

  /**
   * Get the vector data of the product.
   *
   * @return the vector data
   */
  public Vector[] getVectors() {
    return vectorData;
  }

  /**
   * Get the geolocations of the product.
   *
   * @return the geolocations
   */
  public GeoLocation[] getGeoLocations() {
    return geoLocations;
  }

  /**
   * Set the name of the product.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the description of the product.
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the type of the product.
   *
   * @param productType the productType to set
   */
  public void setProductType(String productType) {
    this.productType = productType;
  }

  /**
   * Set the scene size of the product.
   *
   * @param sceneSize the sceneSize to set
   */
  public void setSceneSize(Dimension sceneSize) {
    this.sceneSize = sceneSize;
  }

  /**
   * Set the start time of the product.
   *
   * @param startTime the startTime to set
   */
  public void setStartTime(UTC startTime) {
    this.startTime = startTime;
  }

  /**
   * Set the end time of the product.
   *
   * @param endTime the endTime to set
   */
  public void setEndTime(UTC endTime) {
    this.endTime = endTime;
  }

  /**
   * Set the geolocations of the product.
   *
   * @param geoLocations the geolocations to set
   */
  public void setGeoLocations(GeoLocation[] geoLocations) {
    this.geoLocations = geoLocations;
  }

  /**
   * Set the rasters of the product.
   *
   * @param rasters the rasters to set
   */
  public void setRasters(Raster[] rasters) {
    this.rasters = rasters;
  }

  /**
   * Set the vector data of the product.
   *
   * @param vectorData the vectorData to set
   */
  public void setVectorData(Vector[] vectorData) {
    this.vectorData = vectorData;
  }

  /**
   * Set the metadata of the product.
   *
   * @param metadata the metadata to set
   */
  public void setMetadata(Metadata[] metadata) {
    this.metadata = metadata;
  }

  /**
   * Set the sample codings of the product.
   *
   * @param sampleCoding the sampleCoding to set
   */
  public void setSampleCoding(Coding[] sampleCoding) {
    this.sampleCoding = sampleCoding;
  }
}
