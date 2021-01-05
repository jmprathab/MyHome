package com.myhome.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Locale;

@Configuration
public class EmailTemplateConfig {

  @Value("${spring.mail.templates.path}")
  private String mailTemplatesPath;
  @Value("${spring.mail.templates.format}")
  private String mailTemplatesFormat;
  @Value("${spring.mail.templates.encoding}")
  private String mailTemplatesEncoding;
  @Value("${spring.mail.templates.mode}")
  private String mailTemplatesMode;
  @Value("${spring.mail.templates.cache}")
  private boolean mailTemplatesCache;

  @Value("${spring.mail.templates.localization.path}")
  private String localizationFilesPath;
  @Value("${spring.mail.templates.localization.encoding}")
  private String localizationFilesEncoding;
  @Value("${spring.mail.templates.localization.cache-seconds}")
  private int localizationFilesCacheSeconds;

  @Bean
  public ITemplateResolver thymeleafTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix(mailTemplatesPath.endsWith("/") ? mailTemplatesPath : mailTemplatesPath + "/");
    templateResolver.setSuffix(mailTemplatesFormat);
    templateResolver.setTemplateMode(mailTemplatesMode);
    templateResolver.setCharacterEncoding(mailTemplatesEncoding);
    templateResolver.setCacheable(mailTemplatesCache);
    return templateResolver;
  }

  @Bean
  public ResourceBundleMessageSource emailMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename(localizationFilesPath);
    messageSource.setDefaultLocale(Locale.ENGLISH);
    messageSource.setDefaultEncoding(localizationFilesEncoding);
    messageSource.setCacheSeconds(localizationFilesCacheSeconds);
    return messageSource;
  }

  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine(ResourceBundleMessageSource emailMessageSource, ITemplateResolver thymeleafTemplateResolver) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(thymeleafTemplateResolver);
    templateEngine.setTemplateEngineMessageSource(emailMessageSource);
    return templateEngine;
  }

}
