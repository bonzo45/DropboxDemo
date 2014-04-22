package handlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.dropbox.core.DbxEntry;

import dropbox.RESTDropbox;

@Path("/dropbox")
public class DropboxDirectory {

  // Returns account details
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String getDirectory(@QueryParam("access_token") String accessToken, @QueryParam("directory") String directory) {
    RESTDropbox dropbox = new RESTDropbox();
    DbxEntry.WithChildren listing = dropbox.getDirectory(accessToken, directory);
    
    // Display results
    String result = "";
    for (DbxEntry child : listing.children) {
        result += "{";
        result += "\"icon_name\": " + "\"" + child.iconName + "\"" + ",";
        result += "\"name\": " + "\"" + child.name + "\"" + ",";
        result += "\"path\": " + "\"" + child.path + "\"" + "";
        result += "}";
        result += ",";
    }
    return "{\"children\": [" + result.subSequence(0, result.length() - 1) + "]}";
  }
}