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

import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.IndexCoding;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.ui.ModalDialog;

public class NewTestDialog extends ModalDialog {

  private static final String HELP_ID = "davalienCreateExpectation";

  public NewTestDialog(Window parent, String title) {
    super(parent, title, ID_OK_CANCEL_HELP, HELP_ID);
  }

  @Override
  public int show() {
    setContent(createContentPanel());
    getJDialog().setIconImage(Icons.IMAGE_ICON_16.getImage());
    return super.show();
  }

  private JPanel createContentPanel() {
    JPanel panel = new JPanel(new MigLayout("debug, top, left, gap 5, fill",
                                            "[grow 0][grow 1]"));
    panel.add(new JLabel("Location:"));
    panel.add(new JButton("..."), "align right, wrap");
    JTextField locationTextField = new JTextField();
    panel.add(locationTextField, "span 2, wmin 10, growx, wrap");
    panel.add(new JLabel("Name:"), "growx 0");
    JLabel testPrefixLabel = new JLabel("test-");
    testPrefixLabel.setEnabled(false);
    panel.add(testPrefixLabel, "split, align right, growx 0");
    panel.add(new JTextField(), "wmin 10, growx, wrap");
    panel.add(new JCheckBox("Use pins"), "wrap");
    panel.add(new JLabel("Select Raster:"), "wrap");
    JList<String> jList = new JList<>();
    JScrollPane scrollPane = new JScrollPane(jList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(200, 300));
    panel.add(scrollPane, "span 3, grow, wrap");

    return panel;
  }

  @Override
  protected void onOK() {
    super.onOK();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Product product = createCompatibleProduct();
      NewTestDialog newTestDialog = new NewTestDialog(null, "Create DAVALIEN Test");
      newTestDialog.show();
      System.exit(0);
    });
  }

  private static Product createCompatibleProduct() {
    Product reference = new Product("Test_Product_20230831_", "type", 10, 10);
    reference.addBand("B1", "1");
    reference.addBand("B2", "2");
    reference.addBand("B3", "3");
    reference.addBand("B4", "4");
    Band flags = reference.addBand("flags", "5", ProductData.TYPE_INT8);
    flags.setSampleCoding(new FlagCoding("flags"));
    Band classes = reference.addBand("classes", "11", ProductData.TYPE_INT16);
    classes.setSampleCoding(new IndexCoding("classes"));
    return reference;
  }
}
