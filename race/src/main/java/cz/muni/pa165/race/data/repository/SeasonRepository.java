package cz.muni.pa165.race.data.repository;

import cz.muni.pa165.race.data.model.Season;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for data retrieval.
 */
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {}
