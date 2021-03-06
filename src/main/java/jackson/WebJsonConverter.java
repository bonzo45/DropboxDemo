package jackson;

import java.io.IOException;

import model.FileMetadata;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class WebJsonConverter {

  private static Logger LOG = Logger.getLogger(WebJsonConverter.class);
  
  private final ObjectMapper JSON_CONVERTER = new ObjectMapper();

  public WebJsonConverter() {
    SimpleModule module = new SimpleModule();
    module.addSerializer(FileMetadata.class, new WebFileSerializer());
    JSON_CONVERTER.registerModule(module);
  }
  
  /**
   * Returns a JSON string representation of the given object.
   * 
   * @param object
   * @return
   */
  public final <T> String getJSONString(T object) {
    try {
      String string = JSON_CONVERTER.writeValueAsString(object);
      return string;
    } catch (JsonGenerationException e) {
      LOG.error("Could not generate JSON: " + object, e);
    } catch (JsonMappingException e) {
      LOG.error("Could not map object to JSON: " + object, e);
    } catch (IOException e) {
      LOG.error("An error occurred while JSONising object: " + object, e);
    }
    return null;
  }

  /**
   * Generates an object of type clazz given a JSON String.
   * 
   * @param string
   * @param clazz
   * @return
   */
  public final <T> T getObjectFromJson(String string, Class<T> clazz) {
    try {
      T object = JSON_CONVERTER.readValue(string, clazz);
      return object;
    } catch (JsonParseException e) {
      LOG.error("Could not recover object from string: " + string, e);
    } catch (JsonMappingException e) {
      LOG.error("Could not map object from string: " + string, e);
    } catch (IOException e) {
      LOG.error("An error occurred while recovering: " + string, e);
    }
    return null;
  }
}
