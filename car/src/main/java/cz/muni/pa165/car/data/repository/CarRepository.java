package cz.muni.pa165.car.data.repository;

import cz.muni.pa165.car.data.model.Car;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Car Repository for Driver Manager.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {}
