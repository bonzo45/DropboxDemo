package models;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FileModel implements BasicFileAttributes {

  /* Name and Path */
  private String name;
  private String path;

  /* Standard BasicFileAttributes Fields */
  private long creationTime;
  private long lastAccessTime;
  private long lastModifiedTime;
  private boolean directory;
  private boolean other;
  private boolean regularFile;
  private boolean symbolicLink;
  private long size;

  /* Dropbox Fields */
  private boolean inDropbox;
  private String dropboxPath;

  public FileModel(String name, String path, BasicFileAttributes attr) {
    this.name = name;
    this.path = path;

    creationTime = attr.creationTime().toMillis();
    lastAccessTime = attr.lastAccessTime().toMillis();
    lastModifiedTime = attr.lastModifiedTime().toMillis();
    directory = attr.isDirectory();
    other = attr.isOther();
    regularFile = attr.isRegularFile();
    symbolicLink = attr.isSymbolicLink();
    size = attr.size();

    // Assume the file is not in Dropbox
    inDropbox = false;
    dropboxPath = "";
  }

  public FileModel() {

  }

  @JsonCreator
  public FileModel(
      @JsonProperty("creationTime") long creationTime,
      @JsonProperty("lastAccessTime") long lastAccessTime,
      @JsonProperty("lastModifiedTime") long lastModifiedTime,
      @JsonProperty("directory") boolean directory,
      @JsonProperty("other") boolean other,
      @JsonProperty("regularFile") boolean regularFile,
      @JsonProperty("symbolicLink") boolean symbolicLink,
      @JsonProperty("size") long size,
      @JsonProperty("inDropbox") boolean inDropbox,
      @JsonProperty("dropboxPath") String dropboxPath) {
    this.creationTime = creationTime;
    this.lastAccessTime = lastAccessTime;
    this.lastModifiedTime = lastModifiedTime;
    this.directory = directory;
    this.other = other;
    this.regularFile = regularFile;
    this.symbolicLink = symbolicLink;
    this.size = size;
    this.inDropbox = inDropbox;
    this.dropboxPath = dropboxPath;
  }

  public String toJSON() {
    String result = "";
    result += "\"name\": \"" + name + "\",";
    result += "\"path\": \"" + path + "\",";
    result += "\"creationTime\": \"" + creationTime + "\",";
    result += "\"lastAccessTime\": \"" + lastAccessTime + "\",";
    result += "\"lastModifiedTime\": \"" + lastModifiedTime + "\",";
    result += "\"directory\": " + directory + ",";
    result += "\"other\": " + other + ",";
    result += "\"regularFile\": " + regularFile + ",";
    result += "\"symbolicLink\": " + symbolicLink + ",";
    result += "\"size\": \"" + size + "\",";
    result += "\"inDropbox\": " + inDropbox + ",";
    result += "\"dropboxPath\": \"" + dropboxPath + "\"";
    return "{" + result + "}";
  }

  /* Methods Required to implement BasicFileAttributes */
  @Override
  public FileTime lastModifiedTime() {
    return FileTime.fromMillis(lastModifiedTime);
  }

  @Override
  public FileTime lastAccessTime() {
    return FileTime.fromMillis(lastAccessTime);
  }

  @Override
  public FileTime creationTime() {
    return FileTime.fromMillis(creationTime);
  }

  @Override
  public boolean isRegularFile() {
    return regularFile;
  }

  @Override
  public boolean isDirectory() {
    return directory;
  }

  @Override
  public boolean isSymbolicLink() {
    return symbolicLink;
  }

  @Override
  public boolean isOther() {
    return other;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public Object fileKey() {
    return null;
  }

    /* Getters and Setters (for Jackson) */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public long getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(long creationTime) {
    this.creationTime = creationTime;
  }

  public long getLastAccessTime() {
    return lastAccessTime;
  }

  public void setLastAccessTime(long lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public long getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(long lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public boolean isInDropbox() {
    return inDropbox;
  }

  public void setInDropbox(boolean inDropbox) {
    this.inDropbox = inDropbox;
  }

  public String getDropboxPath() {
    return dropboxPath;
  }

  public void setDropboxPath(String dropboxPath) {
    this.dropboxPath = dropboxPath;
  }

  public void setDirectory(boolean directory) {
    this.directory = directory;
  }

  public void setOther(boolean other) {
    this.other = other;
  }

  public void setRegularFile(boolean regularFile) {
    this.regularFile = regularFile;
  }

  public void setSymbolicLink(boolean symbolicLink) {
    this.symbolicLink = symbolicLink;
  }

}
