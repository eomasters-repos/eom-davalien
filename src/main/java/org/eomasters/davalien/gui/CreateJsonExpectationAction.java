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

package org.eomasters.davalien.gui;

import com.bc.ceres.core.ProgressMonitor;
import com.bc.ceres.swing.progress.ProgressMonitorSwingWorker;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.eomasters.davalien.res.JsonHelper;
import org.eomasters.davalien.res.testdef.ProductContent;
import org.eomasters.davalien.res.testdef.ProductContentFactory;
import org.eomasters.davalien.res.testdef.TestDefinition;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.rcp.SnapApp;
import org.esa.snap.rcp.util.Dialogs;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(category = "Testing", id = "org.eomasters.davalien.gui.CreateJsonExpectationAction")
@ActionRegistration(displayName = "Create DAVALIEN Test")
@ActionReferences({
    @ActionReference(path = "Context/Product/Product", position = 9001, separatorBefore = 9000)
})
public class CreateJsonExpectationAction implements ActionListener {

  protected static final String LAST_VALIDATION_ENV_DIR = "eom.lastValidationEnvDir";
  private final Product product;

  public CreateJsonExpectationAction(Product product) {
    this.product = product;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    final Window window = SnapApp.getDefault().getMainFrame();
    String testName = JOptionPane.showInputDialog(window, "Name of Test: ", "Give the Test a Name",
        JOptionPane.QUESTION_MESSAGE);
    Path targetDir = getValidationEnvDir(window);
    if (targetDir == null) {
      return;
    }
    final ProgressMonitorSwingWorker<Void, Void> worker = new ProgressMonitorSwingWorker<>(window,
        "Creating Validation Expectation") {
      @Override
      protected Void doInBackground(ProgressMonitor pm) {
        pm.beginTask("Collecting data...", ProgressMonitor.UNKNOWN);
        try {
          ProductContent content = ProductContentFactory.create(product);
          TestDefinition testDefinition = new TestDefinition(testName, content);
          String jsonString = JsonHelper.toJson(testDefinition);
          Path outputFile = targetDir.resolve("test-" + testName + ".json");
          boolean doWrite = true;
          if (Files.exists(outputFile)) {
            if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(window, "File already exists. Overwrite?",
                "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
              doWrite = false;
            }
          }
          if (doWrite) {
            Files.writeString(outputFile, jsonString);
          }
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

  private Path getValidationEnvDir(Window window) {
    Preferences preferences = SnapApp.getDefault().getPreferences();
    String lastDir = preferences.get(LAST_VALIDATION_ENV_DIR, "");
    if (lastDir.isEmpty()) {
      lastDir = System.getProperty("user.home");
    }
    JFileChooser dirChooser = new JFileChooser(lastDir);
    dirChooser.setDialogTitle("Select Target Directory");
    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (dirChooser.showDialog(window, "Select") != JFileChooser.APPROVE_OPTION) {
      return null;
    }
    File currentDirectory = dirChooser.getSelectedFile();
    preferences.put(LAST_VALIDATION_ENV_DIR, currentDirectory.getAbsolutePath());
    return currentDirectory.toPath();
  }

}
