package testConfig;

import com.myhome.configuration.properties.mail.MailProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MailTestConfig {

  @Bean
  public MailProperties mailProperties(){
    MailProperties testMailProperties = new MailProperties();

    testMailProperties.setHost("test host");
    testMailProperties.setUsername("test username");
    testMailProperties.setPassword("test password");
    testMailProperties.setPort(0);
    testMailProperties.setProtocol("test protocol");
    testMailProperties.setDebug(false);
    testMailProperties.setDevMode(false);
    testMailProperties.setTestConnection(false);

    MailProperties.MailTemplate testMailTemplate = new MailProperties.MailTemplate();
    testMailTemplate.setPath("test path");
    testMailTemplate.setEncoding("test encoding");
    testMailTemplate.setMode("test mode");
    testMailTemplate.setCache(false);

    MailProperties.MailTemplatesLocalization testTemplatesLocalization = new MailProperties.MailTemplatesLocalization();
    testTemplatesLocalization.setPath("test path");
    testTemplatesLocalization.setEncoding("test encodig");
    testTemplatesLocalization.setCacheSeconds(0);

    MailProperties.MailTemplatesNames testMailTemplatesNames = new MailProperties.MailTemplatesNames();
    testMailTemplatesNames.setPasswordChanged("test account confirmed");
    testMailTemplatesNames.setPasswordReset("test password reset");
    testMailTemplatesNames.setAccountCreated("test account created");
    testMailTemplatesNames.setAccountConfirmed("test account confirmed");

    testMailProperties.setTemplate(testMailTemplate);
    testMailProperties.setTemplateNames(testMailTemplatesNames);
    testMailProperties.setLocalization(testTemplatesLocalization);

    return testMailProperties;
  }
}