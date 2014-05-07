package storage.local;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import storage.AbstractFile;
import model.FileMetadata;

public abstract class LocalFile extends AbstractFile {

  /**
   * Returns an InputStream suitable for reading the file.
   * 
   * @return
   * @throws FileNotFoundException
   */
  public abstract InputStream getInputStream() throws FileNotFoundException;

  /**
   * Returns an OutputStream suitable for writing to the file.
   * 
   * @return
   * @throws FileNotFoundException
   */
  public abstract OutputStream getOutputStream() throws FileNotFoundException;

  /**
   * Marks the file as being stored in a new place at a particular path.
   * 
   * @param place
   * @param path
   */
  public abstract void addCopy(String place, String path);

  /**
   * Sets the metadata fields which are independent of storage location.
   * 
   * e.g. Size is independent but Path is not as it could be stored in different places on different hosts.
   * 
   * @param metadata
   */
  public void setIndependentMetadata(FileMetadata metadata) {
    this.metadata.setCopies(metadata.getCopies());
    this.metadata.setCreationTime(metadata.getCreationTime());
    this.metadata.setDirectory(metadata.isDirectory());
    this.metadata.setFile(metadata.isFile());
    this.metadata.setLastModifiedTime(metadata.getLastModifiedTime());
    this.metadata.setSize(metadata.getSize());
  }

  @Override
  public String toString() {
    return getFullPath();
  }

}
