package model;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import util.FileUtil;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMetadata {

  /* Name and Path */
  private String name;
  private String path;

  /* BasicFileAttributes Fields */
  private long creationTime;
  private long lastModifiedTime;
  private boolean directory;
  private boolean file;
  private long size;

  /* Where are copies of this file? Dropbox/Google Drive/Mars? */
  private Map<String, String> copies = new HashMap<String, String>();

  public FileMetadata(String fullPath) {
    this.name = FileUtil.extractName(fullPath);
    this.path = FileUtil.extractPath(fullPath);
  }

  public FileMetadata() {
    
  }
  
  /*
   * Constructor for when all attributes are not yet known.
   */
  public FileMetadata(String name, String path) {
    this.name = name;
    this.path = path;
  }

  /**
   * Constructor for when attributes are known.
   * 
   * @param name
   *          - name of the file.
   * @param path
   *          - path of the file.
   * @param attr
   *          - attributes retrieved from the file.
   */
  public FileMetadata(String name, String path, BasicFileAttributes attr) {
    this.name = name;
    this.path = path;

    setAttributes(attr);
  }

  /**
   * A constructor used by Jackson to create an object from a JSON representation.
   * 
   */
  @JsonCreator
  public FileMetadata(
      @JsonProperty("name") String name,
      @JsonProperty("path") String path,
      @JsonProperty("creationTime") long creationTime,
      @JsonProperty("lastModifiedTime") long lastModifiedTime,
      @JsonProperty("directory") boolean directory,
      @JsonProperty("file") boolean file,
      @JsonProperty("size") long size,
      @JsonProperty("copies") Map<String, String> copies) {
    this.name = name;
    this.path = path;
    this.creationTime = creationTime;
    this.lastModifiedTime = lastModifiedTime;
    this.directory = directory;
    this.file = file;
    this.size = size;
    this.copies = copies;
  }

  /**
   * Set basic attributes in one go.
   * 
   * @param attr
   */
  private void setAttributes(BasicFileAttributes attr) {
    creationTime = attr.creationTime().toMillis();
    lastModifiedTime = attr.lastModifiedTime().toMillis();
    directory = attr.isDirectory();
    file = attr.isRegularFile();
    size = attr.size();
  }

  /**
   * Add another location this file is stored.
   * 
   * @param place
   * @param path
   */
  public void addCopy(String place, String path) {
    copies.put(place, path);
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

  public long getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(long lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
  }

  public boolean isDirectory() {
    return directory;
  }

  public void setDirectory(boolean directory) {
    this.directory = directory;
  }

  public boolean isFile() {
    return file;
  }

  public void setFile(boolean file) {
    this.file = file;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public Map<String, String> getCopies() {
    return copies;
  }

  public void setCopies(Map<String, String> copies) {
    this.copies = copies;
  }

}
