/*-
 * ========================LICENSE_START=================================
 * EOMasters DAVALIEN - The DAta VALIdation ENvironment for quality assurance of EO data.
 * -> https://www.eomasters.org/davalien
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

package org.eomasters.davalien.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class FileUtilsTest {

  @Test
  public void testGetOptionalFile_exists() throws IOException {
    try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
      Path basePath = fs.getPath("/basePath");
      Files.createDirectory(basePath);
      String fileName = "testFile.txt";

      Files.createFile(basePath.resolve(fileName));

      Path result = FileUtils.getOptionalFile(basePath, fileName);
      assertEquals(basePath.resolve(fileName), result);
    }
  }

  @Test
  public void testGetOptionalFile_doesNotExist() throws IOException {
    try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
      Path basePath = fs.getPath("/basePath");
      Files.createDirectory(basePath);

      String fileName = "nonExistentFile.txt";

      Path result = FileUtils.getOptionalFile(basePath, fileName);
      assertNull(result);
    }
  }

}
