package storage.local;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import model.DirectoryMetadata;
import model.FileMetadata;

public abstract class LocalFileStore {

  /**
   * Creates a new file.
   * 
   * @param filePath
   * @param inputStream
   * @return
   */
  public abstract LocalFile newFile(String filePath, InputStream inputStream) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;

  /**
   * Shortcut to create a new file and set metadata in one call.
   * 
   * @param filePath
   * @param inputStream
   * @param metadata
   * @return
   */
  public LocalFile newFile(String filePath, InputStream inputStream, FileMetadata metadata) throws AccessDeniedException, FileNotFoundException, ProviderException, IOException {
    LocalFile file = newFile(filePath, inputStream);
    setMetadata(file, metadata);
    return file;
  }

  /**
   * Retrieves an existing file.
   * 
   * @param filePath
   * @return
   */
  public abstract LocalFile getFile(String filePath) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;

  /**
   * Sets metadata of file and calls persistMetadata() to store permanently.
   * 
   * @param file
   * @param metadata
   */
  public void setMetadata(LocalFile file, FileMetadata metadata) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException {
    file.setMetadata(metadata);
    persistMetadata(file);
  }

  /**
   * Save the metadata of the file to permanent storage.
   * 
   * @param file
   * @throws FileNotFoundException
   * @throws AccessDeniedException
   * @throws IOException
   * @throws ProviderException
   */
  public abstract void persistMetadata(LocalFile file) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;

  /**
   * Retrieves an existing directory.
   * 
   * @param path
   * @return
   * @throws FileNotFoundException
   * @throws AccessDeniedException
   * @throws IOException
   * @throws ProviderException
   */
  public abstract DirectoryMetadata getDirectory(String path) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;
}