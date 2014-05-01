package storage;

import jackson.JsonConverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import org.apache.log4j.Logger;

import models.FileMetadata;
import util.FileUtil;

public class SamLocalFile extends SamFile {

  private static Logger LOG = Logger.getLogger(SamLocalFile.class);
  
  private static final String ROOT_PATH = "/Users/Sam/files/";
  private static final String METADATA_SUFFIX = ".meta";

  private FileMetadata model;

  public SamLocalFile(String relativePath) {
    String fileName = FileUtil.extractName(relativePath);
    String filePath = FileUtil.extractPath(relativePath);
    model = new FileMetadata(fileName, filePath);
  }

  @Override
  public InputStream getInputStream() throws FileNotFoundException {
    return new FileInputStream(ROOT_PATH + getFullPath());
  }

  @Override
  public OutputStream getOutputStream() throws FileNotFoundException {
    return new FileOutputStream(ROOT_PATH + getFullPath());
  }

  @Override
  public String getFileName() {
    return model.getName();
  }

  @Override
  public String getFilePath() {
    return model.getPath();
  }

  @Override
  public void addCopy(String place, String path) {
    model.addCopy(place, path);
    // model.save
  }

  @Override
  public void generateMetadata(Map<String, String> copies) {
    // Gather information about file
    gatherMetadata(getFullPath());

    // Add the copies information
    info.addCopies(copies);

    /* Return JSON representation of the metadata */
    saveMetadata(filePath, JsonConverter.getJSONString(info));
  }

  private void gatherMetadata(String fullPath) {
    BasicFileAttributes attr;
    try {
      java.nio.file.Path filePath = FileSystems.getDefault().getPath(ROOT_PATH, getFullPath());
      attr = Files.readAttributes(filePath, BasicFileAttributes.class);
    } catch (IOException e) {
      LOG.error("Could not read attributes from file: " + getFullPath());
      return null;
    }

    // Create a new model with all of this information.
    return new models.FileMetadata(name, path, attr);
  }

}
