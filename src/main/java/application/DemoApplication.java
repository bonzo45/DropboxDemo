package application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class DemoApplication extends Application {
  @Override
  public Set<Class<?>> getClasses() {
      final Set<Class<?>> classes = new HashSet<Class<?>>();
      // Let (maybe the web server)? know that we support multi-part things...
      classes.add(MultiPartFeature.class);
      // classes.add(MultiPart.class);
      classes.add(LoggingFilter.class);
      return classes;
  }
}