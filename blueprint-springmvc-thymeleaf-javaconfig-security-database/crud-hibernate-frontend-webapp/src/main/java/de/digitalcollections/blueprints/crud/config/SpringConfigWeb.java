package de.digitalcollections.blueprints.crud.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import de.digitalcollections.blueprints.crud.frontend.webapp.converter.GrantedAuthorityJsonFilter;
import de.digitalcollections.blueprints.crud.frontend.webapp.converter.UserJsonFilter;
import de.digitalcollections.blueprints.crud.frontend.webapp.interceptors.CreateAdminUserInterceptor;
import de.digitalcollections.blueprints.crud.model.api.security.User;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@ComponentScan(basePackages = {
  "de.digitalcollections.blueprints.crud.frontend.webapp.aop",
  "de.digitalcollections.blueprints.crud.frontend.webapp.controller",
  "de.digitalcollections.blueprints.crud.frontend.webapp.propertyeditor"
})
@EnableAspectJAutoProxy
@EnableWebMvc
@PropertySource(value = {
  "classpath:de/digitalcollections/blueprints/crud/config/SpringConfigWeb-${spring.profiles.active:local}.properties"
})
public class SpringConfigWeb implements WebMvcConfigurer {

  @Autowired
  private ApplicationContext applicationContext;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Value("${cacheTemplates}")
  private boolean cacheTemplates;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    registry.addResourceHandler("/css/**").addResourceLocations("/css/");
    registry.addResourceHandler("/favicon.ico").addResourceLocations("/images/favicon.ico");
    registry.addResourceHandler("/html/**").addResourceLocations("/html/");
    registry.addResourceHandler("/images/**").addResourceLocations("/images/");
    registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
  }

  @Bean
  public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("/org/mdz/common/frontend/webapp/thymeleaf/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setCacheable(cacheTemplates);
    templateResolver.setOrder(2);
    return templateResolver;
  }

  private ITemplateResolver servletContextTemplateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setApplicationContext(applicationContext);
    resolver.setCacheable(cacheTemplates);
    resolver.setCharacterEncoding("UTF-8");
    resolver.setOrder(1);
    resolver.setPrefix("/WEB-INF/templates/");
    resolver.setSuffix(".html");
    resolver.setTemplateMode(TemplateMode.HTML);
    return resolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(classLoaderTemplateResolver());
    templateEngine.addTemplateResolver(servletContextTemplateResolver());
    // Activate Thymeleaf LayoutDialect[1] (for 'layout'-namespace)
    // [1] https://github.com/ultraq/thymeleaf-layout-dialect
    templateEngine.addDialect(new LayoutDialect());
    templateEngine.addDialect(new SpringSecurityDialect());
    templateEngine.addDialect(new DataAttributeDialect());
    return templateEngine;
  }

  @Bean
  public ViewResolver viewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    viewResolver.setOrder(1);

    return viewResolver;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("language");
    registry.addInterceptor(localeChangeInterceptor);

    InterceptorRegistration createAdminUserInterceptorRegistration = registry.
            addInterceptor(createAdminUserInterceptor());
    createAdminUserInterceptorRegistration.addPathPatterns("/login");
  }

  @Bean(name = "localeResolver")
  public LocaleResolver sessionLocaleResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.GERMAN);
    return localeResolver;
  }

  @Bean
  public CreateAdminUserInterceptor createAdminUserInterceptor() {
    return new CreateAdminUserInterceptor();
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // support for @ResponseBody of type String
    final StringHttpMessageConverter stringHMC = new StringHttpMessageConverter(Charset.forName("UTF-8"));
    // supported MediaTypes for stringHMC are by default set to: "text/plain" and MediaType.ALL
    converters.add(stringHMC);

    // support for @ResponseBody of type Object: convert object to JSON
    // used in ApiController
    converters.add(mappingJackson2HttpMessageConverter());

    // support for @ResponseBody of type byte[]
    ByteArrayHttpMessageConverter bc = new ByteArrayHttpMessageConverter();
    List<MediaType> supported = new ArrayList<>();
    supported.add(MediaType.IMAGE_JPEG);
    bc.setSupportedMediaTypes(supported);
    converters.add(bc);
  }

  @Bean
  public HttpMessageConverter<?> mappingJackson2HttpMessageConverter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper());
    return converter;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    // do not serialize null values/objects
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // define which fields schould be ignored with Filter-classes:
    objectMapper.addMixIn(User.class, UserJsonFilter.class);
    objectMapper.addMixIn(GrantedAuthority.class, GrantedAuthorityJsonFilter.class);
    return objectMapper;
  }
}
