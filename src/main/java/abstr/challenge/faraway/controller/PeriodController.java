package abstr.challenge.faraway.controller;

import abstr.challenge.faraway.model.PeriodType;
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
    public ResponseEntity<PeriodType> getPeriodTypeForDate(@PathVariable String periodDate){
        PeriodType periodType = periodService.getPeriodTypeForDate(periodDate);
        return ResponseEntity.ok(periodType);
    }

}
