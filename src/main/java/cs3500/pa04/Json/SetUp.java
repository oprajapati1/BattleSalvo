package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.Model.ShipType;
import java.util.Map;

public record SetUp(
    @JsonProperty("height") int height,
    @JsonProperty("width") int width,
    @JsonProperty("fleet") Map<ShipType, Integer> fleetSpecs) {
}
