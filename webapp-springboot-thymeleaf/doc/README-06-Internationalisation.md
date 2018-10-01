# Internationalisation (i18n) ("multilanguage GUI")

Our webapp now works as expected, but the GUI is hard coded only in english language.

## Adding translations

The `main.html` template contains the static label `Time` which we want to provide now in multi languages.

In our blueprint we look up the key `time` referenced in the Thymeleaf template with `#{time}`:

```html
<p>
  <label th:text="#{time}">Time</label>: <span th:text="${time}">...</span>
</p>
```

More about Thymeleaf and internationalisation [here](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#using-thtext-and-externalizing-text).

Spring looks up the tranlsations by key in so called "messages"-properties files.
The files by default have to be located in `src/main/resources` and be named `messages_<language>.properties`.
Values (=translations) are looked up by the language independent key (`time`in our example).

See <https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-functionality-messagesource>.
There are several configuration options available:

```ini
# INTERNATIONALIZATION (MessageSourceProperties)
spring.messages.always-use-message-format=false # Whether to always apply the MessageFormat rules, parsing even messages without arguments.
spring.messages.basename=messages # Comma-separated list of basenames (essentially a fully-qualified classpath location), each following the ResourceBundle convention with relaxed support for slash based locations.
spring.messages.cache-duration= # Loaded resource bundle files cache duration. When not set, bundles are cached forever. If a duration suffix is not specified, seconds will be used.
spring.messages.encoding=UTF-8 # Message bundles encoding.
spring.messages.fallback-to-system-locale=true # Whether to fall back to the system Locale if no files for a specific Locale have been found.
spring.messages.use-code-as-default-message=false # Whether to use the message code as the default message instead of throwing a "NoSuchMessageException". Recommended during development only.
```

We rely on the defaults, except for `spring.messages.fallback-to-system-locale`.
This variable needs to be changed to `false`, because a fallback to the default
language (in our example to `messages.properties`) won't work when the system
locale is set to German. Thus, the following property needs to be added to
`application.yml`:

```yaml
spring:
  messages:
    fallback-to-system-locale: false
```

We do not provide one properties file per template but use the above mentioned global messages-properties files.
We have to provide a default file `messages.properties` containing translations of a default language (we chose english translation...),
therefore we skip adding a `messages_en.properties`as this is the default.
And we add an additional file `messages_de.properties` containing german translations:

File `src/main/resources/messages.properties`:

```ini
time=Current time
```

File `src/main/resources/messages_de.properties`:

```ini
time=Aktuelle Zeit
```

After a restart of the application the label is now rendered in the language that corresponds to your browser language (or the default if for your language no messages-file exists).

## Adding a language switcher

Client (browser) specific language detection is fine, but not always satisfying. Think about an english user using a browser at a chinese airport,
getting chinese GUI instead english one...

So we add a language switcher gui component, following this documentation <https://www.baeldung.com/spring-boot-internationalization>.

"In order for our application to be able to determine which locale is currently being used, we need to add a LocaleResolver bean."

We add a new Spring configuration class `de.digitalcollections.blueprints.webapp.springboot.config.SpringConfigWeb` (name can freely be chosen, but we name it so as it is web related),
containing the Spring bean for LocaleResolver:

```java
package de.digitalcollections.blueprints.webapp.springboot.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class SpringConfigWeb {

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(Locale.US);
    return sessionLocaleResolver;
  }
}
```

"The LocaleResolver interface has implementations that determine the current locale based on the session, cookies, the Accept-Language header, or a fixed value.

In our example, we have used the session based resolver SessionLocaleResolver and set a default locale with value US."

"Next, we need to add an interceptor bean that will switch to a new locale based on the value of the lang parameter appended to a request:"

```java
@Configuration
public class SpringConfigWeb {

  ...

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    return localeChangeInterceptor;
  }
}
```

"In order to take effect, this bean needs to be added to the application’s interceptor registry.

To achieve this, our @Configuration class has to implement the WebMvcConfigurer interface and override the addInterceptors() method:"

```java
...
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfigWeb implements WebMvcConfigurer {

  ...

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
```

Finally we add language switch links to our `base.html`webpage skeleton, so that the user can choose a GUI language by clicking on the language link.
There were already two links in `base.html` that we now change to:

```html
...
<div id="navbar" class="collapse navbar-collapse">
  <ul class="nav navbar-nav">
    <li class="active"><a href="#">Home</a></li>
    <li><a th:href="@{''(lang=de)}">de</a> | <a th:href="@{''(lang=en)}">en</a></li>
  </ul>
</div>
...
```

Start the webapp and click on "de" and "en" links on top. You should see the "time"-label switch between the languages.
