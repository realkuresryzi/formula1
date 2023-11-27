package cz.muni.pa165.component.data.repository;

import cz.muni.pa165.component.data.model.CarComponent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository of the car components.
 */
@Repository
public interface ComponentRepositoryInterface extends JpaRepository<CarComponent, Long> {}
