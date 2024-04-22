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

package org.eomasters.davalien.asserts;

class AssertionUtils {

  private AssertionUtils () {
  }

  /**
   * Compares two doubles using the given epsilon. The epsilon is used to determine if the actual value is close enough
   * to the expected.
   *
   * @param exp the expected value
   * @param act the actual value
   * @param eps the epsilon
   * @return the result of the comparison
   */
  static boolean fuzzyEquals(double exp, double act, double eps) {
    if (Double.isNaN(exp) && Double.isNaN(act)) {
      return true;
    }
    return Math.abs(exp - act) < eps;
  }
}
