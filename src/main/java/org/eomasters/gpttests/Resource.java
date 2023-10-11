package org.eomasters.gpttests;

public class Resource {

  private String id;
  private String relPath;

  public Resource() {
  }

  public Resource(String id, String relPath) {
    this.id = id;
    this.relPath = relPath;
  }

  public String getId() {
    return id;
  }

  public String getRelPath() {
    return relPath;
  }
}
