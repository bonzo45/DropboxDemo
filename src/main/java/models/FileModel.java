package models;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FileModel implements BasicFileAttributes {

  /* Name and Path */
  public String name;
  public String path;

  /* Standard BasicFileAttributes Fields */
  public long creationTime;
  public long lastAccessTime;
  public long lastModifiedTime;
  public boolean isDirectory;
  public boolean isOther;
  public boolean isRegularFile;
  public boolean isSymbolicLink;
  public long size;

  /* Dropbox Fields */
  public boolean isInDropbox;
  public String dropboxPath;

  public FileModel(String name, String path, BasicFileAttributes attr) {
    this.name = name;
    this.path = path;

    creationTime = attr.creationTime().toMillis();
    lastAccessTime = attr.lastAccessTime().toMillis();
    lastModifiedTime = attr.lastModifiedTime().toMillis();
    isDirectory = attr.isDirectory();
    isOther = attr.isOther();
    isRegularFile = attr.isRegularFile();
    isSymbolicLink = attr.isSymbolicLink();
    size = attr.size();

    isInDropbox = false;
    dropboxPath = "";
  }

  public FileModel() {

  }

  @JsonCreator
  public FileModel(
      @JsonProperty("name") String name,
      @JsonProperty("path") String path,
      @JsonProperty("creationTime") long creationTime,
      @JsonProperty("lastAccessTime") long lastAccessTime,
      @JsonProperty("lastModifiedTime") long lastModifiedTime,
      @JsonProperty("isDirectory") boolean isDirectory,
      @JsonProperty("isOther") boolean isOther,
      @JsonProperty("isRegularFile") boolean isRegularFile,
      @JsonProperty("isSymbolicLink") boolean isSymbolicLink,
      @JsonProperty("size") long size,
      @JsonProperty("isInDropbox") boolean isInDropbox,
      @JsonProperty("dropboxPath") String dropboxPath) {
    this.creationTime = creationTime;
    this.lastAccessTime = lastAccessTime;
    this.lastModifiedTime = lastModifiedTime;
    this.isDirectory = isDirectory;
    this.isOther = isOther;
    this.isRegularFile = isRegularFile;
    this.isSymbolicLink = isSymbolicLink;
    this.size = size;
    this.isInDropbox = isInDropbox;
    this.dropboxPath = dropboxPath;
  }

  public String toJSON() {
    String result = "";
    result += "\"name\": \"" + name + "\",";
    result += "\"path\": \"" + path + "\",";
    result += "\"creationTime\": \"" + creationTime + "\",";
    result += "\"lastAccessTime\": \"" + lastAccessTime + "\",";
    result += "\"lastModifiedTime\": \"" + lastModifiedTime + "\",";
    result += "\"isDirectory\": " + isDirectory + ",";
    result += "\"isOther\": " + isOther + ",";
    result += "\"isRegularFile\": " + isRegularFile + ",";
    result += "\"isSymbolicLink\": " + isSymbolicLink + ",";
    result += "\"size\": \"" + size + "\",";
    result += "\"isInDropbox\": " + isInDropbox + ",";
    result += "\"dropboxPath\": \"" + dropboxPath + "\"";
    return "{" + result + "}";
  }

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
    return isRegularFile;
  }

  @Override
  public boolean isDirectory() {
    return isDirectory;
  }

  @Override
  public boolean isSymbolicLink() {
    return isSymbolicLink;
  }

  @Override
  public boolean isOther() {
    return isOther;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public Object fileKey() {
    return null;
  }

  public boolean isInDropbox() {
    return isInDropbox;
  }

  public String dropboxPath() {
    return dropboxPath;
  }

  /* Getters and Setters (for Jackson) */

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

  public String getDropboxPath() {
    return dropboxPath;
  }

  public void setDropboxPath(String dropboxPath) {
    this.dropboxPath = dropboxPath;
  }

  public void setDirectory(boolean isDirectory) {
    this.isDirectory = isDirectory;
  }

  public void setOther(boolean isOther) {
    this.isOther = isOther;
  }

  public void setRegularFile(boolean isRegularFile) {
    this.isRegularFile = isRegularFile;
  }

  public void setSymbolicLink(boolean isSymbolicLink) {
    this.isSymbolicLink = isSymbolicLink;
  }

  public void setInDropbox(boolean isInDropbox) {
    this.isInDropbox = isInDropbox;
  }

}
