/*-
 * ========================LICENSE_START=================================
 * EOMasters GPT Test Environment - This projects provides a test environment for operators you have developed.
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.eomasters.davalien.res.Resources;
import org.eomasters.davalien.res.testdef.TestDefinition;
import org.esa.snap.core.dataio.ProductIOPlugInManager;
import org.esa.snap.core.dataio.ProductReaderPlugIn;

/**
 * A Test instance created from a {@link TestDefinition} and used in the DAVAlIEN test environment.
 */
public class TestInst {

  private static Davalien davalien;
  private final TestDefinition testDef;
  private float duration = Float.NaN;
  private Throwable exception;
  private Path resultDir;
  private Path resultPath;
  private List<String> paramList;

  /**
   * Creates a new test instance.
   *
   * @param davalien         the DAVAlIEN instance
   * @param testDef          the test definition
   * @param resources        the resources
   * @param resultProductDir the result product directory
   * @return the test instance
   * @throws DavalienException if the test instance could not be created
   */
  public static TestInst create(Davalien davalien, TestDefinition testDef, Resources resources, Path resultProductDir)
      throws DavalienException {
    TestInst test;
    try {
      TestInst.davalien = davalien;
      test = new TestInst(testDef);
      test.resultDir = resultProductDir;
      String expandedGptCall = expandVariables(testDef.getGptCall(), resources);
      List<String> paramList = parseCommandline(expandedGptCall);
      String format = ensureFormat(paramList);
      Path targetPath = createTargetPath(resultProductDir, format, testDef.getTestName());
      test.resultPath = targetPath;
      addTargetProduct(paramList, targetPath);
      test.paramList = paramList;
    } catch (Exception e) {
      throw new DavalienException("Error creating test instance for test: " + testDef.getTestName(), e);
    }
    return test;
  }

  private TestInst(TestDefinition testDef) {
    this.testDef = testDef;
  }

  /**
   * Get the test definition.
   *
   * @return the test definition
   */
  public TestDefinition getTestDef() {
    return testDef;
  }

  /**
   * Get the test name.
   *
   * @return the test name
   */
  public String getName() {
    return testDef.getTestName();
  }

  /**
   * Get the test description.
   *
   * @return the test description
   */
  public String getDescription() {
    return testDef.getDescription();
  }

  /**
   * Get the directory where the result product is stored.
   *
   * @return the result directory
   */
  public Path getResultDir() {
    return resultDir;
  }

  /**
   * Get the path to the resulting product.
   *
   * @return the target path
   */
  public Path getResultPath() {
    return resultPath;
  }

  /**
   * Get the parameter list.
   *
   * @return the parameter list
   */
  public List<String> getParamList() {
    return paramList;
  }

  /**
   * Get the duration of the test in seconds.
   *
   * @return the duration
   */
  public float getDuration() {
    return duration;
  }


  /**
   * Set the duration of the test in seconds.
   *
   * @param duration the duration to set
   */
  public void setDuration(float duration) {
    this.duration = duration;
  }

  /**
   * Get the exception that caused the test to fail.
   *
   * @return the exception
   */
  public Throwable getException() {
    return exception;
  }

  /**
   * Set the exception that caused the test to fail.
   *
   * @param t the exception
   */
  public void setException(Throwable t) {
    this.exception = t;
  }

  private static Path createTargetPath(Path resultProductDir, String format, String name) {
    String fileExtension = getFileExtension(format);
    return resultProductDir.resolve(name + fileExtension);
  }

  private static void addTargetProduct(List<String> paramList, Path outputPath) {
    paramList.add(1, "-t");
    paramList.add(2, outputPath.toString());
  }

  private static String ensureFormat(List<String> paramList) {
    String format = davalien.getDefaultTargetFormat();
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
    if (format.equalsIgnoreCase("znap")) {
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
    List<MatchResult> matchResults = getMatchResults(gptCall);
    for (MatchResult matchResult : matchResults) {
      String[] resTokens = getTokens(matchResult);
      Path path = Path.of(resources.getResource(resTokens[1], resTokens[2]).getPath());
      if (!path.isAbsolute()) {
        path = davalien.getEnvPath().resolve(path);
      }
      String resourcePath = path.toAbsolutePath().toString();
      ref.expandedGptCall = ref.expandedGptCall.replace(resTokens[0], resourcePath);

    }
    return ref.expandedGptCall;
  }

  static List<MatchResult> getMatchResults(String gptCall) {
    return Pattern.compile("\\{.*?:.*?}").matcher(gptCall).results().collect(Collectors.toList());
  }

  static String[] getTokens(MatchResult matchResult) {
    String token = matchResult.group();
    // remove enclosing {}
    String tokenName = token.substring(1, token.length() - 1);
    // split category and id
    String[] split = tokenName.split(":");
    if (split.length != 2) {
      throw new IllegalStateException("Illegal resource variable definition: " + token);
    }
    return new String[]{token, split[0], split[1]};
  }

}
