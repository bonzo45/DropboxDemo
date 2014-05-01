package storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import things.AccountDetails;
import things.SamFile;

import com.dropbox.core.DbxAccountInfo;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
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

public class RESTDropbox implements CloudFileStore {

  private static Logger LOG = Logger.getLogger(RESTDropbox.class);

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
  public RESTDropbox() {
    appInfo = new DbxAppInfo(DoNotCommitToGitHub.APP_KEY, DoNotCommitToGitHub.APP_SECRET);
    config = new DbxRequestConfig(DoNotCommitToGitHub.APP_NAME, Locale.getDefault().toString());
  }

  /**
   * Create a Dropbox instance with an access code. Use this constructor when the user has previously granted access to their account.
   * 
   * @param accessToken
   */
  public RESTDropbox(String accessToken) {
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
  public AccountDetails getAccountDetails() {
    AccountDetails info = null;
    try {
      DbxAccountInfo dropboxInfo = client.getAccountInfo();
      info = new AccountDetails(dropboxInfo.userId, dropboxInfo.displayName, dropboxInfo.country);
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
  public void upload(SamFile source, SamFile dest) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException {
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
  public void download(SamFile source, SamFile dest) throws FileNotFoundException, AccessDeniedException, IOException, ProviderException {
    LOG.info("Downloading " + source + " to " + dest);

    OutputStream outputStream = null;

    try {
      // Attempt to open file
      outputStream = dest.getOutputStream();
      // Attempt to download
      client.getFile(source.getFullPath(), null, outputStream);
      LOG.info("Download Successful");

    } catch (FileNotFoundException e) {
      String error = "File may be a directory or could not be created/opened: " + dest;
      LOG.error(error, e);
      throw e;
      // return Response.status(404).entity(error).build();

    } catch (DbxException e) {
      String error = "Dropbox returned an error.";
      LOG.error(error, e);
      throw new ProviderException(error);
      // return Response.status(500).entity(error).build();

    } catch (IOException e) {
      String error = "Could not write to file: " + dest;
      LOG.error(error, e);
      throw e;
      // return Response.status(500).entity(error).build();

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
   * Returns the link to redirect the use to to authorise them with the app.
   * 
   * @param session
   *          - the HTTP Session. Why on earth does it need this?
   * @param sessionKey
   *          - not sure what this even is...
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
   * @throws DbxException
   * @throws ProviderException
   * @throws NotApprovedException
   * @throws CsrfException
   * @throws BadStateException
   * @throws BadRequestException
   */
  public String getAccessTokenRedirect(Map<String, String[]> parameterMap) throws BadRequestException, BadStateException, CsrfException, NotApprovedException, ProviderException,
      DbxException {
    return webAuth.finish(parameterMap).accessToken;
  }
}