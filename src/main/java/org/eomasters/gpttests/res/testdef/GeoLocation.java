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

import java.util.Objects;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.PixelPos;

public class GeoLocation {

  private static final double DEFAULT_EPS = 1.0e-8;
  private PixelPos pixel;
  private GeoPos geo;
  private double fwdEps = DEFAULT_EPS;
  private double invEps = DEFAULT_EPS;


  public GeoLocation(PixelPos pos, GeoPos geoPos) {
    pixel = pos;
    geo = geoPos;
  }

  private GeoLocation() {
  }

  public PixelPos getPixelPos() {
    return pixel;
  }

  public GeoPos getGeoPos() {
    return geo;
  }

  public double getFwdEps() {
    return fwdEps;
  }

  public double getInvEps() {
    return invEps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoLocation location = (GeoLocation) o;
    return Double.compare(getFwdEps(), location.getFwdEps()) == 0
        && Double.compare(getInvEps(), location.getInvEps()) == 0
        && Double.compare(pixel.x, location.pixel.x) == 0 && Double.compare(pixel.y, location.pixel.y) == 0
        && Double.compare(geo.lat, location.geo.lat) == 0 && Double.compare(geo.lon, location.geo.lon) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(pixel, geo, getFwdEps(), getInvEps());
  }

  @Override
  public String toString() {
    return String.format("GeoLocation{pixel=[%s, %s], geo=[%s, %s], fwdEps=[%e], invEps=[%e]}",
        pixel.getX(), pixel.getY(), geo.getLat(), geo.getLon(), fwdEps, invEps);
  }
}
