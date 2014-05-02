package storage.local;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import models.FileMetadata;

import org.apache.log4j.Logger;

import storage.SamFile;
import util.FileUtil;

public class SamLocalFile extends SamFile {

  private static Logger LOG = Logger.getLogger(SamLocalFile.class);

  public SamLocalFile(String relativePath) {
    String fileName = FileUtil.extractName(relativePath);
    String filePath = FileUtil.extractPath(relativePath);
    metadata = new FileMetadata(fileName, filePath);
  }

  @Override
  public InputStream getInputStream() throws FileNotFoundException {
    return new FileInputStream(LocalStorage.ROOT_PATH + getFullPath());
  }

  @Override
  public OutputStream getOutputStream() throws FileNotFoundException {
    return new FileOutputStream(LocalStorage.ROOT_PATH + getFullPath());
  }

  @Override
  public void addCopy(String place, String path) {
    metadata.addCopy(place, path);
  }
}