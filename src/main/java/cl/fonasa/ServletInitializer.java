package cl.fonasa;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

import cl.fonasa.main.SpringBootUploadFile;

public class ServletInitializer extends SpringBootServletInitializer implements WebApplicationInitializer {
	   @Override   
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	      return application.sources(SpringBootUploadFile.class);   }
}
