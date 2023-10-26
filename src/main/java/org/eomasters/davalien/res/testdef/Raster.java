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

package org.eomasters.davalien.res.testdef;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Objects;

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

  public Raster(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public Raster() {
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Raster raster = (Raster) o;
    return Objects.equals(getName(), raster.getName()) && Objects.equals(getDescription(),
        raster.getDescription()) && Objects.equals(getSize(), raster.getSize())
        && getDataType() == raster.getDataType() && getRasterType() == raster.getRasterType() && Objects.equals(
        getNoDataValue(), raster.getNoDataValue()) && Objects.equals(noDataValueUsed, raster.noDataValueUsed)
        && Objects.equals(getValidPixelExpression(), raster.getValidPixelExpression())
        && Arrays.equals(getPixels(), raster.getPixels()) && Arrays.equals(getGeoLocations(),
        raster.getGeoLocations());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getName(), getDescription(), getSize(), getDataType(), getRasterType(), getNoDataValue(),
        noDataValueUsed, getValidPixelExpression());
    result = 31 * result + Arrays.hashCode(getPixels());
    result = 31 * result + Arrays.hashCode(getGeoLocations());
    return result;
  }

  @Override
  public String toString() {
    return String.format(
        "Raster{name='%s', description='%s', size=%s, dataType=%s, rasterType=%s, noDataValue=%s, noDataValueUsed=%s, "
            + "validPixelExpression='%s', pixels=%s, geoLocations=%s}",
        name, description, size != null ? "[" + size.width + "," + size.height + "]" : "null", dataType, rasterType, noDataValue, noDataValueUsed, validPixelExpression,
        Arrays.toString(pixels), Arrays.toString(geoLocations));
  }
}
