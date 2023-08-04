package abstr.challenge.faraway.schedule;


import abstr.challenge.faraway.dto.PlanetDTO;
import abstr.challenge.faraway.model.Period;
import abstr.challenge.faraway.model.PeriodType;
import abstr.challenge.faraway.model.Planet;
import abstr.challenge.faraway.service.PeriodService;
import abstr.challenge.faraway.service.PlanetService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PeriodCalculatorJob {

    private final PeriodService periodService;
    private final PlanetService planetService;

    @Value("${faraway.job.years-to-calculate-forward}")
    private int yearsToCalculateForward;

    @Value("${faraway.job.batch-size}")
    private int batchSize;

    private static final String DEFAULT_TZ_ID = "America/Argentina/Buenos_Aires";

    /**
     * for this job, we iterate in batches of batchSize through the dates to calculate between today and yearsToCalculateForward years
     * ahead. If the date was calculated already (maybe because someone requested it through the endpoint) we skip it
     */
    @Scheduled(cron = "${cron.period-calculator.expression}", zone = DEFAULT_TZ_ID)
    @SchedulerLock(name = "periodCalculatorJob")
    public void calculatedPeriods() {
        LocalDate dateToCalculate = LocalDate.now();
        int daysPendingToCalculate = yearsToCalculateForward * 365;

        List<Planet> allPlanets = planetService.getAllPlanets();
        List<PlanetDTO> planetDTOs = planetService.mapToPlanetDTOs(allPlanets);
        while(daysPendingToCalculate > 0){
            List<Period> periodsCalculated = new ArrayList<>();
            List<LocalDate> datesAlreadyCalculated = periodService.findPeriodsBetweenDates(dateToCalculate, dateToCalculate.plusDays(batchSize))
                    .stream().map(Period::getDate).collect(Collectors.toList());
            for(int x = 0; x < batchSize && daysPendingToCalculate > 0; x++){
                dateToCalculate = dateToCalculate.plusDays(1);
                if(!datesAlreadyCalculated.contains(dateToCalculate)){
                    planetService.setPlanetDTOsPositionInRadsForDate(planetDTOs, dateToCalculate);
                    PeriodType periodType = periodService.getPeriodType(planetDTOs);
                    Period period = periodService.generatePeriod(periodType, dateToCalculate);
                    periodsCalculated.add(period);
                }
                daysPendingToCalculate--;
            }
            periodService.saveAll(periodsCalculated);
        }
    }

    @Scheduled(initialDelay = 1000 * 10, fixedDelay=Long.MAX_VALUE)
    @SchedulerLock(name = "periodCalculatorJob")
    public void runAtStartup(){
        calculatedPeriods();
    }
}
