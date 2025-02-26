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

/**
 * An exception that can be thrown by DAVALIEN.
 */
public class DavalienException extends Exception {

  /**
   * Creates a new instance of the {@link DavalienException} class.
   *
   * @param message the message
   */
  @SuppressWarnings("unused")
  public DavalienException(String message) {
    super(message);
  }

  /**
   * Creates a new instance of the {@link DavalienException} class.
   *
   * @param cause the cause
   */
  public DavalienException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new instance of the {@link DavalienException} class.
   *
   * @param message the message
   * @param cause   the cause
   */
  public DavalienException(String message, Throwable cause) {
    super(message, cause);
  }

}
