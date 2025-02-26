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

import java.nio.file.Path;

/**
 * The configuration for the environment.
 *
 * <p>It is deserialized from JSON by {@link org.eomasters.davalien.res.JsonHelper#getConfig(Path)}
 */
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class EnvConfig {

  private static final String DEFAULT_FORMAT = "ZNAP";

  private int rollingResults = 2;
  private boolean deleteResultAfterSuccess = true;
  private boolean openReport = false;
  private String defaultTargetFormat = DEFAULT_FORMAT;

  /**
   * Returns how many results should be kept. After the limit is reached, the oldest results will be deleted.
   *
   * @return the number of results to be kept
   */
  public int getRollingResults() {
    return rollingResults;
  }

  /**
   * Returns if the results should be deleted after a success test.
   *
   * @return if the results of a success test should be deleted
   */
  public boolean isDeleteResultAfterSuccess() {
    return deleteResultAfterSuccess;
  }

  /**
   * Returns if the report should be opened after the validation has finished.
   *
   * @return if the report should be opened
   */
  public boolean isOpenReport() {
    return openReport;
  }

  /**
   * Returns the target product format.
   *
   * The target product format determines the format in which the products should be generated.
   *
   * @return the target product format as a string
   */
  public String getDefaultTargetFormat() {
    return defaultTargetFormat;
  }
}
