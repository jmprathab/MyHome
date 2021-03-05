package com.myhome.configuration.properties.mail;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email.location")
public class EmailTemplateLocalizationProperties {
  private String path;
  private String encoding;
  private int cacheSeconds;
}
