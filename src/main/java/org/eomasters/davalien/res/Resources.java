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

package org.eomasters.davalien.res;

import static org.eomasters.davalien.utils.FileUtils.getOptionalFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.eomasters.davalien.res.testdef.TestDefinition;

/**
 * Class for accessing the resources.
 */
public class Resources {

  private static Path envPath;
  private Map<String, Resource> sourceProducts;
  private Map<String, Resource> graphFiles;
  private Map<String, Resource> auxdataFiles;

  /**
   * Creates a new instance of the {@link Resources} class.
   *
   * @param envPath the environment path to load the resources from
   * @return the resources
   * @throws IOException if an I/O error occurs
   */
  public static Resources create(Path envPath) throws IOException {
    Resources.envPath = envPath;
    Resources resources = new Resources();
    resources.sourceProducts = JsonHelper.getResources(getOptionalFile(envPath, "source-products.json"));
    resources.graphFiles = JsonHelper.getResources(getOptionalFile(envPath, "test-graphs.json"));
    resources.auxdataFiles = JsonHelper.getResources(getOptionalFile(envPath, "auxiliary-data.json"));

    return resources;
  }

  /**
   * Returns the test definitions.
   *
   * @param testsDir the tests directory
   * @return the test definitions
   * @throws IOException if an I/O error occurs
   */
  public List<TestDefinition> getTestDefinitions(Path testsDir) throws IOException {
    return JsonHelper.getTestDefinitions(envPath.resolve(testsDir));
  }

  /**
   * Returns the source products.
   *
   * @return the source products
   */
  public Map<String, Resource> getSourceProducts() {
    return sourceProducts;
  }

  /**
   * The graph files.
   *
   * @return the graph files
   */
  public Map<String, Resource> getGraphFiles() {
    return graphFiles;
  }

  /**
   * The auxiliary data files.
   *
   * @return the auxiliary data files
   */
  public Map<String, Resource> getAuxdataFiles() {
    return auxdataFiles;
  }

  /**
   * Returns the resource specified by the given type and id.
   *
   * @param type the resource type
   * @param id   the resource id
   * @return the resource
   */
  public Resource getResource(String type, String id) {
    Resource.Type resType = Resource.Type.valueOf(type.toUpperCase());
    Resource resource;
    switch (resType) {
      case SRC:
        resource = getSourceProducts().get(id);
        break;
      case AUX:
        resource = getAuxdataFiles().get(id);
        break;
      case GPH:
        resource = getGraphFiles().get(id);
        break;
      default:
        throw new IllegalArgumentException("Unknown resource category: " + resType);
    }
    if (resource == null) {
      throw new IllegalArgumentException(String.format("Unknown resource id: [%s:%s]", resType, id));
    }
    return resource;
  }

}
