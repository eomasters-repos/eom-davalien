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

package org.eomasters.gpttests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.eomasters.gpttests.res.Resource;
import org.eomasters.gpttests.res.testdef.TestDefinition;
import org.junit.jupiter.api.Test;

class GptTestEnvTest {

  @Test
  void jsonConversion() {
    String json = new Gson().toJson(List.of(new Resource("abc", "path1"), new Resource("def", "path2",
        "some descriptive text")));
    List<Resource> resourceList = new Gson().fromJson(new StringReader(json), new TypeToken<List<Resource>>() {
    }.getType());
    assertEquals(2, resourceList.size());
    assertEquals("abc", resourceList.get(0).getId());
    assertEquals("path1", resourceList.get(0).getPath());
    assertEquals("def", resourceList.get(1).getId());
    assertEquals("some descriptive text", resourceList.get(1).getDescription());
    assertEquals("path2", resourceList.get(1).getPath());
  }

  @Test
  void testTestDefinitionFiltering() {
    ArrayList<TestDefinition> testDefinitions = new ArrayList<>();
    testDefinitions.add(new TestDefinition("test1", new String[]{"ABC"}));
    testDefinitions.add(new TestDefinition("test2", (String[])null));
    testDefinitions.add(new TestDefinition("test3", new String[]{"hasi", "ABC"}));
    testDefinitions.add(new TestDefinition("test4", new String[]{"hundi"}));
    List<TestDefinition> filtered;

    // filtered = GptTestEnv.filterTestDefinitions(testDefinitions, List.of("test2", "test3"), List.of(""));
    // assertEquals(2, filtered.size());
    // assertEquals("test2", filtered.get(0).getTestName());
    // assertEquals("test3", filtered.get(1).getTestName());

    filtered = GptTestEnv.filterTestDefinitions(testDefinitions, List.of(), List.of("ABC"));
    assertEquals(2, filtered.size());
    assertEquals("test1", filtered.get(0).getTestName());
    assertEquals("test3", filtered.get(1).getTestName());

    filtered = GptTestEnv.filterTestDefinitions(testDefinitions, List.of(), List.of());
    assertEquals(4, filtered.size());
    assertEquals("test1", filtered.get(0).getTestName());
    assertEquals("test2", filtered.get(1).getTestName());
    assertEquals("test3", filtered.get(2).getTestName());
    assertEquals("test4", filtered.get(3).getTestName());

    filtered = GptTestEnv.filterTestDefinitions(testDefinitions, List.of("test2"), List.of("ABC"));
    assertEquals(3, filtered.size());
    assertEquals("test1", filtered.get(0).getTestName());
    assertEquals("test2", filtered.get(1).getTestName());
    assertEquals("test3", filtered.get(2).getTestName());

  }
}
