package abstr.challenge.faraway.util;

import abstr.challenge.faraway.dto.PlanetDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MathUtil {

    public static double distanceBetweenTwoPlanets(PlanetDTO planet1, PlanetDTO planet2){
        return Math.sqrt(
                Math.pow(distanceOnXBetweenTwoPlanets(planet1, planet2), 2)
                        + Math.pow(distanceOnYBetweenTwoPlanets(planet1, planet2), 2)
        );
    }

    /**
     * //Sea d el segmento ab.
     * d = b - a;
     *
     * //Sea e el segmento ac.
     * e = c - a;
     *
     * //Variable de ponderación a~b
     * w1 = (e.x * (a.y - p.y) + e.y * (p.x - a.x)) / (d.x * e.y - d.y * e.x);
     *
     * //Variable de ponderación a~c
     * w2 = (p.y - a.y - w1 * d.y) / e.y;
     *
     * //El punto p se encuentra dentro del triángulo
     * //si se cumplen las 3 condiciones:
     * if ((w1 >= 0.0) && (w2 >= 0.0) && ((w1 + w2) <= 1.0))
     * {
     *     //Punto dentro del triángulo
     * }
     * else
     * {
     *      //Punto fuera del triángulo
     * }
     * @param planetDTOS
     * @return true if planet is in the middle or EXACTLY in the border of the triangle formed by the planets
     */

    public static boolean sunIsInTheMiddle(List<PlanetDTO> planetDTOS){
        PlanetDTO p0 = planetDTOS.get(0);
        PlanetDTO p1 = planetDTOS.get(1);
        PlanetDTO p2 = planetDTOS.get(2);
        double p1p0X = distanceOnXBetweenTwoPlanets(p0, p1); //Ex
        double p1p2X = distanceOnXBetweenTwoPlanets(p2, p1); //Dx
        double p1p0Y = distanceOnYBetweenTwoPlanets(p0, p1); //Ey
        double p1p2Y = distanceOnYBetweenTwoPlanets(p2, p1); //Dy

        //D = b - a; p2 - p1
        //E = c - a; p0 - p1
        double w1 = (p1p0X * (vertYLength(p1)) + p1p0Y * (0 - vertXLength(p1))) / (p1p2X * p1p0Y - p1p2Y * p1p0X);
        double w2 = (0 - vertYLength(p1) - w1 * p1p2Y) / p1p0Y;

        return Double.isNaN(w1) || Double.isNaN(w2) ||
                (w1 >= 0.0 && w2 >= 0.0 && (w1 + w2) <= 1.0);
    }

    /**
     * calculates the perimeter of the triangle formed by the planets, which is the adding of all the sides (distances
     * between planets)
     * @param planetDTOS
     * @return the perimeter of the triangle formed by the planets
     */
    public static double actualTrianglePerimeter(List<PlanetDTO> planetDTOS){
        return distanceBetweenTwoPlanets(planetDTOS.get(0), planetDTOS.get(1))
                + distanceBetweenTwoPlanets(planetDTOS.get(0), planetDTOS.get(2))
                + distanceBetweenTwoPlanets(planetDTOS.get(2), planetDTOS.get(1));
    }

    /**
     * x coordinate distance value between two planets
     * @param planet1
     * @param planet2
     * @return
     */
    public static double distanceOnXBetweenTwoPlanets(PlanetDTO planet1, PlanetDTO planet2){
        return vertXLength(planet1) - vertXLength(planet2);
    }

    /**
     * y coordinate distance value between two planets
     * @param planet1
     * @param planet2
     * @return
     */
    public static double distanceOnYBetweenTwoPlanets(PlanetDTO planet1, PlanetDTO planet2){
        return vertYLength(planet1) - vertYLength(planet2);
    }

    private static double vertXLength(PlanetDTO planet){
        return roundToTwoDecimals(Math.cos(Math.toRadians(planet.getPositionInRads())) * planet.getDistanceToSun());
    }

    private static double vertYLength(PlanetDTO planet){
        return roundToTwoDecimals(Math.sin(Math.toRadians(planet.getPositionInRads())) * planet.getDistanceToSun());
    }

    /**
     * if the proportion of the distances between two pairs of planets are equal, then they are in the same line
     * (for an x / y plane, proportion must be equals on both x and y axises)
     * @param planetDTOS
     * @return
     */
    public static boolean planetsAreAlignedBetweenThem(List<PlanetDTO> planetDTOS){
        PlanetDTO p0 = planetDTOS.get(0);
        PlanetDTO p1 = planetDTOS.get(1);
        PlanetDTO p2 = planetDTOS.get(2);
        return roundToTwoDecimals(distanceOnXBetweenTwoPlanets(p0, p1) / distanceOnXBetweenTwoPlanets(p1, p2)) ==
                roundToTwoDecimals(distanceOnYBetweenTwoPlanets(p0, p1) / distanceOnYBetweenTwoPlanets(p1, p2));
    }

    private static double roundToTwoDecimals(double value){
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
