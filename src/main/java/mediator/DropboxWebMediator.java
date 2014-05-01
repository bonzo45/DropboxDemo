package mediator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import javax.ws.rs.core.Response;

import storage.RESTDropbox;
import things.SamFile;

public class DropboxWebMediator implements WebMediator {

  private RESTDropbox dropbox;

  public DropboxWebMediator(RESTDropbox dropbox) {
    this.dropbox = dropbox;
  }

  @Override
  public Response download(SamFile source, SamFile dest) {
    try {
      dropbox.download(source, dest);
      return Response.ok().build();
      
    } catch (AccessDeniedException e) {
      return Response.status(500).build();
      
    } catch (FileNotFoundException e) {
      return Response.status(404).build();
      
    } catch (ProviderException e) {
      return Response.status(500).build();
      
    } catch (IOException e) {
      return Response.status(500).build();
      
    }
  }

  @Override
  public Response upload(SamFile source, SamFile dest) {
    try {
      dropbox.upload(source, dest);
      return Response.ok().build();
      
    } catch (AccessDeniedException e) {
      return Response.status(500).build();
      
    } catch (FileNotFoundException e) {
      return Response.status(404).build();
      
    } catch (ProviderException e) {
      return Response.status(500).build();
      
    } catch (IOException e) {
      return Response.status(500).build();
      
    }
  }

}
