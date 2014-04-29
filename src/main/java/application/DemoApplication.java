package application;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.MvcFeature;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

public class DemoApplication extends ResourceConfig {
  
  public DemoApplication() {
    // Let the server know that we support multi-part things...
    this.register(MultiPartFeature.class);

    // We also support a logging filter, this makes requests/responses appear in the console.
    this.register(LoggingFilter.class);
    
    // We support Freemarker templates (used for index page)
    //this.property(MvcFeature.TEMPLATE_BASE_PATH, "templates");
    this.register(FreemarkerMvcFeature.class);
  }
}