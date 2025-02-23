/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
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

package org.eomasters.davalien;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.regex.MatchResult;
import org.junit.jupiter.api.Test;

class TestTestInst {

  @Test
  void testMatchResourceVariable() {
    List<MatchResult> matchResults = TestInst.getMatchResults("{GPH:S2_subset_resampleGraph} -Pinput={SRC:S2_MSI}");
    assertEquals(2, matchResults.size());
    String[] firstMatch = TestInst.getTokens(matchResults.get(0));
    assertEquals(3, firstMatch.length);
    assertEquals("{GPH:S2_subset_resampleGraph}", firstMatch[0]);
    assertEquals("GPH", firstMatch[1]);
    assertEquals("S2_subset_resampleGraph", firstMatch[2]);
    String[] secondMatch = TestInst.getTokens(matchResults.get(1));
    assertEquals(3, secondMatch.length);
    assertEquals("{SRC:S2_MSI}", secondMatch[0]);
    assertEquals("SRC", secondMatch[1]);
    assertEquals("S2_MSI", secondMatch[2]);

  }
}
