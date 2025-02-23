/*-
 * ========================LICENSE_START=================================
 * EOMTBX - EOMasters Toolbox for SNAP
 * -> https://www.eomasters.org/sw/EOMTBX
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

package org.eomasters.davalien;

import com.bc.ceres.jai.operator.ReinterpretDescriptor;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.esa.snap.core.util.SystemUtils;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionGroups;
import org.netbeans.spi.sendopts.OptionProcessor;
import org.openide.util.NbBundle;

/**
 * Option processor for the --validate option.
 *
 * <p>With this option, the Data Validation Environment (DAVALIEN) can be started.
 *
 * <p>Example: <code>snap --validate C:\test\envPath</code>
 *
 * <p>To prevent the GUI and splash screen from showing, add also {@code --nogui} {@code --nosplash} <br>
 */
@org.openide.util.lookup.ServiceProvider(service = OptionProcessor.class)
@NbBundle.Messages({
    "DSC_Validate=Start the Data Validation Environment: snap --validate <envPath> [-N=<TestNameList>] [-T=<TagList>]."
        + "Add also --nogui --nosplash to prevent GUI and splash screen from showing.",
    "DSC_TestNames=Optional comma separated list of test names to execute. If not provided all tests will be executed.",
    "DSC_TagNames=Optional comma separated list of tags associated with Tests to be executed. "
        + "If not provided all tests will be executed."})
public class ValidationOptionProcessor extends OptionProcessor {

  private static final String PROP_PLUGIN_MANAGER_CHECK_INTERVAL = "plugin.manager.check.interval";
  private static final Option gptTestsOpt;
  private static final Option testNamesOpt;
  private static final Option tagNamesOpt;
  private static final Set<Option> optionSet;

  static {
    String b = ValidationOptionProcessor.class.getPackageName() + ".Bundle";
    gptTestsOpt = Option.shortDescription(Option.requiredArgument(Option.NO_SHORT_NAME, "validate"), b, "DSC_Validate");
    testNamesOpt = Option.shortDescription(Option.requiredArgument('N', null), b, "DSC_TestNames");
    tagNamesOpt = Option.shortDescription(Option.requiredArgument('T', null), b, "DSC_TagNames");
    optionSet = Set.of(OptionGroups.allOf(gptTestsOpt), OptionGroups.anyOf(testNamesOpt, tagNamesOpt));
  }

  @Override
  protected Set<Option> getOptions() {
    return optionSet;
  }

  @Override
  protected void process(Env env, Map<Option, String[]> optionValues) throws CommandException {

    if (optionValues.containsKey(gptTestsOpt)) {
      String actualUpdateInterval = System.getProperty(PROP_PLUGIN_MANAGER_CHECK_INTERVAL);
      try {
        System.setProperty(PROP_PLUGIN_MANAGER_CHECK_INTERVAL, "NEVER");
        Locale.setDefault(Locale.ENGLISH); // Force usage of english locale
        // need to use a class from ceres-jai in order to get the defined JAI descriptors loaded
        SystemUtils.init3rdPartyLibs(ReinterpretDescriptor.class);

        String envPath = getArgument(optionValues, gptTestsOpt);
        String[] testNames = getTestNames(optionValues);
        String[] tags = getTags(optionValues);
        Davalien davalien = new Davalien(envPath, testNames, tags);

        doInit(env, davalien);
        List<TestResult> testResults = doExecute(davalien);
        doReport(env, davalien, testResults);

      } catch (RuntimeException re) {
        env.getErrorStream().println("Error while executing validation tests.");
        env.getErrorStream().println("Sorry, this should not have happened. Please help to fix this problem and "
            + "report the issue to EOMasters (https://www.eomasters.org/forum).");
        re.printStackTrace(env.getErrorStream());
      } finally {
        if (actualUpdateInterval != null) {
          System.setProperty(PROP_PLUGIN_MANAGER_CHECK_INTERVAL, actualUpdateInterval);
        }
      }
      System.exit(0);
    }
  }

  private static void doReport(Env env, Davalien davalien, List<TestResult> testResults) throws CommandException {
    try {
      davalien.createReport(testResults);
    } catch (IOException e) {
      CommandException exception = new CommandException(80030, "Error while creating validation reports.");
      exception.initCause(e);
      e.printStackTrace(env.getErrorStream());
      throw exception;
    }
  }

  private static List<TestResult> doExecute(Davalien davalien) throws CommandException {
    try {
      return davalien.execute();
    } catch (Exception e) {
      CommandException commandException = new CommandException(80020, "Error while executing validation tests.");
      commandException.initCause(e);
      throw commandException;
    }
  }

  private static void doInit(Env env, Davalien davalien) throws CommandException {
    try {
      davalien.init();
    } catch (Exception e) {
      CommandException exception = new CommandException(80010, "Data Validation Environment cannot be started.");
      exception.initCause(e);
      e.printStackTrace(env.getOutputStream());
      throw exception;
    }
  }

  private static String[] getTestNames(Map<Option, String[]> optionValues) throws CommandException {
    String[] testNames = null;
    if (optionValues.containsKey(testNamesOpt)) {
      testNames = argumentToArray(getArgument(optionValues, testNamesOpt));
    }
    return testNames;
  }

  private static String[] getTags(Map<Option, String[]> optionValues) throws CommandException {
    String[] tags = null;
    if (optionValues.containsKey(tagNamesOpt)) {
      tags = argumentToArray(getArgument(optionValues, tagNamesOpt));
    }
    return tags;
  }

  private static String[] argumentToArray(String testNameString) {
    return testNameString.replace("=", "").split(",");
  }

  private static String getArgument(Map<Option, String[]> optionValues, Option option) throws CommandException {
    String[] args = optionValues.get(option);
    if (args.length < 1) {
      throw new CommandException(80001, "Missing argument for option " + option);
    }
    return args[0];
  }

}
