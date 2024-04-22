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

package org.eomasters.davalien.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class that provides utility methods for working with files.
 */
public class FileUtils {

  private FileUtils() {
  }

  /**
   * Returns the path to the file with the given name in the given base path, or null if the file does not exist.
   *
   * @param basePath the base path against which to resolve the file
   * @param fileName the file name
   * @return the path to the file, or null if the file does not exist
   * @throws IOException if the file  is not readable
   */
  public static Path getOptionalFile(Path basePath, String fileName) throws IOException {
    Path file = basePath.resolve(fileName);
    if (!Files.exists(file)) {
      return null;
    }
    if (!Files.isReadable(file)) {
      throw new IOException("File is not readable: " + file);
    }
    return file;
  }

}
