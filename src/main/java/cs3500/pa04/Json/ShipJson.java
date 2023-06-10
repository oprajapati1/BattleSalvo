package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.Model.Coord;
import cs3500.pa03.Model.Position;

public record ShipJson(
  @JsonProperty("coord") CoordJson coord,
  @JsonProperty("length") int length,
  @JsonProperty("direction") Position position) {
}
