package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoordJson(
    @JsonProperty("X") int x,
    @JsonProperty("Y") int y) {
  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }
}
