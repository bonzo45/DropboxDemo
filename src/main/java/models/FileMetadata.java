package models;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMetadata {

  /* Name and Path */
  private String name;
  private String path;

  /* BasicFileAttributes Fields */
  private long creationTime;
  private long lastAccessTime;
  private long lastModifiedTime;
  private boolean directory;
  private boolean other;
  private boolean regularFile;
  private boolean symbolicLink;
  private long size;

  /* Where are copies of this file? Dropbox/Google Drive/Mars? */
  private Map<String, String> copies = new HashMap<String, String>();
  
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
   * @param creationTime
   * @param lastAccessTime
   * @param lastModifiedTime
   * @param directory
   * @param other
   * @param regularFile
   * @param symbolicLink
   * @param size
   * @param inDropbox
   * @param dropboxPath
   */
  @JsonCreator
  public FileMetadata(@JsonProperty("name") String name, @JsonProperty("path") String path, @JsonProperty("creationTime") long creationTime,
      @JsonProperty("lastAccessTime") long lastAccessTime, @JsonProperty("lastModifiedTime") long lastModifiedTime, @JsonProperty("directory") boolean directory,
      @JsonProperty("other") boolean other, @JsonProperty("regularFile") boolean regularFile, @JsonProperty("symbolicLink") boolean symbolicLink, @JsonProperty("size") long size,
      @JsonProperty("copies") Map<String, String> copies) {
    this.name = name;
    this.path = path;
    this.creationTime = creationTime;
    this.lastAccessTime = lastAccessTime;
    this.lastModifiedTime = lastModifiedTime;
    this.directory = directory;
    this.other = other;
    this.regularFile = regularFile;
    this.symbolicLink = symbolicLink;
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
    lastAccessTime = attr.lastAccessTime().toMillis();
    lastModifiedTime = attr.lastModifiedTime().toMillis();
    directory = attr.isDirectory();
    other = attr.isOther();
    regularFile = attr.isRegularFile();
    symbolicLink = attr.isSymbolicLink();
    size = attr.size();
  }

  /**
   * Add another location this file is stored.
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

  public boolean isDirectory() {
    return directory;
  }

  public void setDirectory(boolean directory) {
    this.directory = directory;
  }

  public boolean isOther() {
    return other;
  }

  public void setOther(boolean other) {
    this.other = other;
  }

  public boolean isRegularFile() {
    return regularFile;
  }

  public void setRegularFile(boolean regularFile) {
    this.regularFile = regularFile;
  }

  public boolean isSymbolicLink() {
    return symbolicLink;
  }

  public void setSymbolicLink(boolean symbolicLink) {
    this.symbolicLink = symbolicLink;
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
