package abstr.challenge.faraway.repository;

import abstr.challenge.faraway.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PeriodRepository extends JpaRepository<Period, Long> {

    Optional<Period> findFirstByDate(LocalDate date);

    List<Period> findByDateBetween(LocalDate from, LocalDate to);
}
