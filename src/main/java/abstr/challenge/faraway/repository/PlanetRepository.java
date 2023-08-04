package abstr.challenge.faraway.repository;

import abstr.challenge.faraway.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
}
