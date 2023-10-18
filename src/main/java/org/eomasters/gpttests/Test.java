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

package org.eomasters.gpttests;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.eomasters.gpttests.res.Resources;
import org.eomasters.gpttests.res.testdef.TestDefinition;
import org.esa.snap.core.dataio.ProductIOPlugInManager;
import org.esa.snap.core.dataio.ProductReaderPlugIn;

public class Test {

  private final String name;
  private final String description;
  private float executionTime = -1.0f;
  private Throwable exception;
  private Path tempProductDir;
  private Path targetPath;
  private List<String> paramList;

  public static Test create(TestDefinition testDef, Resources resources, Path resultProductDir) {
    String gptCall = testDef.getGptCall();
    if (gptCall == null || gptCall.isEmpty() || TestDefinition.GPT_CALL_REMINDER.equals(gptCall)) {
      throw new IllegalArgumentException("gptCall must be defined");
    }
    String expandedGptCall = expandVariables(gptCall, resources);
    Test test = new Test(testDef.getTestName(), testDef.getDescription());
    List<String> paramList = parseCommandline(expandedGptCall);
    String format = ensureFormat(paramList);
    test.tempProductDir = resultProductDir;
    Path targetPath = createTargetPath(test, test.tempProductDir, format);
    test.targetPath = targetPath;
    addTargetProduct(paramList, targetPath);
    test.paramList = paramList;
    return test;
  }


  public Test(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Path getTempProductDir() {
    return tempProductDir;
  }

  public Path getTargetPath() {
    return targetPath;
  }

  public List<String> getParamList() {
    return paramList;
  }

  public float getExecutionTime() {
    return executionTime;
  }

  public void setExecutionTime(float executionTime) {
    this.executionTime = executionTime;
  }

  public Throwable getException() {
    return exception;
  }

  public void setException(Throwable t) {
    this.exception = t;
  }

  private static Path createTargetPath(Test test, Path resultProductDir, String format) {
    String fileExtension = getFileExtension(format);
    return resultProductDir.resolve(test.getName() + fileExtension);
  }

  private static void addTargetProduct(List<String> paramList, Path outputPath) {
    paramList.add(1, "-t");
    paramList.add(2, outputPath.toString());
  }

  private static String ensureFormat(List<String> paramList) {
    String format = "ZNAP";
    int formatIndex = paramList.indexOf("-f");
    if (formatIndex >= 0) {
      format = paramList.get(formatIndex + 1);
    } else {
      paramList.add(1, "-f");
      paramList.add(2, format);
    }
    return format;
  }

  private static String getFileExtension(String format) {
    ProductIOPlugInManager registry = ProductIOPlugInManager.getInstance();
    Iterator<ProductReaderPlugIn> it = registry.getReaderPlugIns(format);
    if (!it.hasNext()) {
      throw new IllegalArgumentException("No reader plug-in found for format: " + format);
    }
    if(format.equalsIgnoreCase("znap")) {
      return it.next().getDefaultFileExtensions()[1];
    }
    return it.next().getDefaultFileExtensions()[0];
  }

  // Splits a command line into an array of strings. As separator serves a space character but if the space is inside
  // a pair of double quotes, it is not used as separator. The double quotes are removed from the result.
  private static List<String> parseCommandline(String cmd) {
    String[] split = cmd.trim().split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    for (int i = 0; i < split.length; i++) {
      split[i] = split[i].replace("\"", "");
    }
    return new ArrayList<>(List.of(split));
  }

  private static String expandVariables(final String gptCall, Resources resources) {
    // find tokens in gptCall which are enclosed in {}
    var ref = new Object() {
      String expandedGptCall = gptCall;
    };
    Pattern.compile("\\{.*:.*?}").matcher(gptCall).results().forEach(matchResult -> {
      String token = matchResult.group();
      // remove enclosing {}
      String tokenName = token.substring(1, token.length() - 1);
      // split category and id
      String[] split = tokenName.split(":");
      String value = resources.getResource(split[0], split[1]).getPath();
      ref.expandedGptCall = ref.expandedGptCall.replace(token, value);
    });
    return ref.expandedGptCall;
  }

}
