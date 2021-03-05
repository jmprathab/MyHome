package com.myhome.configuration.properties.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
  private String host;
  private String username;
  private String password;
  private int port;
  private String protocol;
  private boolean debug;
  private boolean devMode;
}

