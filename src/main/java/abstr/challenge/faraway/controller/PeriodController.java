package abstr.challenge.faraway.controller;

import abstr.challenge.faraway.dto.PeriodDTO;
import abstr.challenge.faraway.service.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PeriodController {

    private final PeriodService periodService;

    @GetMapping("/period/{periodDate}")
    public ResponseEntity<PeriodDTO> getPeriodTypeForDate(@PathVariable String periodDate){
        PeriodDTO period = periodService.getPeriodTypeForDate(periodDate);
        return ResponseEntity.ok(period);
    }

}
