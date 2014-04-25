package jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

  public static final ObjectMapper JSON_CONVERTER = new ObjectMapper();

  /**
   * Returns a JSON string representation of the given object.
   * 
   * @param object
   * @return
   */
  public static final <T> String getJSONString(T object) {
    try {
      String string = JSON_CONVERTER.writeValueAsString(object);
      return string;
    } catch (JsonGenerationException e) {
      System.err.println("could not generate JSON for " + object);
    } catch (JsonMappingException e) {
      System.err.println("could not map object to JSON for " + object);
    } catch (IOException e) {
      System.err.println("an error occurred while jsonising object " + object);
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
  public static final <T> T getObjectFromJson(String string, Class<T> clazz) {
    try {
      T object = JSON_CONVERTER.readValue(string, clazz);
      return object;
    } catch (JsonParseException e) {
      System.err.println("Could not recover object from string " + string);
    } catch (JsonMappingException e) {
      System.err.println("Could not recover object from string " + string);
    } catch (IOException e) {
      System.err.println("An error occurred while recovering object from "
          + string);
    }
    return null;
  }
}
