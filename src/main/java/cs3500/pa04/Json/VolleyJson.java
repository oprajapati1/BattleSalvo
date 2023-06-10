package cs3500.pa04.Json;


import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.Model.Coord;
import java.util.List;

public record VolleyJson(
    @JsonProperty("coordinates") List<CoordJson> coords){
  public List<CoordJson> getCoords() {
    return coords;
  }
}