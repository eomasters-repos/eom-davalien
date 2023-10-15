/*-
 * ========================LICENSE_START=================================
 * EOMTBX - EOMasters Toolbox for SNAP
 * -> https://www.eomasters.org/sw/EOMTBX
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

package org.eomasters.gpttests;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionGroups;
import org.netbeans.spi.sendopts.OptionProcessor;
import org.openide.util.NbBundle;

/**
 * Option processor for the --gpttests option.
 * <p>With this option, the GPT Test Environment can be started.
 * <p>Example: <code>snap --gpttests C:\test\envPath</code></p>
 * <p>To prevent the GUI and splash screen from showing, add also {@code --nogui} {@code --nosplash} <br>
 */
@org.openide.util.lookup.ServiceProvider(service = OptionProcessor.class)
@NbBundle.Messages({
    "DSC_GptTests=Start the GPT Test Environment: snap --loopgpt <envPath> [-tests=<TestNameList>] [-tags=<TagList>]."
        + "Add also --nogui --nosplash to prevent GUI and splash screen from showing.",
    "DSC_TestNames=Optional comma separated list of test names to execute. If not provided all tests will be executed.",
    "DSC_TagNames=Optional comma separated list of tags associated with Tests to be executed. If not provided all tests will be executed."})
public class GptTestEnvOptionProcessor extends OptionProcessor {

  private static final String PROP_PLUGIN_MANAGER_CHECK_INTERVAL = "plugin.manager.check.interval";
  private static final Option gptTestsOpt;
  private static final Option testNamesOpt;
  private static final Option tagNamesOpt;
  private static final Set<Option> optionSet;

  static {
    String b = GptTestEnvOptionProcessor.class.getPackageName() + ".Bundle";
    gptTestsOpt = Option.shortDescription(Option.requiredArgument(Option.NO_SHORT_NAME, "gpttests"), b, "DSC_GptTests");
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
        String envPath = getArgument(optionValues, gptTestsOpt);
        String[] testNames = new String[0];
        String[] tags = new String[0];
        if (optionValues.containsKey(testNamesOpt)) {
          testNames = argumentToArray(getArgument(optionValues, testNamesOpt));
        }
        if (optionValues.containsKey(tagNamesOpt)) {
          tags = argumentToArray(getArgument(optionValues, tagNamesOpt));
        }
        GptTestEnv gptTestEnv = new GptTestEnv(envPath, testNames, tags);
        try {
          gptTestEnv.init();
        } catch (IOException e) {
          CommandException exception = new CommandException(80010, "GPT Test Environment cannot be started");
          exception.initCause(e);
          e.printStackTrace(env.getOutputStream());
          throw exception;
        }
        gptTestEnv.execute();
      } finally {
        if (actualUpdateInterval != null) {
          System.setProperty(PROP_PLUGIN_MANAGER_CHECK_INTERVAL, actualUpdateInterval);
        }
      }
      System.exit(0);
    }
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
