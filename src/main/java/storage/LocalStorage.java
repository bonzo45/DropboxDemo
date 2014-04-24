package storage;

import jackson.JsonConverter;

import java.io.BufferedReader;
import java.io.File;
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

  private static final String FILE_PATH = "/Users/Sam/files";
  private static final String META_DATA_SUFFIX = ".meta";

  /**
   * Returns information about a specified file. Information taken from the file
   * itself, not the metadata file accompanying it.
   * 
   * @param filePathString
   *          - path of the file.
   * @return File information.
   */
  private static FileModel getFile(String filePathString) {
    // Create Path
    java.nio.file.Path filePath = 
        FileSystems.getDefault().getPath(FILE_PATH, filePathString);

    // Try to read attributes from the file.
    BasicFileAttributes attr;
    try {
      attr = Files.readAttributes(filePath, BasicFileAttributes.class);
    } catch (IOException e) {
      System.err.println("Could not read attributes from file: " + filePathString);
      return new FileModel();
    }

    // Split the file name from the path
    String name = getFileName(filePathString);
    String path = getFilePath(filePathString);

    // Create a new model with all of this information.
    return new models.FileModel(name, path, attr);
  }

  /**
   * Returns information about all files in a directory.
   * 
   * @param directoryPath
   *          - path of the directory.
   * @return Directory information.
   */
  public static DirectoryModel getDirectory(String directoryPath) {
    // Set up the directory as a 'File'
    java.io.File folder = new java.io.File("/Users/Sam/files" + directoryPath);
    java.io.File[] listOfFiles = folder.listFiles();

    DirectoryModel directoryModel = new DirectoryModel();
    for (int i = 0; i < listOfFiles.length; i++) {
      // Don't return meta files...
      String fileName = listOfFiles[i].getName();
      if (!fileName.endsWith(META_DATA_SUFFIX) && !fileName.startsWith(".")) {
        directoryModel.addFile(getMetaData(listOfFiles[i].getName()));
      }
    }

    return directoryModel;
  }

  /**
   * Create a new file on the file system.
   * 
   * @param filePathString
   *          - path to the file.
   * @param inputStream
   *          - stream of the file.
   */
  public static void newFile(String filePathString, InputStream inputStream) {
    /* Write the file to disk */
    saveFile(FILE_PATH + "/" + filePathString, inputStream);

    /* Gather information about file */
    FileModel info = getFile(filePathString);

    /* Create JSON representation of the metadata */
    String jsonInfo = JsonConverter.getJSONString(info);

    /* Store the metadata to a .meta file */
    saveMetaData(FILE_PATH + "/" + filePathString + META_DATA_SUFFIX, jsonInfo);
  }

  /**
   * Actually writes a file to the local file system.
   * 
   * @param fullFilePath
   *          - full path to the file.
   * @param inputStream
   *          - stream of the file.
   */
  private static void saveFile(String fullFilePath, InputStream inputStream) {

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

  }

  /**
   * Saves meta data of a newly created file.
   * 
   * @param fullFilePath
   *          - path of the file
   * @param metadata
   *          - meta data to write
   */
  private static void saveMetaData(String fullFilePath, String metadata) {
    try (PrintWriter out = new PrintWriter(fullFilePath)) {
      out.print(metadata);
    } catch (FileNotFoundException e) {
      System.err.println("Could not find file: " + fullFilePath);
    }
  }

  /**
   * Returns the stored meta data of a file.
   * 
   * @param filePathString
   *          - path of the file.
   * @return File meta data.
   */
  public static FileModel getMetaData(String filePathString) {
    String fullFilePath = FILE_PATH + "/" + filePathString + META_DATA_SUFFIX;

    FileModel result = null;
    try (BufferedReader br = new BufferedReader(new FileReader(fullFilePath))) {
      String line = br.readLine();
      result = JsonConverter.getObjectFromJson(line, FileModel.class);
    } catch (IOException e) {
      System.err.println("Could not find file: " + fullFilePath);
    }

    result.setName(getFileName(filePathString));
    result.setPath(getFilePath(filePathString));

    return result;
  }

  private static String getFileName(String filePath) {
    // Create Path
    java.nio.file.Path path = FileSystems.getDefault().getPath(FILE_PATH,
        filePath);

    return path.getFileName().toString();
  }

  private static String getFilePath(String filePath) {
    // Create Path
    java.nio.file.Path path = FileSystems.getDefault().getPath(FILE_PATH,
        filePath);

    String name = path.getFileName().toString();
    return filePath.substring(0, filePath.length() - name.length());
  }

  /**
   * Returns the File object corresponding to the path.
   * @param filePath - relative path of the file.
   * @return
   */
  public static File getActualFile(String filePath) {
    return new File(FILE_PATH + "/" + filePath);
  }

  public static void setInDropbox(String source, String dest) {
    FileModel originalData = getMetaData(source);
    originalData.setInDropbox(true);
    originalData.setDropboxPath(dest);
    saveMetaData(FILE_PATH + "/" + source + META_DATA_SUFFIX, JsonConverter.getJSONString(originalData));
  }
  
}
