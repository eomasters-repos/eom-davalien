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

public class Raster {

  private String name;
  private String description;
  private Dimension size;
  private DataType dataType;
  private RasterType rasterType;
  private Double noDataValue;
  private Boolean noDataValueUsed;
  private String validPixelExpression;
  private Pixel[] pixels;
  private GeoLocation[] geoLocations;


  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Dimension getSize() {
    return size;
  }

  public DataType getDataType() {
    return dataType;
  }

  public RasterType getRasterType() {
    return rasterType;
  }

  public Double getNoDataValue() {
    return noDataValue;
  }

  public Boolean isNoDataValueUsed() {
    return noDataValueUsed;
  }

  public String getValidPixelExpression() {
    return validPixelExpression;
  }

  public Pixel[] getPixels() {
    return pixels;
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

  public void setSize(Dimension size) {
    this.size = size;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public void setRasterType(RasterType rasterType) {
    this.rasterType = rasterType;
  }

  public void setNoDataValue(Double noDataValue) {
    this.noDataValue = noDataValue;
  }

  public void setNoDataValueUsed(Boolean noDataValueUsed) {
    this.noDataValueUsed = noDataValueUsed;
  }

  public void setValidPixelExpression(String validPixelExpression) {
    this.validPixelExpression = validPixelExpression;
  }

  public void setPixels(Pixel[] pixels) {
    this.pixels = pixels;
  }

  public void setGeoLocations(GeoLocation[] geoLocations) {
    this.geoLocations = geoLocations;
  }
}
