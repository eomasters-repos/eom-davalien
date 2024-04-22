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

package org.eomasters.davalien.gui;

import com.bc.ceres.core.ProgressMonitor;
import com.bc.ceres.swing.progress.ProgressMonitorSwingWorker;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.eomasters.davalien.Davalien;
import org.eomasters.davalien.res.JsonHelper;
import org.eomasters.davalien.res.testdef.ProductContent;
import org.eomasters.davalien.res.testdef.ProductContentFactory;
import org.eomasters.davalien.res.testdef.TestDefinition;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductNode;
import org.esa.snap.rcp.SnapApp;
import org.esa.snap.rcp.util.Dialogs;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.actions.Presenter;

/**
 * Creates a DAVALIEN Test Definition File.
 *
 * @author Marco Peters
 */
@ActionID(category = "Tools", id = "org.eomasters.davalien.gui.CreateDavalienTestAction")
@ActionRegistration(displayName = "#TXT_CreateDavalienTestAction",
    popupText = "#TXT_CreateDavalienTestAction",
    lazy = false)
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 401),
    @ActionReference(path = "Context/Product/Product", position = 9001, separatorBefore = 9000)
})
@NbBundle.Messages({
    "TXT_CreateDavalienTestAction=Create DAVALIEN Test",
    "DESCR_CreateDavalienTestAction=Creates a DAVALIEN Test Definition File"
})
public class CreateDavalienTestAction extends AbstractAction implements Presenter.Popup {

  protected static final String LAST_VALIDATION_ENV_DIR = "eom.lastValidationEnvDir";
  private final Result<ProductNode> result;
  @SuppressWarnings("FieldCanBeLocal")
  private final LookupListener lookupListener;

  /**
   * Creates a new instance.
   */
  public CreateDavalienTestAction() {
    putValue(Action.NAME, Bundle.TXT_CreateDavalienTestAction());
    putValue(Action.SHORT_DESCRIPTION, Bundle.DESCR_CreateDavalienTestAction());
    putValue(SMALL_ICON, new ImageIcon(
        Objects.requireNonNull(getClass().getResource("icons/davalien_16.png"))));
    putValue(LARGE_ICON_KEY, new ImageIcon(
        Objects.requireNonNull(getClass().getResource("icons/davalien_32.png"))));
    result = Utilities.actionsGlobalContext().lookupResult(ProductNode.class);
    this.lookupListener = ev -> EventQueue.invokeLater(() -> setEnabled(!result.allInstances().isEmpty()));
    result.addLookupListener(WeakListeners.create(LookupListener.class, this.lookupListener, result));
    this.lookupListener.resultChanged(null);
  }

  @Override
  public boolean isEnabled() {
    return result.allInstances().stream().parallel().findAny().isPresent();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    Optional<? extends ProductNode> any = result.allInstances().stream().parallel().findAny();
    if (any.isPresent()) {
      Product product = any.get().getProduct();
      final Window window = SnapApp.getDefault().getMainFrame();
      String testName = (String) JOptionPane.showInputDialog(window, "Name of Test: ", "Give the Test a Name",
          JOptionPane.QUESTION_MESSAGE, null, null, product.getName());
      if (testName == null || testName.isEmpty()) {
        return;
      }
      Path targetDir = getValidationEnvDir(window);
      if (targetDir == null) {
        return;
      }
      final ProgressMonitorSwingWorker<Void, Void> worker = new TestDefinitionSwingWorker(window, product, testName,
          targetDir);
      worker.executeWithBlocking();
    }
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

  @Override
  public JMenuItem getPopupPresenter() {
    return new JMenuItem(this);
  }

  private static class TestDefinitionSwingWorker extends ProgressMonitorSwingWorker<Void, Void> {

    private final Window window;
    private final Product product;
    private final String testName;
    private final Path targetDir;

    public TestDefinitionSwingWorker(Window window, Product product, String testName, Path targetDir) {
      super(window, Bundle.TXT_CreateDavalienTestAction());
      this.window = window;
      this.product = product;
      this.testName = testName;
      this.targetDir = targetDir;
    }

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
        Davalien.LOGGER.log(Level.SEVERE, "Failed to create test definition", e);
        Dialogs.showError(e.getClass().getSimpleName() + ": " + e.getMessage());
      } finally {
        pm.done();
      }
      return null;
    }
  }
}
