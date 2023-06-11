package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.Model.Ship;
import java.util.List;

/**
 * Represents a fleet in JSON format.
 */
public record FleetJson(
  @JsonProperty("fleet") List<ShipJson> ships) {
  }

