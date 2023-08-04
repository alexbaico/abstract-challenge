package abstr.challenge.faraway.service;

import abstr.challenge.faraway.dto.PeriodDTO;
import abstr.challenge.faraway.dto.PlanetDTO;
import abstr.challenge.faraway.model.Period;
import abstr.challenge.faraway.model.PeriodType;
import abstr.challenge.faraway.model.Planet;
import abstr.challenge.faraway.repository.PeriodRepository;
import abstr.challenge.faraway.util.MathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodService {

    //This is the day we take as a reference or starting point to calculate the periods,
    //at this date all planets are aligned in the 0 degrees, and the rest are calculated from here
    public static LocalDate DAY_ZERO;

    private static double MAX_TRIANGE_PERIMETER;

    private final PlanetService planetService;
    private final PeriodRepository periodRepository;

    public PeriodType getPeriodType(List<PlanetDTO> planets){
        if(dayIsDrought(planets)){
            return PeriodType.DROUGHT;
        } else if (dayIsOptimalPressureTemperature(planets)){
            return PeriodType.OPTIMAL_PRESSURE_TEMPERATURE;
        }
        boolean sunIsInTheMiddle = MathUtil.sunIsInTheMiddle(planets);
        if (dayIsRainyMax(sunIsInTheMiddle, planets)){
            return PeriodType.RAINY_MAX;
        } else if (sunIsInTheMiddle){
            return PeriodType.RAINY;
        }
        return PeriodType.NORMAL;
    }

    private boolean dayIsDrought(List<PlanetDTO> planets){
        int positionInRads = planets.get(0).getPositionInRads();
        return planets.stream().allMatch(planetDTO -> planetDTO.getPositionInRads() == positionInRads ||
                planetDTO.getPositionInRads() == ((positionInRads + 180) % 360));
    }

    private boolean dayIsOptimalPressureTemperature(List<PlanetDTO> planets){
        return MathUtil.planetsAreAlignedBetweenThem(planets);
    }

    private boolean dayIsRainyMax(boolean sunIsInTheMiddle, List<PlanetDTO> planets){
        return sunIsInTheMiddle &&
                MAX_TRIANGE_PERIMETER == MathUtil.actualTrianglePerimeter(planets);
    }


    @Value("${faraway.day-zero}")
    public void setDayZero(String dayZeroParam){
        PeriodService.DAY_ZERO = LocalDate.parse(dayZeroParam, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void saveAll(List<Period> periods) {
        periodRepository.saveAll(periods);
    }

    public Period generatePeriod(PeriodType periodType, LocalDate calculatedDate) {
        Period period = new Period();
        period.setPeriodType(periodType);
        period.setDate(calculatedDate);
        return period;
    }

    public PeriodDTO getPeriodTypeForDate(String periodDateString) {
        LocalDate periodDate = LocalDate.parse(periodDateString, DateTimeFormatter.ISO_LOCAL_DATE);
        Period period = periodRepository.findFirstByDate(periodDate)
                .orElseGet(() -> calculateAndSavePeriodForDate(periodDate));
        return mapToPeriodDTO(period);
    }

    private PeriodDTO mapToPeriodDTO(Period period) {
        return PeriodDTO.builder()
                .date(period.getDate())
                .periodType(period.getPeriodType().name())
                .build();
    }

    private Period calculateAndSavePeriodForDate(LocalDate periodDate) {
        List<PlanetDTO> planetDTOs = planetService.mapToPlanetDTOs(planetService.getAllPlanets());
        planetService.setPlanetDTOsPositionInRadsForDate(planetDTOs, periodDate);
        PeriodType periodType = getPeriodType(planetDTOs);
        Period period = generatePeriod(periodType, periodDate);
        period = periodRepository.save(period);
        return period;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        List<Planet> allPlanets = planetService.getAllPlanets();
        List<PlanetDTO> planetDTOs = planetService.mapToPlanetDTOs(allPlanets);
        PlanetDTO p1 = planetDTOs.get(0);
        PlanetDTO p2 = planetDTOs.get(1);
        PlanetDTO p3 = planetDTOs.get(2);
        p1.setPositionInRads(0);
        p2.setPositionInRads(90);
        p3.setPositionInRads(180);
        PeriodService.MAX_TRIANGE_PERIMETER = MathUtil.actualTrianglePerimeter(planetDTOs);
    }

    public List<Period> findPeriodsBetweenDates(LocalDate from, LocalDate to) {
        return periodRepository.findByDateBetween(from, to);
    }
}
