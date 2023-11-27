package cz.muni.pa165.common_library.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Manager that checks if the api key in header has roles needed for the authentication.
 */
@Component
public class ApiKeyAuthManager implements AuthenticationManager {

  private final RestTemplate client = new RestTemplate();

  @Value("${server.services.authentication.port}")
  private String authenticationPort;

  @Value("${server.services.authentication.host}")
  private String authenticationHost;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    final String apiKey = (String) authentication.getPrincipal();

    String url = "http://" + authenticationHost + ":" + authenticationPort + "/authenticate/apikey?apiKey="
        + apiKey;

    try {
      var res = client.getForEntity(url, Set.class);
      Set<String> roles = res.getBody();

      List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
      for (String role : roles) {
        authorities.add(new SimpleGrantedAuthority(role));
      }
      return new UsernamePasswordAuthenticationToken(
          authentication.getPrincipal(), authentication.getCredentials(), authorities);

    } catch (BadCredentialsException exception) {
      return new UsernamePasswordAuthenticationToken(
          authentication.getPrincipal(), authentication.getCredentials(), Collections.emptyList());
    }
  }
}
