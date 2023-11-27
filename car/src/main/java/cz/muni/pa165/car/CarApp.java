package cz.muni.pa165.car;

import cz.muni.pa165.common_library.exception.RestExceptionHandler;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.common_library.security.SecurityFilter;
import cz.muni.pa165.common_library.user.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
@SpringBootApplication
@Transactional
@ComponentScan(basePackages = {"cz.muni.pa165.car",
    "cz.muni.pa165.common_library.security"})
@EntityScan(basePackages = {"cz.muni.pa165.car.data.model"})
@Import({RestExceptionHandler.class, CommonDbGetter.class})
public class CarApp {

  private static final Logger log = LoggerFactory.getLogger(CarApp.class);

  public static void main(String[] args) {
    SpringApplication.run(CarApp.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Autowired
  ApiKeyAuthManager apiKeyAuthManager;

  /**
   * Security filter chain for roles.
   *
   * @param httpSecurity httpsecurity
   * @return filter chain
   * @throws Exception when error occurs
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
        .requestMatchers(HttpMethod.GET, "/car/*").hasAnyAuthority(
            Role.USER.getValue(), Role.ENGINEER.getValue(),
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.PATCH, "/car/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.POST, "/car/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.DELETE, "/car/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .anyRequest().permitAll();

    return httpSecurity.build();
  }

  /**
   * Handler called when OIDC login successfully completes.
   * It extends the default SavedRequestAwareAuthenticationSuccessHandler
   * that saves the access tokento the session.
   * This handler just prints the available info about user to the log
   * and calls its parent implementation.
   *
   * @see SavedRequestAwareAuthenticationSuccessHandler
   */
  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new SavedRequestAwareAuthenticationSuccessHandler() {
      @Override
      public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                          Authentication auth) throws
          ServletException, IOException {
        if (auth instanceof OAuth2AuthenticationToken token
            && token.getPrincipal() instanceof OidcUser user) {
          log.debug("********************************************************");
          log.debug("* user successfully logged in                          *");
          log.debug("********************************************************");
          log.info("user.issuer: {}", user.getIssuer());
          log.info("user.subject: {}", user.getSubject());
          log.info("user.fullName: {}", user.getFullName());
          log.info("user.givenName: {}", user.getGivenName());
          log.info("user.familyName: {}", user.getFamilyName());
          log.info("user.email: {}", user.getEmail());
        }
        super.onAuthenticationSuccess(req, res, auth);
      }
    };
  }


}
