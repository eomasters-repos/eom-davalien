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

package org.eomasters.gpttests.gui;

import com.bc.ceres.core.ProgressMonitor;
import com.bc.ceres.swing.progress.ProgressMonitorSwingWorker;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.eomasters.gpttests.res.JsonHelper;
import org.eomasters.gpttests.res.testdef.ProductContent;
import org.eomasters.gpttests.res.testdef.ProductContentFactory;
import org.eomasters.gpttests.res.testdef.TestDefinition;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.rcp.SnapApp;
import org.esa.snap.rcp.util.Dialogs;
import org.geotools.util.DefaultFileFilter;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(category = "Testing", id = "org.eomasters.gpttests.gui.CreateJsonExpectationAction")
@ActionRegistration(displayName = "Create EOM Validation Expectation")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/Testing"),
    @ActionReference(path = "Context/Product/Product", position = 9001, separatorBefore = 9000)
})
public class CreateJsonExpectationAction implements ActionListener {

  private final Product product;

  public CreateJsonExpectationAction(Product product) {
    this.product = product;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    final Window window = SnapApp.getDefault().getMainFrame();
    String testName = JOptionPane.showInputDialog(window, "Name of Test: ", "Give the Test a Name",
        JOptionPane.QUESTION_MESSAGE);
    JFileChooser fileChooser = createFileChooser(testName);
    if (fileChooser.showDialog(window, "Save") != JFileChooser.APPROVE_OPTION) {
      return;
    }
    Path file = fileChooser.getSelectedFile().toPath();
    final ProgressMonitorSwingWorker<Void, Void> worker = new ProgressMonitorSwingWorker<>(window,
        "Creating Test Expectation") {
      @Override
      protected Void doInBackground(ProgressMonitor pm) {
        pm.beginTask("Collecting data...", ProgressMonitor.UNKNOWN);
        try {
          ProductContent content = ProductContentFactory.create(product, new Random(123546));
          TestDefinition testDefinition = new TestDefinition(testName, content);
          String jsonString = JsonHelper.toJson(testDefinition);
          Files.writeString(file, jsonString);
        } catch (Exception e) {
          e.printStackTrace();
          Dialogs.showError(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
          pm.done();
        }
        return null;
      }
    };
    worker.executeWithBlocking();

  }

  private JFileChooser createFileChooser(String testName) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select File");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(new DefaultFileFilter("*.json", "JSON Files"));
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), "test-" + testName + ".json"));
    return fileChooser;
  }

}
