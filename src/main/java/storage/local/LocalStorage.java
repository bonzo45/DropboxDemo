package storage.local;

import jackson.JsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import models.DirectoryMetadata;
import models.FileMetadata;

import org.apache.log4j.Logger;

import storage.LocalFileStore;
import storage.SamFile;

public class LocalStorage extends LocalFileStore {

  private static Logger LOG = Logger.getLogger(LocalStorage.class);

  public static final String ROOT_PATH = "/Users/Sam/files/";
  public static final String METADATA_SUFFIX = ".meta";

  @Override
  public SamFile newFile(String filePath, InputStream inputStream) throws FileNotFoundException, IOException {
    SamFile result = new SamLocalFile(filePath);

    // Open file to be written
    
    OutputStream outputStream;
    try {
      outputStream = result.getOutputStream();
    } catch (FileNotFoundException e) {
      LOG.error("File could not be opened for writing.", e);
      throw e;
    }
    
    // Write data
    
    int read = 0;
    byte[] bytes = new byte[1024];

    try {
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    } catch (IOException e) {
      LOG.error("Error whilst writing to file.", e);
      throw e;
    }
    
    // Close file
    
    try {
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      LOG.error("Could not flush and close destination file.", e);
      throw e;
    }

    return result;
  }

  @Override
  public SamFile getFile(String filePath) {
    SamFile result = new SamLocalFile(filePath);
    FileMetadata metadata = readMetadata(filePath);
    result.setMetadata(metadata);

    return result;
  }

  private FileMetadata readMetadata(String filePath) {
    String metadataPath = ROOT_PATH + filePath + METADATA_SUFFIX;

    FileMetadata result = null;
    try (BufferedReader br = new BufferedReader(new FileReader(metadataPath))) {
      String line = br.readLine();
      result = JsonConverter.getObjectFromJson(line, FileMetadata.class);
    } catch (IOException e) {
      LOG.error("Could not read metadata from: " + metadataPath);
    }
    return result;
  }

  @Override
  public void persistMetadata(SamFile file) throws FileNotFoundException, SecurityException {
    String metadataPath = ROOT_PATH + file.getFullPath() + METADATA_SUFFIX;

    PrintWriter out = null;
    try {
      out = new PrintWriter(metadataPath);
      out.print(JsonConverter.getJSONString(file.getMetadata()));
    } catch (FileNotFoundException e) {
      LOG.error("Could not write metadata.", e);
      throw e;
      
    } catch (SecurityException e) {
      LOG.error("Could not write metadata due to permissions.", e);
      throw e;
    }
    finally {
      if (out != null) {
        out.close();
      }
    }
  }

  /* DIRECTORY STUFF NOT REFACTORED YET */
  /**
   * Returns information about all files in a directory.
   * 
   * @param directoryPath
   *          - path of the directory.
   * @return Directory information.
   */
  public DirectoryMetadata getDirectory(String directoryPath) {
    java.io.File folder = new java.io.File(ROOT_PATH + directoryPath);
    java.io.File[] listOfFiles = folder.listFiles();

    // If this directory is not valid, return null
    if (listOfFiles == null) {
      LOG.error("Directory invalid: " + directoryPath);
      return null;
    }

    DirectoryMetadata directoryModel = new DirectoryMetadata();
    // Go through each file in the directory.
    for (File file : listOfFiles) {
      // Get it's name
      String fileName = file.getName();

      // If it's not hidden or metadata, add it to the list.
      if (!fileName.endsWith(METADATA_SUFFIX) && !fileName.startsWith(".")) {
        // directoryModel.addFile(getFile(file.getName()));
      }
    }

    return directoryModel;
  }
}
