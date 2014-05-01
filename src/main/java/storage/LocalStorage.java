package storage;

import jackson.JsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.log4j.Logger;

import models.DirectoryMetadata;
import models.FileMetadata;

public class LocalStorage {

  private static Logger LOG = Logger.getLogger(LocalStorage.class);

  /**
   * Create a new file on the file system.
   * 
   * @param filePathString
   *          - path to the file.
   * @param inputStream
   *          - stream of the file.
   */
  public static void newFile(String filePath, InputStream inputStream) {
    String fullFilePath = FILE_PATH + filePath;

    // Write the file to disk
    try {
      OutputStream outpuStream = new FileOutputStream(new File(fullFilePath));
      int read = 0;
      byte[] bytes = new byte[1024];

      outpuStream = new FileOutputStream(new File(fullFilePath));
      while ((read = inputStream.read(bytes)) != -1) {
        outpuStream.write(bytes, 0, read);
      }
      outpuStream.flush();
      outpuStream.close();
    } catch (IOException e) {

      e.printStackTrace();
    }

    /* Store the metadata to a .meta file */
    generateFileMetadata(filePath, false, "");
  }

  /**
   * Returns information about all files in a directory.
   * 
   * @param directoryPath
   *          - path of the directory.
   * @return Directory information.
   */
  public static DirectoryMetadata getDirectory(String directoryPath) {
    java.io.File folder = new java.io.File(FILE_PATH + directoryPath);
    java.io.File[] listOfFiles = folder.listFiles();

    // If this directory is not valid, return null
    if (listOfFiles == null) {
      LOG.error("It's all knackered.");
      return null;
    }

    DirectoryMetadata directoryModel = new DirectoryMetadata();
    // Go through each file in the directory.
    for (File file : listOfFiles) {
      // Get it's name
      String fileName = file.getName();

      // If it's not hidden or metadata, add it to the list.
      if (!fileName.endsWith(METADATA_SUFFIX) && !fileName.startsWith(".")) {
        directoryModel.addFile(getFile(file.getName()));
      }
    }

    return directoryModel;
  }

  /**
   * Returns the stored metadata of a file.
   * 
   * @param filePathString
   *          - path of the file.
   * @return File metadata.
   */
  public static FileMetadata getFile(String filePathString) {
    // Create path to metadata
    String metadataPath = FILE_PATH + filePathString + METADATA_SUFFIX;

    // Read the stored information
    FileMetadata result = null;
    try (BufferedReader br = new BufferedReader(new FileReader(metadataPath))) {
      String line = br.readLine();
      result = JsonConverter.getObjectFromJson(line, FileMetadata.class);
    } catch (IOException e) {
      LOG.error("Could not read metadata from: " + metadataPath);
    }

    return result;
  }

  /**
   * Reads a file and generates metadata from it.
   * 
   * @param filePath
   *          - path of the file
   * @param inDropbox
   *          - is the file in dropbox?
   * @param dropboxPath
   *          - if so where is it?
   * @return
   */
  public static void generateFileMetadata(String filePath, boolean inDropbox, String dropboxPath) {
    // Gather information about file
    FileMetadata info = getMetadataFromFile(filePath);

    // Set whether or not it's in Dropbox.
    info.setInDropbox(inDropbox);

    // If the file is in Dropbox set it's dropbox path.
    if (inDropbox) {
      info.setDropboxPath(dropboxPath);
    }

    /* Return JSON representation of the metadata */
    saveMetadata(filePath, JsonConverter.getJSONString(info));
  }

  /**
   * Returns information about a specified file. Information taken from the file itself, not the metadata file accompanying it.
   * 
   * @param filePathString
   *          - path of the file.
   * @return
   */
  private static FileMetadata getMetadataFromFile(String filePathString) {
    // Try to read attributes from the file.
    BasicFileAttributes attr;
    try {
      // Create Path
      java.nio.file.Path filePath = FileSystems.getDefault().getPath(FILE_PATH, filePathString);
      attr = Files.readAttributes(filePath, BasicFileAttributes.class);
    } catch (IOException e) {
      System.err.println("Could not read attributes from file: " + filePathString);
      return null;
    }

    // Split the file name from the path
    String name = getFileName(filePathString);
    String path = getFilePath(filePathString);

    // Create a new model with all of this information.
    return new models.FileMetadata(name, path, attr);
  }

  /**
   * Saves metadata of a newly created file.
   * 
   * @param filePath
   *          - path of the file
   * @param metadata
   *          - metadata to write
   */
  private static void saveMetadata(String filePath, String metadata) {
    // Create path to metadata
    String metadataPath = FILE_PATH + filePath + METADATA_SUFFIX;

    // Write the metadata
    try (PrintWriter out = new PrintWriter(metadataPath)) {
      out.print(metadata);
    } catch (FileNotFoundException e) {
      System.err.println("Could not find file: " + metadataPath);
    }
  }

}
