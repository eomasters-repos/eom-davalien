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

package org.eomasters.davalien.res;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eomasters.davalien.EnvConfig;
import org.eomasters.davalien.res.testdef.TestDefinition;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.ProductData.UTC;

/**
 * Helper class to serialize/deserialize JSON.
 *
 * @author marco
 */
public class JsonHelper {

  private static final Gson gson;

  static {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Dimension.class, new DimensionTypeAdapter());
    builder.registerTypeAdapter(PixelPos.class, new PixelPosTypeAdapter());
    builder.registerTypeAdapter(GeoPos.class, new GeoPosTypeAdapter());
    builder.registerTypeAdapter(ProductData.UTC.class, new UtcAdapter());
    builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
    builder.registerTypeHierarchyAdapter(Throwable.class, new ThrowableAdapter());
    builder.registerTypeAdapter(Path.class, new PathAdapter());
    builder.registerTypeAdapter(int[].class, new IntArrayTypeAdapter());
    builder.serializeSpecialFloatingPointValues();
    builder.setPrettyPrinting();
    gson = builder.create();
  }

  /**
   * Loads all test definitions from the given directory.
   *
   * @param fromDir the directory
   * @return a list of test definitions
   * @throws IOException if an I/O error occurs
   */
  public static List<TestDefinition> getTestDefinitions(Path fromDir) throws IOException {
    List<Path> testDefFiles;
    try (Stream<Path> list = Files.list(fromDir)) {
      testDefFiles = list
          .filter(f -> f.getFileName().toString().matches("test-.*\\.json"))
          .collect(Collectors.toList());
    }
    Set<TestDefinition> testDefinitions = new HashSet<>();
    for (Path testDefFile : testDefFiles) {
      TestDefinition testDef = readTestDefinition(testDefFile);
      if (!testDefinitions.add(testDef)) {
        throw new IllegalStateException("Duplicate test name: " + testDef.getTestName());
      }
    }
    return new ArrayList<>(testDefinitions);
  }

  /**
   * Loads all resources from the given file.
   *
   * @param fromFile the file
   * @return the resources
   * @throws IOException if an I/O error occurs
   */
  public static Map<String, Resource> getResources(Path fromFile) throws IOException {
    if (fromFile != null) {
      return getResources(Files.newBufferedReader(fromFile));
    }
    return Collections.emptyMap();
  }

  static Map<String, Resource> getResources(Reader reader) {
    List<Resource> resourceList = gson.fromJson(reader,
        new TypeToken<List<Resource>>() {
        }.getType());
    return resourceList.stream().collect(TreeMap::new, (m, v) -> m.put(v.getId(), v), TreeMap::putAll);
  }

  /**
   * Loads the environment configuration from the given file.
   *
   * @param fromFile the file
   * @return the environment configuration
   * @throws IOException if an I/O error occurs
   */
  public static EnvConfig getConfig(Path fromFile) throws IOException {
    if (fromFile != null && Files.exists(fromFile)) {
      return gson.fromJson(Files.newBufferedReader(fromFile), EnvConfig.class);
    }
    return new EnvConfig();
  }

  /**
   * Converts an object to JSON.
   *
   * @param content The object to convert
   * @return The JSON string
   */
  public static String toJson(Object content) {
    return gson.toJson(content);
  }

  /**
   * Converts a JSON string to an object.
   *
   * @param json   The JSON string
   * @param someClass The object class
   * @return The object
   */
  public static Object fromJson(String json, Class<?> someClass) {
    return gson.fromJson(json, someClass);
  }


  private static TestDefinition readTestDefinition(Path testDefFile) throws IOException {
    TestDefinition testDef;
    try {
      testDef = gson.fromJson(Files.newBufferedReader(testDefFile),
          new TypeToken<TestDefinition>() {
          }.getType());
    } catch (Exception e) {
      throw new IOException(String.format("Error reading test definition file [%s]", testDefFile.getFileName()), e);
    }
    if (testDef.getTestName() == null || testDef.getTestName().isEmpty()) {
      throw new IOException("Element 'testName' must be provided and not empty: " + testDefFile);
    }
    try {
      Paths.get(testDef.getTestName());
    } catch (Exception e) {
      throw new IOException(
          "Invalid test name '%s'. Name must follow rules of the files system: " + testDefFile, e);
    }
    String gptCall = testDef.getGptCall();
    if (gptCall == null || gptCall.isEmpty() || TestDefinition.GPT_CALL_REMINDER.equals(gptCall)) {
      throw new IOException("Element 'gptCall' must be provided and not empty: " + testDefFile);
    }
    if (testDef.getExpectation() == null) {
      throw new IOException("Element 'expectation' must be provided: " + testDefFile);
    }
    return testDef;
  }


  private static class UtcAdapter implements JsonDeserializer<ProductData.UTC>, JsonSerializer<ProductData.UTC> {

    @Override
    public ProductData.UTC deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      String timeString = json.getAsString();
      try {
        return ProductData.UTC.parse(timeString);
      } catch (ParseException e) {
        throw new JsonParseException("Cannot parse time: ", e);
      }
    }

    @Override
    public JsonElement serialize(UTC src, Type typeOfSrc, JsonSerializationContext context) {
      return context.serialize(src.format());
    }
  }

  private static class PixelPosTypeAdapter implements JsonDeserializer<PixelPos>, JsonSerializer<PixelPos> {

    @Override
    public PixelPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      double[] arr = context.deserialize(json, double[].class);
      return new PixelPos(arr[0], arr[1]);
    }

    @Override
    public JsonElement serialize(PixelPos src, Type typeOfSrc, JsonSerializationContext context) {
      JsonArray array = new JsonArray();
      array.add(src.getX());
      array.add(src.getY());
      return array;
    }
  }

  private static class GeoPosTypeAdapter implements JsonDeserializer<GeoPos>, JsonSerializer<GeoPos> {

    @Override
    public GeoPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      double[] arr = context.deserialize(json, double[].class);
      return new GeoPos(arr[0], arr[1]);
    }

    @Override
    public JsonElement serialize(GeoPos src, Type typeOfSrc, JsonSerializationContext context) {
      JsonArray array = new JsonArray();
      array.add(src.getLat());
      array.add(src.getLon());
      return array;
    }
  }

  private static class DimensionTypeAdapter implements JsonDeserializer<Dimension>, JsonSerializer<Dimension> {

    @Override
    public Dimension deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      int[] arr = context.deserialize(json, int[].class);
      return new Dimension(arr[0], arr[1]);
    }

    @Override
    public JsonElement serialize(Dimension src, Type typeOfSrc, JsonSerializationContext context) {
      JsonArray array = new JsonArray(2);
      array.add(src.width);
      array.add(src.height);
      return array;
    }
  }

  private static class ThrowableAdapter implements JsonSerializer<Throwable> {

    @Override
    public JsonElement serialize(Throwable exception, Type typeOfSrc, JsonSerializationContext context) {
      StringWriter traceWriter = new StringWriter();
      exception.printStackTrace(new PrintWriter(traceWriter));
      JsonObject jsonObject = new JsonObject();
      jsonObject.add("message", context.serialize(exception.getMessage()));
      jsonObject.add("stacktrace", context.serialize(traceWriter.toString()));
      if (exception.getCause() != null && exception.getCause() != exception.getCause().getCause()) {
        jsonObject.add("cause", context.serialize(exception.getCause()));
      }
      return jsonObject;
    }
  }

  private static class PathAdapter implements JsonSerializer<Path> {

    @Override
    public JsonElement serialize(Path path, Type typeOfSrc, JsonSerializationContext context) {
      return context.serialize(path.toAbsolutePath().toString());
    }
  }

  private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
      return context.serialize(localDateTime.withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
  }

  /**
   * Ensures that multiple (15) array elements are written on a single line. Pretty printing writes each element on a new
   * line, which is not desired.
   */
  static class IntArrayTypeAdapter extends TypeAdapter<int[]> {

    @Override
    public void write(JsonWriter out, int[] value) throws IOException {
      out.beginArray();
      // split value array in chunks of 15
      for (int i = 0; i < value.length; i += 15) {
        int len = Math.min(15, value.length - i);
        int[] chunk = new int[len];
        System.arraycopy(value, i, chunk, 0, len);
        // concatenate the chunk values to a string using ',' as separator
        String[] strings = new String[len];
        for (int j = 0; j < len; j++) {
          strings[j] = String.valueOf(chunk[j]);
        }
        String s = String.join(",", strings);
        out.value(s);
      }

      out.endArray();
    }

    @Override
    public int[] read(JsonReader in) throws IOException {
      List<Integer> list = new ArrayList<>();
      in.beginArray();
      while (in.hasNext()) {
        String[] split = in.nextString().split(",");
        Arrays.stream(split).forEach(i -> list.add(Integer.parseInt(i.trim())));
      }
      in.endArray();
      int[] arr = new int[list.size()];
      for (int i = 0; i < list.size(); i++) {
        arr[i] = list.get(i);
      }
      return arr;
    }
  }
}
