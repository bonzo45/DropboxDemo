package handlers;

import jackson.JsonConverter;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

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

import mediator.ConcreteWebMediator;
import mediator.WebMediator;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataParam;

import storage.CloudFileStore;
import storage.LocalStorage;
import storage.RESTDropbox;
import storage.SamDropboxFile;
import storage.SamFile;
import storage.SamLocalFile;
import util.ResponseUtil;

@Path("/files/{file_path}")
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
  public String getFile(@PathParam("file_path") String filePath) {
    System.err.println("File Information: " + filePath);
    return JsonConverter.getJSONString(LocalStorage.getFile(filePath));
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
  public Response uploadFile(@FormDataParam("file") InputStream fileInputStream, @PathParam("file_path") String filePath) {
    LocalStorage.newFile(filePath, fileInputStream);
    return Response.ok().build();
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
    CloudFileStore dropbox = new RESTDropbox(accessToken);
    WebMediator mediator = new ConcreteWebMediator(dropbox);
    SamFile source = new SamLocalFile(filePath);
    SamFile dest = new SamDropboxFile(filePath);
    Response response = mediator.upload(source,  dest);
    
    // If upload was successful, add dropbox as a copy.
    if (ResponseUtil.isOk(response)) {
      source.addCopy("dropbox", dest.getFullPath());
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
    CloudFileStore dropbox = new RESTDropbox(accessToken);
    WebMediator mediator = new ConcreteWebMediator(dropbox);
    SamFile source = new SamDropboxFile(filePath);
    SamFile dest = new SamLocalFile(filePath);
    Response response = mediator.download(source, dest);
    
    // If download was successful, generate metadata for it.
    if (ResponseUtil.isOk(response)) {
      // Add to the metadata that the file is stored in Dropbox.
      Map<String, String> copies = new HashMap<String, String>();
      copies.put("dropbox", source.getFullPath());
      source.generateMetadata(copies);
    }
    
    return response;
  }
}