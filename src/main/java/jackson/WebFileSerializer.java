package jackson;

import java.io.IOException;
import java.util.Map;

import model.FileMetadata;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WebFileSerializer extends JsonSerializer<FileMetadata> {

  @Override
  public void serialize(FileMetadata metadata, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    jgen.writeStartObject();

    // Standard Fields

    jgen.writeStringField("name", metadata.getName());
    jgen.writeStringField("path", metadata.getPath());
    jgen.writeNumberField("creationTime", metadata.getCreationTime());
    jgen.writeNumberField("lastModifiedTime", metadata.getLastModifiedTime());
    jgen.writeBooleanField("directory", metadata.isDirectory());
    jgen.writeBooleanField("file", metadata.isFile());

    // Fields for Dropbox

    Map<String, String> copies = metadata.getCopies();
    
    // If the file is in Dropbox:
    String dropboxPath = copies.get("dropbox");
    if (dropboxPath != null) {
      jgen.writeBooleanField("inDropbox", true);
      jgen.writeStringField("dropboxPath", dropboxPath);
    }
    // If it's not:
    else {
      jgen.writeBooleanField("inDropbox", false);
      jgen.writeStringField("dropboxPath", "");
    }

    jgen.writeEndObject();
  }
}
