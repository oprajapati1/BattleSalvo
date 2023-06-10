package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record MessageJson (
    @JsonProperty("method-name") String messageName,
    @JsonProperty("arguments") JsonNode arguments) {

}


