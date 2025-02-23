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

package org.eomasters.davalien.gui;

import java.util.Objects;
import javax.swing.ImageIcon;
import org.eomasters.davalien.Davalien;

public class Icons {

  public static final ImageIcon IMAGE_ICON_16 = new ImageIcon(
      Objects.requireNonNull(Icons.class.getResource("icons/davalien_16.png")));
  public static final ImageIcon IMAGE_ICON_32 = new ImageIcon(
      Objects.requireNonNull(Icons.class.getResource("icons/davalien_32.png")));
}
