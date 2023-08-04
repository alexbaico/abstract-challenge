package abstr.challenge.faraway.service;

import abstr.challenge.faraway.dto.PlanetDTO;
import abstr.challenge.faraway.model.PeriodType;
import abstr.challenge.faraway.repository.PeriodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PeriodServiceTest {

    @InjectMocks
    private PeriodService periodService;

    @Mock
    private PlanetService planetService;

    @Mock
    private PeriodRepository periodRepository;

    @Test
    void testMaxRainyPeriodType(){
        PlanetDTO p1 = PlanetDTO.builder()
                .angularSpeed(1)
                .distanceToSun(500)
                .positionInRads(0)
                .build();
        PlanetDTO p2 = PlanetDTO.builder()
                .angularSpeed(3)
                .distanceToSun(1000)
                .positionInRads(180)
                .build();
        PlanetDTO p3 = PlanetDTO.builder()
                .angularSpeed(5)
                .distanceToSun(2000)
                .positionInRads(90)
                .build();

        ReflectionTestUtils.setField(periodService, "MAX_TRIANGE_PERIMETER", 5797.62079030862);

        PeriodType periodType = periodService.getPeriodType(List.of(p1,p2,p3));

        Assertions.assertEquals(PeriodType.RAINY_MAX, periodType);
    }

    @Test
    void testRainyPeriodType(){
        PlanetDTO p1 = PlanetDTO.builder()
                .angularSpeed(1)
                .distanceToSun(500)
                .positionInRads(50)
                .build();
        PlanetDTO p2 = PlanetDTO.builder()
                .angularSpeed(3)
                .distanceToSun(1000)
                .positionInRads(140)
                .build();
        PlanetDTO p3 = PlanetDTO.builder()
                .angularSpeed(5)
                .distanceToSun(2000)
                .positionInRads(270)
                .build();

        PeriodType periodType = periodService.getPeriodType(List.of(p1,p2,p3));

        Assertions.assertEquals(PeriodType.RAINY, periodType);
    }

    @Test
    void testOptimalPressureTemperaturePeriodType(){
        PlanetDTO p1 = PlanetDTO.builder()
                .angularSpeed(1)
                .distanceToSun(500)
                .positionInRads(45)
                .build();
        PlanetDTO p2 = PlanetDTO.builder()
                .angularSpeed(3)
                .distanceToSun(1000)
                .positionInRads(143)
                .build();
        PlanetDTO p3 = PlanetDTO.builder()
                .angularSpeed(5)
                .distanceToSun(2000)
                .positionInRads(0)
                .build();

        PeriodType periodType = periodService.getPeriodType(List.of(p1,p2,p3));

        Assertions.assertEquals(PeriodType.OPTIMAL_PRESSURE_TEMPERATURE, periodType);
    }

    @Test
    void testDroughtPeriodType(){
        PlanetDTO p1 = PlanetDTO.builder()
                .angularSpeed(1)
                .distanceToSun(500)
                .positionInRads(45)
                .build();
        PlanetDTO p2 = PlanetDTO.builder()
                .angularSpeed(3)
                .distanceToSun(1000)
                .positionInRads(45)
                .build();
        PlanetDTO p3 = PlanetDTO.builder()
                .angularSpeed(5)
                .distanceToSun(2000)
                .positionInRads(45)
                .build();

        PeriodType periodType = periodService.getPeriodType(List.of(p1,p2,p3));

        Assertions.assertEquals(PeriodType.DROUGHT, periodType);
    }

    @Test
    void testNormalPeriodType(){
        PlanetDTO p1 = PlanetDTO.builder()
                .angularSpeed(1)
                .distanceToSun(500)
                .positionInRads(95)
                .build();
        PlanetDTO p2 = PlanetDTO.builder()
                .angularSpeed(3)
                .distanceToSun(1000)
                .positionInRads(130)
                .build();
        PlanetDTO p3 = PlanetDTO.builder()
                .angularSpeed(5)
                .distanceToSun(2000)
                .positionInRads(160)
                .build();

        PeriodType periodType = periodService.getPeriodType(List.of(p1,p2,p3));

        Assertions.assertEquals(PeriodType.NORMAL, periodType);
    }

}
