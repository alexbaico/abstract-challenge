package abstr.challenge.faraway.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlanetDTO {

    private int positionInRads;

    private int angularSpeed;

    private int distanceToSun;
}
