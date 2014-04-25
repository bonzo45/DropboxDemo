package dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import com.dropbox.core.DbxAccountInfo;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

import storage.LocalStorage;

public class RESTDropbox {

  // Configuration
  private DbxAppInfo appInfo;
  private DbxRequestConfig config;

  // Authorisation
  DbxWebAuthNoRedirect webAuth;

  // Client
  DbxClient client;

  /**
   * Create a Dropbox instance with no access code. Use this constructor to set
   * up authentication with a new user.
   */
  public RESTDropbox() {
    appInfo = new DbxAppInfo(DoNotCommitToGitHub.APP_KEY,
        DoNotCommitToGitHub.APP_SECRET);
    config = new DbxRequestConfig(DoNotCommitToGitHub.APP_NAME, Locale
        .getDefault().toString());    
  }

  public RESTDropbox(String accessToken) {
    this();
    setupClient(accessToken);
  }
  
  /**
   * Retrieve the link users authorise the app with.
   * 
   * @return String of the URL
   */
  public String getAuthorisationLink() {
    webAuth = new DbxWebAuthNoRedirect(config, appInfo);
    return webAuth.start();
  }

  /**
   * Given an authorisation code, generates a persistent access code to
   * authenticate the app.
   * 
   * @param authorisationCode
   *          - The response from the URL generated by getAuthorisationLink()
   * @return String of the access token
   */
  public String getAccessToken(String authorisationCode) {
    webAuth = new DbxWebAuthNoRedirect(config, appInfo);
    DbxAuthFinish authFinish;
    try {
      authFinish = webAuth.finish(authorisationCode);
    } catch (DbxException e) {
      System.err.println("Authorisation Failure: Dropbox reported an error.");
      return null;
    }

    return authFinish.accessToken;
  }

  /**
   * Given an access token sets up a client for dropbox operations.
   * 
   * @param accessToken
   *          - token allowing access to account.
   */
  private void setupClient(String accessToken) {
    client = new DbxClient(config, accessToken);
  }

  /**
   * Gets the account details.
   * @return The account details.
   */
  public DbxAccountInfo getAccountDetails() {
    DbxAccountInfo info = null;
    try {
      info = client.getAccountInfo();
    } catch (DbxException e) {
      System.err
          .println("Error Printing Account Details: Dropbox reported an error.");
    }
    return info;
  }

  /**
   * UNUSED - Returns dropbox directory information.
   * @param accessToken
   * @param directory
   * @return
   */
  public DbxEntry.WithChildren getDirectory(String accessToken, String directory) {
    setupClient(accessToken);
    
    // Try to read directory
    DbxEntry.WithChildren listing = null;
    try {
      listing = client.getMetadataWithChildren(directory);
    } catch (DbxException e) {
      System.err.println("Could not read directory: " + directory);
    }
    
    return listing;
  }
  
  /**
   * Uploads a file to Dropbox.
   * @param source - source file path e.g. "/home/user/image.jpg"
   * @param dest - destination file path e.g. "/images/image.jpg"
   */
  public void upload(String source, String dest) {
    System.err.println("Uploading " + source + " to " + dest);
    
    // Attempt to open file
    File inputFile = LocalStorage.getActualFile(source);
    try (FileInputStream inputStream = new FileInputStream(inputFile)) {
      // Attempt to upload file
      client.uploadFile(
          dest,
          DbxWriteMode.add(), //                          <-- Renames the file to (1) if it exists...
          //DbxWriteMode.force()                          <-- Blasts the file and starts again...
          //DbxWriteMode.update(String revisionToReplace) <-- Updates the file, produces conflicted copy if changed since last pull...
          inputFile.length(),
          inputStream
      );
      System.err.println("Upload Successful");
      LocalStorage.setInDropbox(source, dest);
    
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + source);
    } catch (SecurityException e) {
      System.err.println("Permission denied: " + source);
    } catch (DbxException e) {
      System.err.println("Upload Error: Dropbox returned an error.");
    } catch (IOException e) {
      System.err.println("Upload Error: Could not read file.");
    }
  }

  
  /**
   * Downloads a file from Dropbox
   * @param source - source file path e.g. "/images/image.jpg"
   * @param dest - destination file path e.g. "/home/user/image.jpg"
   */
  public void download(String source, String dest) {
    System.out.println("Downloading " + source + " to " + dest);
    // TODO: Remove this massive bodge.
    // Try to open destination for writing
    try (FileOutputStream outputStream = new FileOutputStream("/Users/Sam/Files/" + dest)){
      client.getFile(source, null, outputStream);
      System.out.println("Download Successful");
      LocalStorage.generateFileMetaData(dest, true, source);
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + dest);
    } catch (DbxException e) {
      System.err.println("Download Error: Dropbox reported an error.");
    } catch  (IOException e) {
      System.err.println("Download Error: Could not read file.");
    }
    
  }
}




















