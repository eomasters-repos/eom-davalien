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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

  private FileUtils() {
  }

  public static Path getOptionalFile(Path envPath, String configFileName) throws IOException {
    Path configFile = envPath.resolve(configFileName);
    if (!Files.exists(configFile)) {
      return null;
    }
    if (!Files.isReadable(configFile)) {
      throw new IOException("Config file is not readable: " + configFile);
    }
    return configFile;
  }

  public static Path getMandatoryFile(Path envPath, String configFileName) throws IOException {
    Path configFile = envPath.resolve(configFileName);
    if (!Files.isReadable(configFile)) {
      throw new IOException("Config file does not exist or is not readable: " + configFile);
    }
    return configFile;
  }
}
