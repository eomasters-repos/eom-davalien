package org.eomasters.gpttests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GptTestEnv {

  private final Path envPath;
  private final String[] testNames;
  private final String[] tags;

  public GptTestEnv(String envPath, String[] testNames, String[] tags) {
    this.envPath = Paths.get(envPath);
    this.testNames = testNames;
    this.tags = tags;
  }

  public void init() throws IOException {
    List<TestDefinition> testDefinitions = getTestDefinitions(getMandatoryFile("test-definitions.json"));
    Map<String, Resource> sourceProducts = getResources(getMandatoryFile("test-products.json"));
    Map<String, Resource> targetProducts = getResources(getOptionalFile("test-target-products.json"));
    Map<String, Resource> graphFiles = getResources(getOptionalFile("test-graphs.json"));
    Map<String, Resource> auxdataFiles = getResources(getOptionalFile("test-auxiliary-data.json"));
  }

  private List<TestDefinition> getTestDefinitions(Path fromFile) throws IOException {
    return new Gson().fromJson(Files.newBufferedReader(fromFile),
        new TypeToken<TestDefinition>() {
        }.getType());
  }

  private Map<String, Resource> getResources(Path fromFile) throws IOException {
    if (fromFile != null) {
      List<Resource> resourceList = new Gson().fromJson(Files.newBufferedReader(fromFile),
          new TypeToken<Resource>() {
          }.getType());
      return resourceList.stream().collect(Collectors.toMap(Resource::getId, Function.identity()));
    }
    return Collections.emptyMap();
  }

  private Path getOptionalFile(String configFileName) throws IOException {
    Path configFile = envPath.resolve(configFileName);
    if (!Files.exists(configFile)) {
      return null;
    }
    if (!Files.isReadable(configFile)) {
      throw new IOException("Config file is not readable: " + configFile);
    }
    return configFile;
  }

  private Path getMandatoryFile(String configFileName) throws IOException {
    Path configFile = envPath.resolve(configFileName);
    if (!Files.isReadable(configFile)) {
      throw new IOException("Config file does not exist or is not readable: " + configFile);
    }
    return configFile;
  }

  public void execute() {

  }
}
