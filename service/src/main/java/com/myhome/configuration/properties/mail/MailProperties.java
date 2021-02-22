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

  private MailTemplatesLocalization localization = new MailTemplatesLocalization();
  private MailTemplatesNames templateNames = new MailTemplatesNames();
  private MailTemplate template = new MailTemplate();

  @Data
  public static class MailTemplate {
    private String path;
    private String format;
    private String encoding;
    private String mode;
    private boolean cache;
  }

  @Data
  public static class MailTemplatesLocalization {
    private String path;
    private String encoding;
    private int cacheSeconds;
  }

  @Data
  public static class MailTemplatesNames {
    private String passwordChanged;
    private String passwordReset;
    private String accountCreated;
    private String accountConfirmed;
  }
}

