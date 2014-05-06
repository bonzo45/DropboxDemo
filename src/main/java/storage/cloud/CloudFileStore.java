package storage.cloud;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import storage.SamFile;
import model.AccountMetadata;

public interface CloudFileStore {

  public AccountMetadata getAccountDetails();
  
  public void download(SamFile source, SamFile destination) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;
  public void upload(SamFile source, SamFile destination) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;
}
