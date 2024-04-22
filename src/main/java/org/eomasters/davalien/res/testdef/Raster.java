/*-
 * ========================LICENSE_START=================================
 * EOMasters GPT Test Environment - This projects provides a test environment for operators you have developed.
 * -> https://www.eomasters.org/
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

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@link org.esa.snap.core.datamodel.RasterDataNode raster} of a
 * {@link org.esa.snap.core.datamodel.Product product}.
 */
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
  private int[] histogramBins;
  private Double minimum;
  private Double maximum;

  /**
   * Creates a new raster with the given name and description.
   *
   * @param name        the name
   * @param description the description
   */
  public Raster(String name, String description) {
    this.name = name;
    this.description = description;
  }

  // for deserialization/serialization
  @SuppressWarnings("unused")
  private Raster() {
  }

  /**
   * Get the name of the raster.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the description of the raster.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the size of the raster.
   *
   * @return the size
   */
  public Dimension getSize() {
    return size;
  }

  /**
   * Get the data type of the raster.
   *
   * @return the data type
   */
  public DataType getDataType() {
    return dataType;
  }

  /**
   * Get the raster type of the raster.
   *
   * @return the raster type
   */
  public RasterType getRasterType() {
    return rasterType;
  }

  /**
   * Get the no-data value of the raster.
   *
   * @return the no-data value
   */
  public Double getNoDataValue() {
    return noDataValue;
  }

  /**
   * Whether the no-data value is used or not.
   *
   * @return true if the no-data value is used
   */
  public Boolean isNoDataValueUsed() {
    return noDataValueUsed;
  }

  /**
   * Get the valid pixel expression of the raster.
   *
   * @return the valid pixel expression
   */
  public String getValidPixelExpression() {
    return validPixelExpression;
  }

  /**
   * Get the defined pixels of the raster.
   *
   * @return the defined pixels
   */
  public Pixel[] getPixels() {
    return pixels;
  }

  /**
   * Get the geolocations of the raster.
   *
   * @return the defined geolocations
   */
  public GeoLocation[] getGeoLocations() {
    return geoLocations;
  }

  /**
   * Get the histogram bins of the raster.
   *
   * @return the histogram bins
   */
  public int[] getHistogramBins() {
    return histogramBins;
  }

  /**
   * Get the maximum of the raster.
   *
   * @return the maximum
   */
  public Double getMaximum() {
    return maximum;
  }

  /**
   * Get the minimum of the raster.
   *
   * @return the minimum
   */
  public Double getMinimum() {
    return minimum;
  }

  /**
   * Set the name of the raster.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the description of the raster.
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the size of the raster.
   *
   * @param size the size to set
   */
  public void setSize(Dimension size) {
    this.size = size;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  /**
   * Set the raster type of the raster.
   *
   * @param rasterType the rasterType to set
   */
  public void setRasterType(RasterType rasterType) {
    this.rasterType = rasterType;
  }

  /**
   * Set the no-data value of the raster.
   *
   * @param noDataValue the no-data value to set
   */
  public void setNoDataValue(Double noDataValue) {
    this.noDataValue = noDataValue;
  }

  /**
   * Set whether the no-data value is used or not.
   *
   * @param noDataValueUsed whether the no-data value is used
   */
  public void setNoDataValueUsed(Boolean noDataValueUsed) {
    this.noDataValueUsed = noDataValueUsed;
  }

  /**
   * Set the valid pixel expression of the raster.
   *
   * @param validPixelExpression the valid pixel expression to set
   */
  public void setValidPixelExpression(String validPixelExpression) {
    this.validPixelExpression = validPixelExpression;
  }

  /**
   * Set the defined pixels of the raster.
   *
   * @param pixels the pixels to set
   */
  public void setPixels(Pixel[] pixels) {
    this.pixels = pixels;
  }

  /**
   * Set the geolocations of the raster.
   *
   * @param geoLocations the geolocations to set
   */
  public void setGeoLocations(GeoLocation[] geoLocations) {
    this.geoLocations = geoLocations;
  }

  /**
   * Set the histogram bins of the raster.
   *
   * @param histogramBins the histogram bins
   */
  public void setHistogramBins(int[] histogramBins) {
    this.histogramBins = histogramBins;
  }

  /**
   * Set the minimum of the raster.
   *
   * @param minimum the minimum value of the raster
   */
  public void setMinimum(double minimum) {
    this.minimum = minimum;
  }

  /**
   * Set the maximum of the raster.
   *
   * @param maximum the maximum value of the raster
   */
  public void setMaximum(double maximum) {
    this.maximum = maximum;
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
        raster.getGeoLocations()) && Arrays.equals(getHistogramBins(), raster.getHistogramBins())
        && Objects.equals(getMinimum(), raster.getMinimum()) && Objects.equals(getMaximum(),
        raster.getMaximum());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getName(), getDescription(), getSize(), getDataType(), getRasterType(), getNoDataValue(),
        noDataValueUsed, getValidPixelExpression(), Arrays.hashCode(getHistogramBins()), getMinimum(), getMaximum());
    result = 31 * result + Arrays.hashCode(getPixels());
    result = 31 * result + Arrays.hashCode(getGeoLocations());
    return result;
  }

  @Override
  public String toString() {
    return "Raster{"
        + "name='" + name + '\''
        + ", description='" + description + '\''
        + ", size=" + size
        + ", dataType=" + dataType
        + ", rasterType=" + rasterType
        + ", noDataValue=" + noDataValue
        + ", noDataValueUsed=" + noDataValueUsed
        + ", validPixelExpression='" + validPixelExpression + '\''
        + ", pixels=" + Arrays.toString(pixels)
        + ", geoLocations=" + Arrays.toString(geoLocations)
        + ", minimum=" + minimum
        + ", maximum=" + maximum
        + ", histogram=" + Arrays.toString(histogramBins)
        + "}";
  }
}
