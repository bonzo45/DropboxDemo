package storage.cloud.dropbox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import model.AccountMetadata;
import model.FileMetadata;

import org.apache.log4j.Logger;

import storage.cloud.CloudFile;
import storage.cloud.CloudFileStore;
import storage.local.LocalFile;
import util.FileUtil;
import application.DoNotCommitToGitHub;

import com.dropbox.core.DbxAccountInfo;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuth.BadRequestException;
import com.dropbox.core.DbxWebAuth.BadStateException;
import com.dropbox.core.DbxWebAuth.CsrfException;
import com.dropbox.core.DbxWebAuth.NotApprovedException;
import com.dropbox.core.DbxWriteMode;

public class Dropbox implements CloudFileStore {

  private static Logger LOG = Logger.getLogger(Dropbox.class);

  // Configuration
  private DbxAppInfo appInfo;
  private DbxRequestConfig config;

  /* Authorisation */
  private DbxWebAuth webAuth;
  private DbxSessionStore csrfTokenStore;

  // Client
  DbxClient client;

  /**
   * Create a Dropbox instance with no access code. Use this constructor to set up authentication with a new user.
   */
  public Dropbox() {
    appInfo = new DbxAppInfo(DoNotCommitToGitHub.APP_KEY, DoNotCommitToGitHub.APP_SECRET);
    config = new DbxRequestConfig(DoNotCommitToGitHub.APP_NAME, Locale.getDefault().toString());
  }

  /**
   * Create a Dropbox instance with an access code. Use this constructor when the user has previously granted access to their account.
   * 
   * @param accessToken
   */
  public Dropbox(String accessToken) {
    this();
    setupClient(accessToken);
  }

  /**
   * Sets up a client for performing Dropbox download/upload operations.
   * 
   * @param accessToken
   *          - The persistent access token for the user.
   */
  private void setupClient(String accessToken) {
    client = new DbxClient(config, accessToken);
  }

  /**
   * Retrieves a user's Dropbox account details.
   * 
   * @return
   */
  public AccountMetadata getAccountDetails() {
    AccountMetadata info = null;
    try {
      DbxAccountInfo dropboxInfo = client.getAccountInfo();
      info = new AccountMetadata(dropboxInfo.userId, dropboxInfo.displayName, dropboxInfo.country);
    } catch (DbxException e) {
      LOG.warn("Could not fetch Dropbox Account Details", e);
    }
    return info;
  }

  /**
   * Uploads a file to Dropbox.
   * 
   * @param source
   *          - source file path e.g. "/home/user/image.jpg"
   * @param dest
   *          - destination file path e.g. "/images/image.jpg"
   */
  public void upload(LocalFile source, CloudFile dest) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException {
    LOG.info("Dropbox Upload: Uploading " + source + " to " + dest);

    InputStream inputStream = null;
    try {
      // Attempt to open file
      inputStream = source.getInputStream();
      // Attempt to upload
      client.uploadFile(dest.getFullPath(), DbxWriteMode.add(), -1, inputStream);
      LOG.info("Upload Successful");

    } catch (FileNotFoundException e) {
      String error = "Dropbox Upload: File not found: + source";
      LOG.error(error, e);
      throw e;

    } catch (SecurityException e) {
      String error = "Dropbox Upload: Permission denied: " + source;
      LOG.error(error, e);
      throw e;

    } catch (IOException e) {
      String error = "Dropbox Upload: Could not read file. " + e.getMessage();
      LOG.error(error, e);
      throw e;

    } catch (DbxException e) {
      String error = "Dropbox Upload: Dropbox returned an error. " + e.getMessage();
      LOG.error(error, e);
      throw new ProviderException(error);

    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        LOG.error("Dropbox Upload: Could not close file being uploaded.", e);
        throw e;
      }
    }
  }

  /**
   * Downloads a file from Dropbox
   * 
   * @param source
   *          - source file path e.g. "/images/image.jpg"
   * @param dest
   *          - destination file path e.g. "/home/user/image.jpg"
   * @return
   * @throws FileNotFoundException
   */
  public void download(CloudFile source, LocalFile dest) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException {
    LOG.info("Downloading " + source + " to " + dest);

    OutputStream outputStream = null;

    try {
      // Download File
      client.getFile(source.getFullPath(), null, dest.getOutputStream());
      // Download Metadata
      dest.setIndependentMetadata(downloadMetadata(source.getFullPath()));
      LOG.info("Download Successful");

    } catch (FileNotFoundException e) {
      String error = "File may be a directory or could not be created/opened: " + dest;
      LOG.error(error, e);
      throw e;

    } catch (DbxException e) {
      String error = "Dropbox returned an error.";
      LOG.error(error, e);
      throw new ProviderException(error);

    } catch (IOException e) {
      String error = "Could not write to file: " + dest;
      LOG.error(error, e);
      throw e;

    } finally {
      try {
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (IOException e) {
        LOG.error("Could not close created file.", e);
        throw e;
      }
    }
  }

  /**
   * Returns the link to redirect the user to authorise them with the app.
   * 
   * @param session
   *          - the HTTP Session. Used as a state.
   * @param sessionKey
   *          - token stored under this name.
   * @param returnURI
   *          - Where the user is returned to after authenticating.
   * @return
   */
  public String getAuthorisationRedirect(HttpSession session, String sessionKey, String returnURI) {
    // Create a Token Store?
    csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);
    // Create DbxWebAuth
    webAuth = new DbxWebAuth(config, appInfo, returnURI, csrfTokenStore);
    // Return the link to authorise
    return webAuth.start();
  }

  /**
   * Requests an Access Token from Dropbox given the response of the re-direct.
   * 
   * @param parameterMap
   *          - a map from String to String[] containing "state" and "code" where state is used for CSRF detection and code is the authentication code.
   * @return
   * @throws com.dropbox.core.DbxWebAuth.ProviderException
   * @throws DbxException
   * @throws ProviderException
   * @throws NotApprovedException
   * @throws CsrfException
   * @throws BadStateException
   * @throws BadRequestException
   */
  public String getAccessTokenRedirect(Map<String, String[]> parameterMap) throws BadRequestException, BadStateException, CsrfException, NotApprovedException,
      com.dropbox.core.DbxWebAuth.ProviderException, DbxException {
    return webAuth.finish(parameterMap).accessToken;
  }

  /**
   * Downloads and Converts Dropbox Metadata to Uniform Metadata
   * 
   * @param entry
   * @return
   */
  public FileMetadata downloadMetadata(String fullPath) throws DbxException {
    // Download the Metadata
    DbxEntry entry = client.getMetadata(fullPath);

    // Build FileMetadata from response.
    FileMetadata result = new FileMetadata(entry.name, FileUtil.extractPath(entry.path));
    result.setFile(entry.isFile());
    result.setDirectory(entry.isFolder());
    DbxEntry.File fileEntry = entry.asFile();
    result.setLastModifiedTime(fileEntry.lastModified.getTime());

    // Add to the metadata that the file is stored in Dropbox.
    Map<String, String> copies = new HashMap<String, String>();
    copies.put("dropbox", fullPath);
    result.setCopies(copies);

    return result;
  }
}