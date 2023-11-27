package cz.muni.pa165.driver.data.repository;

import cz.muni.pa165.driver.data.model.Driver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for driver.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}
