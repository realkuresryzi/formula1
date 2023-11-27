package cz.muni.pa165.authentication;

import cz.muni.pa165.common_library.exception.RestExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * Main Authentication App.
 */
@SpringBootApplication
@EntityScan(basePackages = {"cz.muni.pa165.authentication.data.model"})
@Import({RestExceptionHandler.class})
public class App {

  private static final Logger log = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  /**
   * Security filter chain for roles.
   *
   * @param httpSecurity httpsecurity
   * @return filter chain
   * @throws Exception when error occurs
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf().disable()
        .authorizeHttpRequests(x -> x
            .requestMatchers("/error", "/robots.txt", "/style.css", "/favicon.ico",
                "/webjars/**", "/authenticate/apikey", "", "/authenticate/apikey/getManagerEmails")
            .permitAll()
            .requestMatchers("/authenticate/apikey/deleteAll", "/apikey/delete")
            .hasAnyAuthority("NOONE")
            .anyRequest().authenticated()
        ).oauth2Login(x -> x.successHandler(authenticationSuccessHandler()))
        .logout(x -> x.logoutSuccessUrl("/"));

    return httpSecurity.build();
  }

  /**
   * OIDC Authentication handler.
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
