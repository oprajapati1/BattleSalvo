package cs3500.pa04.Json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.Model.Coord;
import cs3500.pa03.Model.GameResult;
import cs3500.pa03.Model.Player;
import cs3500.pa03.Model.Position;
import cs3500.pa03.Model.Ship;
import cs3500.pa03.Model.ShipType;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProxyDealer {
  Socket server;
  Player player;
  private final InputStream in;
  private final PrintStream out;
  private final ObjectMapper mapper = new ObjectMapper();
  private static final JsonNode VOID_RESPONSE = new ObjectMapper().getNodeFactory().textNode("void");

  public ProxyDealer(Socket server, Player player) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
  }

  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      handleJoin(arguments);
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots(arguments);
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    }
    else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      handleEndGame(arguments);
    }
    else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  private void handleJoin(JsonNode arguments) {
    JoinJson args = new JoinJson("yashkumar530", "SINGLE");
    JsonNode joinArg = JsonUtils.serializeRecord(args);
    MessageJson joinGame = new MessageJson("join", joinArg);
    JsonNode jsonOutput = JsonUtils.serializeRecord(joinGame);
    this.out.println(jsonOutput);
  }

  //TODO: FIX SETUP
  private void handleSetup(JsonNode arguments) {
    SetUp args = this.mapper.convertValue(arguments, SetUp.class);

    int height = arguments.get("height").asInt();
    int width = arguments.get("width").asInt();

    Map<ShipType, Integer> specifications = args.fleetSpecs();
    List<Ship> fleet = player.setup(height, width, specifications);

    List<ShipJson> fleetJson = new ArrayList<>();

    for(Ship ship : fleet)  {
      Coord start = ship.getCoordinates().get(0);
      int shipSize = ship.getCoordinates().size();
      Position position = ship.getPosition();

      CoordJson coordJson = new CoordJson(start.getX(), start.getY());
      ShipJson shipJson = new ShipJson(coordJson, shipSize, position);

      fleetJson.add(shipJson);
    }
    FleetJson fleetOutput = new FleetJson(fleetJson);
    JsonNode fleetJsonOutput = JsonUtils.serializeRecord(fleetOutput);
    MessageJson setup = new MessageJson("setup", fleetJsonOutput);
    JsonNode setupOutput = JsonUtils.serializeRecord(setup);
    this.out.println(setupOutput);
  }

  private void handleTakeShots(JsonNode arguments) {
    List<Coord> shots = player.takeShots();
    List<CoordJson> shotsJson = new ArrayList<>();

    for (Coord c: shots ) {
      CoordJson coordJson = new CoordJson(c.getX(), c.getY());
      shotsJson.add(coordJson);
    }

    VolleyJson volley = new VolleyJson(shotsJson);
    JsonNode shotOutput = JsonUtils.serializeRecord(volley);
    MessageJson takeShots = new MessageJson("take-shots", shotOutput);
    JsonNode takeShotsOutput = JsonUtils.serializeRecord(takeShots);
    this.out.println(takeShotsOutput);
  }

  private void handleReportDamage(JsonNode arguments) {
    VolleyJson givenShots = mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> shots = new ArrayList<>();

    for(CoordJson cj : givenShots.getCoords()) {
      shots.add(new Coord(cj.getX(), cj.getY()));
    }

    List<Coord> hits = player.reportDamage(shots);

    List<CoordJson> hitsJson = new ArrayList<>();
    for(Coord c : hits) {
      CoordJson coordJson = new CoordJson(c.getX(), c.getY());
      hitsJson.add(coordJson);
    }
    VolleyJson volley = new VolleyJson(hitsJson);
    JsonNode volleyOutput = JsonUtils.serializeRecord(volley);

    MessageJson damageReport = new MessageJson("report-damage", volleyOutput);
    JsonNode damageReportOutput = JsonUtils.serializeRecord(damageReport);
    this.out.println(damageReportOutput);
  }

  //TODO:Successful hits needs to be fixed cuz we completley changed the way the method is supposed to work
  private void handleSuccessfulHits(JsonNode arguments) {
    List<Coord> shotsThatHitOpponentShips = new ArrayList<>();
    for (JsonNode shot : arguments.get("shots")) {
      shotsThatHitOpponentShips.add(new Coord(shot.get("x").asInt(), shot.get("y").asInt()));
    }
    player.successfulHits(shotsThatHitOpponentShips);
  }

  //Is this correct?
  private void handleEndGame(JsonNode arguments) {
    GameResult result = GameResult.valueOf(arguments.get("result").asText());
    String reason = arguments.get("reason").asText();
    player.endGame(result, reason);
  }
}
