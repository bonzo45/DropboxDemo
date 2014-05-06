package handlers;

import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mediator.CloudWebMediator;
import mediator.CloudWebMediatorInterface;
import mediator.LocalWebMediator;
import mediator.LocalWebMediatorInterface;
import models.FileMetadata;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import storage.CloudFileStore;
import storage.SamFile;
import storage.dropbox.RESTDropbox;
import storage.dropbox.SamDropboxFile;
import storage.local.LocalStorage;
import storage.local.SamLocalFile;
import util.ResponseUtil;

@Path("files/{file_path}")
public class FileController {

  private static Logger LOG = Logger.getLogger(FileController.class);

  /**
   * Returns JSON with all of the information about a file.
   * 
   * @param filePath
   *          - path to the file e.g. "badger.jpg"
   * @return
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFile(@PathParam("file_path") String filePath) {
    LOG.info("File Information Requested: " + filePath);

    LocalStorage localStore = new LocalStorage();
    LocalWebMediatorInterface mediator = new LocalWebMediator(localStore);
    Response response = mediator.getFile(filePath);

    return response;
  }

  /**
   * Upload a file to the server.
   * 
   * @param fileInputStream
   *          - file stream of the file in the form.
   * @param filePath
   * @return
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(
      @FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetails,
      @PathParam("file_path") String filePath) {
    LocalStorage localStore = new LocalStorage();
    LocalWebMediatorInterface mediator = new LocalWebMediator(localStore);

    // Build metadata
    FileMetadata metadata = new FileMetadata(filePath);
    metadata.setSize(fileDetails.getSize());
    Date creationDate = fileDetails.getCreationDate();
    if (creationDate != null) {
      metadata.setCreationTime(creationDate.getTime());
    }
    Date modifiedDate = fileDetails.getModificationDate();
    if (modifiedDate != null) {
      metadata.setLastModifiedTime(modifiedDate.getTime());
    }
    metadata.setFile(true);
    metadata.setDirectory(false);

    return mediator.newFile(filePath, fileInputStream, metadata);
  }

  /**
   * Push a file from the server to Dropbox.
   * 
   * @param accessToken
   * @param filePath
   *          - path of file e.g. "badger.jpg"
   * @return
   */
  @PUT
  @Path("to_dropbox")
  public Response serverToDropbox(@QueryParam("access_token") String accessToken, @PathParam("file_path") String filePath) {
    LocalStorage localStore = new LocalStorage();
    LocalWebMediatorInterface localMediator = new LocalWebMediator(localStore);
    CloudFileStore dropbox = new RESTDropbox(accessToken);
    CloudWebMediatorInterface mediator = new CloudWebMediator(dropbox);

    SamFile source = new SamLocalFile(filePath);
    SamFile dest = new SamDropboxFile(filePath);
    Response response = mediator.upload(source, dest);

    // If upload was successful, save metadata.
    if (ResponseUtil.isOk(response)) {
      source.addCopy("dropbox", dest.getFullPath());

      // Save metadata permanently
      response = localMediator.persistMetadata(source);
    }

    return response;
  }

  /**
   * Pulls a file from Dropbox to the server.
   * 
   * NOTE: Files pulled from directory/subdirectory/file.jpg will be placed at /file.jpg on the server.
   * 
   * @param accessToken
   * @param filePath
   *          - path of the file e.g. "images/badger.jpg"
   * @return
   */
  @PUT
  @Path("from_dropbox")
  public Response dropboxToServer(@QueryParam("access_token") String accessToken, @PathParam("file_path") String filePath) {
    LocalStorage localStore = new LocalStorage();
    LocalWebMediatorInterface localMediator = new LocalWebMediator(localStore);
    CloudFileStore dropbox = new RESTDropbox(accessToken);
    CloudWebMediatorInterface mediator = new CloudWebMediator(dropbox);

    SamFile source = new SamDropboxFile(filePath);
    SamFile dest = new SamLocalFile(filePath);
    Response response = mediator.download(source, dest);

    // If download successful, save metadata.
    if (ResponseUtil.isOk(response)) {
      response = localMediator.persistMetadata(dest);  
    }
    
    return response;
  }
}