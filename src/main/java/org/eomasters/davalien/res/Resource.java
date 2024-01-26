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

public class Resource {


  public enum Type {
    SRC,
    AUX,
    GPH
  }

  private String id;
  private String description;
  private String path;

  private Resource() {
  }

  public Resource(String id, String path) {
    this(id, path, null);
  }
  public Resource(String id, String path, String description) {
    this.id = id;
    this.path = path;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getPath() {
    return path;
  }

  public String getDescription() {
    return description;
  }
}
