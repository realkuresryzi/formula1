package cz.muni.pa165.authentication.service;

import cz.muni.pa165.authentication.api.ApiKeyDto;
import cz.muni.pa165.authentication.data.model.ApiKey;
import cz.muni.pa165.authentication.data.repository.ApiKeyRepository;
import cz.muni.pa165.common_library.exception.DatabaseException;
import cz.muni.pa165.common_library.user.Role;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * Implementation of api key service interface.
 */
@Service
public class ApiKeyServiceImpl implements ApiKeyService {

  private static final String ADMIN_SCOPE = "test_4";
  private static final String MANAGER_SCOPE = "test_3";
  private static final String USER_SCOPE = "test_1";
  private static final String ENGINEER_SCOPE = "test_2";

  @Value("${formula.authenticate.api-key}")
  private String validApiKey;

  @Autowired
  ApiKeyRepository apiKeyRepository;

  @Override
  public ApiKeyDto createApiKey(OAuth2AuthorizedClient client, OidcUser user) {

    ApiKey apiKey = new ApiKey();
    if (client == null) {
      throw new BadCredentialsException("No token provided");
    }
    var generatedApiKey = ApiKeyUtil.generateApiKey(client.getAccessToken().getTokenValue());
    apiKey.setApiKeyValue(ApiKeyUtil.encryptApiKey(generatedApiKey));
    if (apiKeyRepository.findByKey(apiKey.getApiKeyValue()) != null) {
      throw new DatabaseException("Only one api key per session!");
    }

    var scopes = client.getAccessToken().getScopes();

    if (scopes.contains("email") && user != null && user.getEmail() != null) {
      apiKey.setEmail(user.getEmail());
    }
    grantRoles(scopes, apiKey);
    var newLocalDate = LocalDate.now();
    newLocalDate = scopes.contains(MANAGER_SCOPE) || scopes.contains(ENGINEER_SCOPE)
        ? newLocalDate.plusDays(30) : newLocalDate.plusDays(7);

    apiKey.setExpirationDate(Date.from(newLocalDate.atStartOfDay(
        ZoneId.systemDefault()).toInstant()));
    var savedApiKey = apiKeyRepository.save(apiKey);
    savedApiKey.setApiKeyValue(ApiKeyUtil.decryptApiKey(savedApiKey.getApiKeyValue()));
    return convertApiKey(savedApiKey);
  }

  @Override
  public Set<Role> isAuthenticated(String apiKey) {

    if (apiKey.equals(validApiKey)) {
      return Set.of(Role.ADMIN, Role.MANAGER, Role.ENGINEER, Role.USER);
    }

    var foundApiKey = apiKeyRepository.findByKey(ApiKeyUtil.encryptApiKey(apiKey));
    if (foundApiKey != null) {
      if (foundApiKey.getExpirationDate().getTime() < new Date().getTime()) {
        return Collections.emptySet();
      }
      return foundApiKey.getRoles();
    }
    return Collections.emptySet();
  }

  @Override
  public String delete(String apiKey, OAuth2AuthorizedClient client) {
    if (client == null || client.getAccessToken() == null
        || !client.getAccessToken().getScopes().contains(ADMIN_SCOPE)) {
      throw new BadCredentialsException("UnAuthorized access");
    }
    var foundApiKey = apiKeyRepository.findByKey(ApiKeyUtil.encryptApiKey(apiKey));
    apiKeyRepository.delete(foundApiKey);
    return "OK";
  }

  @Override
  public String deleteAll(OAuth2AuthorizedClient client) {
    if (client == null || client.getAccessToken() == null
        || !client.getAccessToken().getScopes().contains(ADMIN_SCOPE)) {
      throw new BadCredentialsException("UnAthorized access");
    }
    apiKeyRepository.deleteAll();
    return "OK";
  }

  @Override
  public List<ApiKeyDto> getAll(OAuth2AuthorizedClient client) {
    if (client == null || client.getAccessToken() == null
        || !client.getAccessToken().getScopes().contains(ADMIN_SCOPE)) {
      throw new BadCredentialsException("UnAthorized access");
    }
    var apiKeys = apiKeyRepository.findAll();
    return apiKeys.stream().map(x -> {
      x.setApiKeyValue(
          ApiKeyUtil.decryptApiKey(x.getApiKeyValue()));
      return x;
    }).map(this::convertApiKey).toList();
  }

  @Override
  public ApiKeyDto setRoles(String apiKey, Set<Role> roles, OAuth2AuthorizedClient client) {
    if (client == null || client.getAccessToken() == null
        || !client.getAccessToken().getScopes().contains(ADMIN_SCOPE)) {
      throw new BadCredentialsException("UnAuthorized access");
    }
    var foundApiKey = apiKeyRepository.findByKey(ApiKeyUtil.encryptApiKey(apiKey));
    if (foundApiKey != null) {
      foundApiKey.getRoles().addAll(roles);
      var savedApiKey = apiKeyRepository.save(foundApiKey);
      savedApiKey.setApiKeyValue(ApiKeyUtil.decryptApiKey(savedApiKey.getApiKeyValue()));
      return convertApiKey(savedApiKey);
    }
    throw new DatabaseException("Api key does not exist!");
  }

  @Override
  public Set<String> findAllManagers() {
    var apiKeys = apiKeyRepository.findAll();
    return apiKeys.stream().filter(x -> x.getRoles().contains(Role.MANAGER)).map(ApiKey::getEmail)
        .collect(
            Collectors.toSet());
  }

  private ApiKeyDto convertApiKey(ApiKey apiKey) {
    return ApiKeyDto.builder()
        .id(apiKey.getId())
        .expirationDate(apiKey.getExpirationDate())
        .roles(apiKey.getRoles())
        .email(apiKey.getEmail())
        .apiKeyValue(apiKey.getApiKeyValue())
        .build();
  }

  private void grantRoles(Set<String> scopes, ApiKey apiKey) {
    if (scopes.contains(USER_SCOPE)) {
      apiKey.getRoles().add(Role.USER);
    }
    if (scopes.contains(ENGINEER_SCOPE)) {
      apiKey.getRoles().add(Role.ENGINEER);
    }
    if (scopes.contains(MANAGER_SCOPE)) {
      apiKey.getRoles().add(Role.MANAGER);
    }
    if (scopes.contains(ADMIN_SCOPE)) {
      apiKey.getRoles().add(Role.ADMIN);
    }
  }
}
