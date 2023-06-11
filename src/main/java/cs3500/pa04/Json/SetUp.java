package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.Model.ShipType;
import java.util.Map;

/**
 * Represents a fleet in JSON format.
 */
public record SetUp(
    @JsonProperty("height") int height,
    @JsonProperty("width") int width,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> fleetSpecs) {
}
