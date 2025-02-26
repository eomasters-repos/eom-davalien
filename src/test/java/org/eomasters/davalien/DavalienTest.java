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

package org.eomasters.davalien;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.eomasters.davalien.res.testdef.TestDefinition;
import org.junit.jupiter.api.Test;

class DavalienTest {

  @Test
  void testTestDefinitionFiltering() {
    ArrayList<TestDefinition> testDefinitions = new ArrayList<>();
    testDefinitions.add(createTestDef("test1", new String[]{"ABC"}));
    testDefinitions.add(createTestDef("test2", null));
    testDefinitions.add(createTestDef("test3", new String[]{"hasi", "ABC"}));
    testDefinitions.add(createTestDef("test4", new String[]{"hundi"}));
    List<TestDefinition> filtered;

    filtered = Davalien.filterTestDefinitions(testDefinitions, List.of(), List.of("ABC"));
    assertEquals(2, filtered.size());
    assertEquals("test1", filtered.get(0).getTestName());
    assertEquals("test3", filtered.get(1).getTestName());

    filtered = Davalien.filterTestDefinitions(testDefinitions, List.of(), List.of());
    assertEquals(4, filtered.size());
    assertEquals("test1", filtered.get(0).getTestName());
    assertEquals("test2", filtered.get(1).getTestName());
    assertEquals("test3", filtered.get(2).getTestName());
    assertEquals("test4", filtered.get(3).getTestName());

    filtered = Davalien.filterTestDefinitions(testDefinitions, List.of("test2"), List.of("ABC"));
    assertEquals(3, filtered.size());
    assertEquals("test1", filtered.get(0).getTestName());
    assertEquals("test2", filtered.get(1).getTestName());
    assertEquals("test3", filtered.get(2).getTestName());

  }

  private static TestDefinition createTestDef(String name, String[] tags) {
    TestDefinition testDefinition = new TestDefinition(name, null);
    if (tags != null) {
      testDefinition.setTags(List.of(tags));
    }

    return testDefinition;
}
}
