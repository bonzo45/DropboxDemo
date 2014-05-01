package storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import things.AccountDetails;
import things.SamFile;

public interface CloudFileStore {

  public AccountDetails getAccountDetails();
  
  public void download(SamFile source, SamFile destination) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;
  public void upload(SamFile source, SamFile destination) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException;
}
