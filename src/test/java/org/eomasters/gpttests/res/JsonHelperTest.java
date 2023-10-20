package org.eomasters.gpttests.res;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonHelperTest {

  @Test
  void jsonConversion() {
    String json = JsonHelper.toJson(List.of(new Resource("abc", "path1"), new Resource("def", "path2",
        "some descriptive text")));
    Map<String, Resource> resourceList = JsonHelper.getResources(new StringReader(json));
    assertEquals(2, resourceList.size());
    assertEquals("abc", resourceList.get("abc").getId());
    assertEquals("path1", resourceList.get("abc").getPath());
    assertEquals("def", resourceList.get("def").getId());
    assertEquals("some descriptive text", resourceList.get("def").getDescription());
    assertEquals("path2", resourceList.get("def").getPath());
  }

}