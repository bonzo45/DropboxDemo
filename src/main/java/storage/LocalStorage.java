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

import models.DirectoryModel;
import models.FileModel;

public class LocalStorage {

  private static final String FILE_PATH = "/Users/Sam/files/";
  private static final String METADATA_SUFFIX = ".meta";

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
  public static DirectoryModel getDirectory(String directoryPath) {
    java.io.File folder = new java.io.File("/Users/Sam/files" + directoryPath);
    java.io.File[] listOfFiles = folder.listFiles();

    DirectoryModel directoryModel = new DirectoryModel();
    // Go through each file in the directory.
    for (int i = 0; i < listOfFiles.length; i++) {
      // Get it's name
      String fileName = listOfFiles[i].getName();

      // If it's hidden or is metadata, ignore it.
      if (fileName.endsWith(METADATA_SUFFIX) || fileName.startsWith(".")) {
        continue;
      }

      // Otherwise, add it to the list.
      directoryModel.addFile(getFile(listOfFiles[i].getName()));
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
  public static FileModel getFile(String filePathString) {
    // Create path to metadata
    String metadataPath = FILE_PATH + filePathString + METADATA_SUFFIX;

    // Read the stored information
    FileModel result = null;
    try (BufferedReader br = new BufferedReader(new FileReader(metadataPath))) {
      String line = br.readLine();
      result = JsonConverter.getObjectFromJson(line, FileModel.class);
    } catch (IOException e) {
      System.err.println("Could not find file: " + metadataPath);
    }

    return result;
  }

  
  
  /**
   * Mark a file as in Dropbox.
   * 
   * @param localPath
   *          - path of file to mark
   * @param dropboxPath
   *          - path in Dropbox
   */
  public static void setInDropbox(String localPath, String dropboxPath) {
    FileModel originalData = getFile(localPath);
    originalData.setInDropbox(true);
    originalData.setDropboxPath(dropboxPath);
    saveMetadata(localPath, JsonConverter.getJSONString(originalData));
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
  public static void generateFileMetadata(String filePath, boolean inDropbox,
      String dropboxPath) {
    // Gather information about file
    FileModel info = getMetadataFromFile(filePath);

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
   * Returns information about a specified file. Information taken from the file
   * itself, not the metadata file accompanying it.
   * 
   * @param filePathString
   *          - path of the file.
   * @return
   */
  private static FileModel getMetadataFromFile(String filePathString) {
    // Try to read attributes from the file.
    BasicFileAttributes attr;
    try {
      // Create Path
      java.nio.file.Path filePath = FileSystems.getDefault().getPath(FILE_PATH,
          filePathString);
      attr = Files.readAttributes(filePath, BasicFileAttributes.class);
    } catch (IOException e) {
      System.err.println("Could not read attributes from file: "
          + filePathString);
      return null;
    }

    // Split the file name from the path
    String name = getFileName(filePathString);
    String path = getFilePath(filePathString);

    // Create a new model with all of this information.
    return new models.FileModel(name, path, attr);
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

  
  
  /**
   * Returns an input stream corresponding to a file. Can be used for reading
   * from a file directly.
   * 
   * @param filePath
   * @return
   */
  public static InputStream getFileInputStream(String filePath) {
    try {
      return new FileInputStream(FILE_PATH + filePath);
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  /**
   * Returns an output stream corresponding to a file. Can be used for writing
   * to a file directly.
   * 
   * @param filePath
   * @return
   */
  public static OutputStream getFileOutputStream(String filePath) {
    try {
      return new FileOutputStream(FILE_PATH + filePath);
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  
  
  /**
   * Separates the file name from the rest of the path.
   * 
   * @param filePath
   * @return
   */
  private static String getFileName(String filePath) {
    // Create Path
    java.nio.file.Path path = FileSystems.getDefault().getPath(FILE_PATH,
        filePath);

    return path.getFileName().toString();
  }

  /**
   * Separates the file path from the file name.
   * 
   * @param filePath
   * @return
   */
  private static String getFilePath(String filePath) {
    // Create Path
    java.nio.file.Path path = FileSystems.getDefault().getPath(FILE_PATH,
        filePath);

    String name = path.getFileName().toString();
    return filePath.substring(0, filePath.length() - name.length());
  }
}
