package storage.cloud;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import model.AccountMetadata;
import storage.local.LocalFile;

public interface CloudFileStore {

  /**
   * Returns the account details associated with the cloud file store.
   * 
   * @return
   */
  public AccountMetadata getAccountDetails();

  /**
   * Downloads a file from the cloud storage provider to a local store.
   * 
   * @param source
   * @param destination
   * @throws FileNotFoundException
   * @throws AccessDeniedException
   * @throws IOException
   * @throws ProviderException
   */
  public void download(CloudFile source, LocalFile destination) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;

  /**
   * Uploads a file from the local store to the cloud provider.
   * 
   * @param source
   * @param destination
   * @throws FileNotFoundException
   * @throws AccessDeniedException
   * @throws IOException
   * @throws ProviderException
   */
  public void upload(LocalFile source, CloudFile destination) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;
}
