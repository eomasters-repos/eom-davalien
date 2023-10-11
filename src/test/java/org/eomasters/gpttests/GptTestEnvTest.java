package org.eomasters.gpttests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

class GptTestEnvTest {

  @Test
  void jsonConversion() {
    String json = new Gson().toJson(List.of(new Resource("abc", "path1"), new Resource("def", "path2")));
    List<Resource> resourceList = new Gson().fromJson(new StringReader(json), new TypeToken<List<Resource>>() {
    }.getType());
    assertEquals(2, resourceList.size());
    assertEquals("abc", resourceList.get(0).getId());
    assertEquals("path1", resourceList.get(0).getRelPath());
    assertEquals("def", resourceList.get(1).getId());
    assertEquals("path2", resourceList.get(1).getRelPath());
  }
}