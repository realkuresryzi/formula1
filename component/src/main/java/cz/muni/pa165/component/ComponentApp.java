package cz.muni.pa165.component;

import cz.muni.pa165.common_library.exception.RestExceptionHandler;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.common_library.security.SecurityFilter;
import cz.muni.pa165.common_library.user.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;


/**
 * Main app.
 */
@SpringBootApplication()
@ComponentScan(basePackages = {"cz.muni.pa165.component",
    "cz.muni.pa165.common_library.security"})
@EntityScan("cz.muni.pa165.component.data.model")
@Import({RestExceptionHandler.class, CommonDbGetter.class})
public class ComponentApp {

  public static void main(String[] args) {
    SpringApplication.run(ComponentApp.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Autowired
  ApiKeyAuthManager apiKeyAuthManager;

  /**
   * Configuration of Spring Security.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    final SecurityFilter filter = new SecurityFilter();
    filter.setAuthenticationManager(apiKeyAuthManager);
    httpSecurity
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(filter)
        .authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/component/*").hasAnyAuthority(
            Role.USER.getValue(), Role.ENGINEER.getValue(),
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.PATCH, "/component/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue(), Role.ENGINEER.getValue())
        .requestMatchers(HttpMethod.POST, "/component/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue(), Role.ENGINEER.getValue())
        .requestMatchers(HttpMethod.DELETE, "/component/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue(), Role.ENGINEER.getValue())
        .anyRequest().permitAll();

    return httpSecurity.build();
  }

}
