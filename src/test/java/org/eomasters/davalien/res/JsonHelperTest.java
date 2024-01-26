/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
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

package org.eomasters.davalien.res;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonHelperTest {

  @Test
  void jsonConversion() {
    String json = JsonHelper.toJson(List.of(new Resource("abc", "path1"), new Resource("def", "path2",
        "some descriptive text")));
    Map<String, Resource> resourceList = JsonHelper.getResources(new StringReader(json));
    assertEquals(2, resourceList.size());
    assertEquals("abc", resourceList.get("abc").getId());
    assertEquals("path1", resourceList.get("abc").getPath());
    assertEquals("def", resourceList.get("def").getId());
    assertEquals("some descriptive text", resourceList.get("def").getDescription());
    assertEquals("path2", resourceList.get("def").getPath());
  }

}
