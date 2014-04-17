package storage;

import jackson.JsonConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
   * Returns information about a specified file.
   * 
   * @param filePathString
   *          - path of the file.
   * @return File information.
   */
  public static FileModel getFile(String filePathString) {
    // Create Path
    java.nio.file.Path filePath = FileSystems.getDefault().getPath(FILE_PATH,
        filePathString);

    // Try to read attributes.
    BasicFileAttributes attr;
    try {
      attr = Files.readAttributes(filePath, BasicFileAttributes.class);
    } catch (IOException e) {
      return new FileModel();
    }

    return new models.FileModel(attr);
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
    for (int i = 0; i < listOfFiles.length; i++) {
      directoryModel.addFile(getFile(listOfFiles[i].getName()));
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

  private static void saveMetaData(String fullFilePath, String metadata) {
    try (PrintWriter out = new PrintWriter(fullFilePath)) {
      out.print(metadata);
    }
    catch (FileNotFoundException e) {
      System.err.println("Could not find file: " + fullFilePath);
    }
  }
}
