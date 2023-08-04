package abstr.challenge.faraway.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PeriodDTO {

    private LocalDate date;

    private String periodType;
}
