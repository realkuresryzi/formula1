package cz.muni.pa165.authentication.data.repository;

import cz.muni.pa165.authentication.data.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Api key repository.
 */
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

  @Query("SELECT a FROM ApiKey a "
      + "WHERE a.apiKeyValue = :apiKeyValue")
  ApiKey findByKey(@Param("apiKeyValue") String encryptedVal);
}
