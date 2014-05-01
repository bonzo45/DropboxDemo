package mediator;

import javax.ws.rs.core.Response;

import things.SamFile;

public interface WebMediator {

  public Response download(SamFile source, SamFile dest);
  public Response upload(SamFile source, SamFile dest);
  
}
