/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
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

package org.eomasters.gpttests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import org.junit.jupiter.api.Test;

public class StringFormatTest {

  @Test
  void testFormat() {
    Locale.setDefault(Locale.ENGLISH);

    assertEquals("Value: [1.000000]", String.format("Value: [%f]", 1.0));
    assertEquals("Value: [0.000001]", String.format("Value: [%f]", 1.0e-6));
    assertEquals("Value: [0.000000]", String.format("Value: [%f]", 1.0e-8));
    assertEquals("Value: [0.00000001]", String.format("Value: [%.8f]", 1.0e-8));
    assertEquals("Value: [0.00000001]", String.format("Value: [%.8f]", 1.0e-8));
    assertEquals("Value: [1.00000e-06]", String.format("Value: [%g]", 1.0e-6));
    assertEquals("Value: [1.00000e-08]", String.format("Value: [%g]", 1.0e-8));
    assertEquals("Value: [1.45689e-08]", String.format("Value: [%g]", 1.45689412e-8));
    assertEquals("Value: [1.000000e-06]", String.format("Value: [%.7g]", 1.0e-6));
    assertEquals("Value: [1.000e-08]", String.format("Value: [%.4g]", 1.0e-8));
    assertEquals("Value: [1.00000E-08]", String.format("Value: [%G]", 1.0e-8));
    assertEquals("Value: [1.000000e-06]", String.format("Value: [%e]", 1.0e-6));
    assertEquals("Value: [1.000000e-08]", String.format("Value: [%e]", 1.0e-8));
    assertEquals("Value: [1.456894e-08]", String.format("Value: [%e]", 1.45689412e-8));
    assertEquals("Value: [1.0000e-08]", String.format("Value: [%.4e]", 1.0e-8));
  }
}
