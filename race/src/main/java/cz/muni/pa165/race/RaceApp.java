package cz.muni.pa165.race;


import cz.muni.pa165.common_library.client.ClientConfig;
import cz.muni.pa165.common_library.exception.RestExceptionHandler;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.common_library.security.SecurityFilter;
import cz.muni.pa165.common_library.user.Role;
import cz.muni.pa165.race.data.model.Race;
import cz.muni.pa165.race.data.model.Season;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main app.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cz.muni.pa165.race",
    "cz.muni.pa165.common_library.security"})
@EnableWebSecurity
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Race.class, Season.class})
@Import({RestExceptionHandler.class, ClientConfig.class, CommonDbGetter.class})
public class RaceApp {

  public static void main(String[] args) {
    SpringApplication.run(RaceApp.class, args);
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
        .requestMatchers(HttpMethod.GET, "/race/*").hasAnyAuthority(
            Role.USER.getValue(), Role.ENGINEER.getValue(),
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.PATCH, "/race/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.POST, "/race/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.DELETE, "/race/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())

        .requestMatchers(HttpMethod.GET, "/season/*").hasAnyAuthority(
            Role.USER.getValue(), Role.ENGINEER.getValue(),
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.PATCH, "/season/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.POST, "/season/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .requestMatchers(HttpMethod.DELETE, "/season/*").hasAnyAuthority(
            Role.MANAGER.getValue(), Role.ADMIN.getValue())
        .anyRequest().permitAll();

    return httpSecurity.build();
  }
}
