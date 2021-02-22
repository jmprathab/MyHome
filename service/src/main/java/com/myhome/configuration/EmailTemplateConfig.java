package com.myhome.configuration;

import com.myhome.configuration.properties.mail.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class EmailTemplateConfig {

  private final MailProperties mailProperties;

  @Bean
  public ITemplateResolver thymeleafTemplateResolver() {
    MailProperties.MailTemplate templatesProperties = mailProperties.getTemplate();

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    String templatePath = templatesProperties.getPath();
    String fileSeparator = System.getProperty("file.separator");
    templateResolver.setPrefix(templatePath.endsWith(fileSeparator) ? templatePath : templatePath + fileSeparator);

    templateResolver.setSuffix(templatesProperties.getFormat());
    templateResolver.setTemplateMode(templatesProperties.getMode());
    templateResolver.setCharacterEncoding(templatesProperties.getEncoding());
    templateResolver.setCacheable(templatesProperties.isCache());
    return templateResolver;
  }

  @Bean
  public ResourceBundleMessageSource emailMessageSource() {
    MailProperties.MailTemplatesLocalization mailLocalization = mailProperties.getLocalization();

    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename(mailLocalization.getPath());
    messageSource.setDefaultLocale(Locale.ENGLISH);
    messageSource.setDefaultEncoding(mailLocalization.getEncoding());
    messageSource.setCacheSeconds(mailLocalization.getCacheSeconds());
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
