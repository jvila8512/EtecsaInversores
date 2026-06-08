package com.etecsa.monitor.config;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProfileImageWebConfiguration implements WebMvcConfigurer {

  @Value("${app.upload.dir:uploads/profile-images}")
  private String uploadDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String absolutePath = Path.of(uploadDir).toAbsolutePath().normalize().toUri().toString();
        registry
            .addResourceHandler("/content/profile-images/**")
            .addResourceLocations(absolutePath);
    }
}
