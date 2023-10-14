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

package org.eomasters.gpttests.res;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.awt.Dimension;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eomasters.gpttests.res.testdef.TestDefinition;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.ProductData.UTC;

public class JsonHelper {

  private static final Gson gson;

  static {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Dimension.class, new DimensionTypeAdapter());
    builder.registerTypeAdapter(PixelPos.class, new PixelPosTypeAdapter());
    builder.registerTypeAdapter(GeoPos.class, new GeoPosTypeAdapter());
    builder.registerTypeAdapter(ProductData.UTC.class, new UtcAdapter());
    builder.setPrettyPrinting();
    gson = builder.create();
  }

  public static List<TestDefinition> getTestDefinitions(Path fromDir) throws IOException {
    List<Path> testDefFiles;
    try (Stream<Path> list = Files.list(fromDir)) {
      testDefFiles = list
          .filter(f -> f.getFileName().toString().matches("test-.*\\.json"))
          .collect(Collectors.toList());
    }
    List<TestDefinition> testDefinitions = new ArrayList<>();
    for (Path testDefFile : testDefFiles) {
      try {
        testDefinitions.add(gson.fromJson(Files.newBufferedReader(testDefFile),
            new TypeToken<TestDefinition>() {
            }.getType()));
        for (TestDefinition testDefinition : testDefinitions) {
          try {
            Paths.get(testDefinition.getTestName());
          } catch (Exception e) {
            throw new IOException("Invalid test name. Name must be a valid filename: " + testDefinition.getTestName(), e);
          }
        }
      } catch (Exception e) {
        throw new IOException("Cannot read test definition file: " + testDefFile, e);
      }
    }
    return testDefinitions;
  }

  public static Map<String, Resource> getResources(Path fromFile) throws IOException {
    if (fromFile != null) {
      List<Resource> resourceList = gson.fromJson(Files.newBufferedReader(fromFile),
          new TypeToken<Resource>() {
          }.getType());
      return resourceList.stream().collect(TreeMap::new, (m, v) -> m.put(v.getId(), v), TreeMap::putAll);
    }
    return Collections.emptyMap();
  }

  public static String toJson(TestDefinition content) {
    return gson.toJson(content);
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
}
