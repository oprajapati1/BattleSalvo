package cs3500.pa04.Json;


import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.Model.Coord;
import java.util.List;

/**
 * Represents a volley message in JSON format.
 */
public record VolleyJson(
    @JsonProperty("coordinates") List<CoordJson> coords){
  /**
   * Gets the coordinates of the volley.
   * @return the coordinates of the volley
   */
  public List<CoordJson> getCoords() {
    return coords;
  }
}