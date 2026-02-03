/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 - 2026 Marco Peters
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class PathSerializationTest {

    @Test
    void testPathSerialization() {
        Path path = Paths.get("some", "test", "path");
        String json = JsonHelper.toJson(path);
        // The current implementation uses toAbsolutePath().toString()
        assertEquals("\"" + path.toAbsolutePath().toString().replace("\\", "\\\\") + "\"", json);
    }

    @Test
    void testPathDeserialization() {
        String json = "\"C:\\\\some\\\\path\"";
        Path path = (Path) JsonHelper.fromJson(json, Path.class);
        assertNotNull(path);
        assertEquals(Paths.get("C:\\some\\path"), path);
    }

    @Test
    void testPathInObjectSerialization() {
        PathContainer container = new PathContainer(Paths.get("test", "file.txt"));
        String json = JsonHelper.toJson(container);
        assertNotNull(json);
        PathContainer deserialized = (PathContainer) JsonHelper.fromJson(json, PathContainer.class);
        assertEquals(container.getPath().toAbsolutePath(), deserialized.getPath().toAbsolutePath());
    }

    private static class PathContainer {
        private Path path;

        public PathContainer() {}

        public PathContainer(Path path) {
            this.path = path;
        }

        public Path getPath() {
            return path;
        }
    }
}
