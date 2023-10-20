/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HtmlReport {

  private final static String REPORT_TEMPLATE;
  private final static String STD_ROWS_TEMPLATE;
  private static final String TARGET_PATH_TEMPLATE;
  private static final String NO_TARGET_PATH_TEMPLATE;
  private static final String EXCEPTION_ROW_TEMPLATE;
  private static final String ERRORS_ROW_TEMPLATE;
  private static final String ERROR_ITEM_TEMPLATE;
  private static final String NO_PROBLEM_ROW_TEMPLATE;
  private static final String DESCRIPTION_ELEM_TEMPLATE;

  static {
    try {
      try (InputStream resource = HtmlReport.class.getResourceAsStream("Report.template")) {
        assert resource != null;
        REPORT_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("StdRows.template")) {
        assert resource != null;
        STD_ROWS_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("TargetPath.template")) {
        assert resource != null;
        TARGET_PATH_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("NoTargetPath.template")) {
        assert resource != null;
        NO_TARGET_PATH_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("ExceptionRow.template")) {
        assert resource != null;
        EXCEPTION_ROW_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("ErrorsRow.template")) {
        assert resource != null;
        ERRORS_ROW_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("ErrorItem.template")) {
        assert resource != null;
        ERROR_ITEM_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("NoProblemRow.template")) {
        assert resource != null;
        NO_PROBLEM_ROW_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
      try (InputStream resource = HtmlReport.class.getResourceAsStream("DescriptionElem.template")) {
        assert resource != null;
        DESCRIPTION_ELEM_TEMPLATE = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String create(TestReport report) {
    HashMap<String, String> variables = new HashMap<>();
    variables.put("Date", report.getCreationTime().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    List<String> tags = report.getTags();
    variables.put("Tags", tags == null || tags.isEmpty() ? "-" : String.join(",", tags));
    List<String> testNames = report.getTestNames();
    variables.put("TestNames", testNames == null || testNames.isEmpty() ? "-" : String.join(",", testNames));
    variables.put("EnvironmentPathUrl", report.getEnvPath().toAbsolutePath().toUri().toString());
    variables.put("EnvironmentPath", report.getEnvPath().toAbsolutePath().toString());
    variables.put("NumSelectedTests", String.valueOf(report.getTestResults().size()));
    variables.put("NumAllTests", String.valueOf(report.getTestsExecuted()));
    variables.put("NumSuccess", String.valueOf(report.getNumSuccessTests()));
    variables.put("NumError", String.valueOf(report.getNumErrorTests()));
    variables.put("NumFailure", String.valueOf(report.getNumFailureTests()));
    variables.put("ReportRows", createReportRows(report));
    return expandVariables(REPORT_TEMPLATE, variables);
  }

  private static String createReportRows(TestReport report) {
    List<TestResult> testResults = report.getTestResults();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < testResults.size(); i++) {
      TestResult testResult = testResults.get(i);
      HashMap<String, String> variables = new HashMap<>();
      variables.put("TestNumber", String.valueOf(i + 1));
      variables.put("TestName", testResult.getTestName());
      variables.put("TestDescription", createDescriptionElement(testResult.getDescription()));
      variables.put("TestStatus", String.valueOf(testResult.getStatus()));
      variables.put("TestTime", String.valueOf(testResult.getDuration()));
      variables.put("TargetPathRow", createTargetPathRow(testResult.getTargetPath()));
      if (testResult.getException() != null) {
        variables.put("ProblemRow", createExceptionRow(testResult.getException()));
      } else if (testResult.getErrors() != null && !testResult.getErrors().isEmpty()) {
        variables.put("ProblemRow", createErrorRow(testResult.getErrors()));
      } else {
        variables.put("ProblemRow", NO_PROBLEM_ROW_TEMPLATE);
      }
      sb.append(expandVariables(STD_ROWS_TEMPLATE, variables));

    }
    return sb.toString();
  }

  private static String createDescriptionElement(String description) {
    if (description == null || description.isEmpty()) {
      return "";
    } else {
      HashMap<String, String> variables = new HashMap<>();
      variables.put("Description", description);
      return expandVariables(DESCRIPTION_ELEM_TEMPLATE, variables);
    }
  }

  private static String createTargetPathRow(Path targetPath) {
    if (targetPath == null) {
      return NO_TARGET_PATH_TEMPLATE;
    } else {
      HashMap<String, String> variables = new HashMap<>();
      variables.put("TargetPathUrl", targetPath.toAbsolutePath().toUri().toString());
      variables.put("TargetPath", targetPath.toAbsolutePath().toString());
      return expandVariables(TARGET_PATH_TEMPLATE, variables);
    }
  }

  private static String createErrorRow(List<AssertionError> errors) {
    HashMap<String, String> variables = new HashMap<>();
    variables.put("NumErrors", String.valueOf(errors.size()));
    variables.put("ErrorItems", createErrorItems(errors));
    return expandVariables(ERRORS_ROW_TEMPLATE, variables);
  }

  private static String createErrorItems(List<AssertionError> errors) {
    StringBuilder sb = new StringBuilder();
    for (AssertionError assertionError : errors) {
      sb.append(createErrorItem(assertionError));
    }
    return sb.toString();
  }

  private static String createErrorItem(Throwable throwable) {
    if (throwable == null) {
      return "";
    }
    HashMap<String, String> variables = new HashMap<>();
    variables.put("ErrorMessage", throwable.getMessage());
    StringWriter traceWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(traceWriter));
    variables.put("StackTrace", traceWriter.toString());
    variables.put("Cause", createErrorItem(throwable.getCause()));
    return expandVariables(ERROR_ITEM_TEMPLATE, variables);
  }

  private static String createExceptionRow(Throwable throwable) {
    HashMap<String, String> variables = new HashMap<>();
    variables.put("ExceptionMessage", throwable.getMessage());
    StringWriter traceWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(traceWriter));
    variables.put("StackTrace", traceWriter.toString());
    variables.put("Cause", createErrorItem(throwable.getCause()));
    return expandVariables(EXCEPTION_ROW_TEMPLATE, variables);
  }

  private static String expandVariables(final String text, Map<String, String> variables) {
    // find tokens in text which are enclosed in {{}}
    var ref = new Object() {
      String expandedGptCall = text;
    };
    Pattern.compile("\\{\\{.*?}}").matcher(text).results().forEach(matchResult -> {
      String token = matchResult.group();
      // remove enclosing {{}}
      String varName = token.substring(2, token.length() - 2);
      String value = variables.get(varName);
      if (value == null) {
        throw new RuntimeException("No value found for variable: " + varName);
      }
      ref.expandedGptCall = ref.expandedGptCall.replace(token, value);
    });
    return ref.expandedGptCall;
  }
}
