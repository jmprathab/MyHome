package com.myhome.configuration.properties.mail;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email.template")
public class EmailTemplateProperties {
  private String path;
  private String format;
  private String encoding;
  private String mode;
  private boolean cache;
}
