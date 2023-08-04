package abstr.challenge.faraway.service;

import abstr.challenge.faraway.dto.PlanetDTO;
import abstr.challenge.faraway.model.Planet;
import abstr.challenge.faraway.repository.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanetService {

    private final PlanetRepository planetRepository;

    //it's usually not a good idea to have a findAll method that returns all registries from the db,
    //this is just for the purpose of the challenge, otherwise is recommended to not have a findAll or
    //to have a paginated method at least
    public List<Planet> getAllPlanets(){
        return planetRepository.findAll();
    }

    public List<PlanetDTO> mapToPlanetDTOs(List<Planet> planets) {
        return planets.stream().map(planet -> PlanetDTO.builder()
                .distanceToSun(planet.getDistanceToSun())
                .angularSpeed(planet.getAngularSpeed())
                .build()
        ).collect(Collectors.toList());
    }

    public void setPlanetDTOsPositionInRadsForDate(List<PlanetDTO> planetDTOs, LocalDate calculatedDate) {
        planetDTOs.forEach(planetDTO ->
                planetDTO.setPositionInRads(Long.valueOf(planetDTO.getAngularSpeed() *
                        PeriodService.DAY_ZERO.until(calculatedDate, ChronoUnit.DAYS) % 360)
                        .intValue())
        );
    }
}
