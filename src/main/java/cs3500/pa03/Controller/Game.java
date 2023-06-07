package cs3500.pa03.Controller;

import cs3500.pa03.Model.AIPlayer;
import cs3500.pa03.Model.Coord;
import cs3500.pa03.Model.GameResult;
import cs3500.pa03.Model.HumanPlayer;
import cs3500.pa03.Model.Ship;
import cs3500.pa03.Model.ShipType;
import cs3500.pa03.View.View;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents a game of BattleSalvo.
 * The game manages the interaction between a human player and an AI player.
 */
public class Game {
  private final View view;
  private final HumanPlayer humanPlayer;
  private final AIPlayer aiPlayer;

  /**
   * Constructs a new Game object.
   *
   * @param view         the view object for displaying messages and boards
   * @param humanPlayer  the human player object
   * @param aiPlayer     the AI player object
   */
  public Game(View view, HumanPlayer humanPlayer, AIPlayer aiPlayer) {
    this.view = view;
    this.humanPlayer = humanPlayer;
    this.aiPlayer = aiPlayer;
  }

  /**
   * Starts the game of BattleSalvo.
   *
   * @throws IOException if there is an I/O error
   */
  public void start() throws IOException {
    view.displayMessage("Welcome to BattleSalvo!");

    int boardHeight = view.getBoardDimension("height");
    int boardWidth = view.getBoardDimension("width");

    Map<ShipType, Integer> fleetSpecifications =
        view.getFleetSpecifications(boardHeight, boardWidth);

    List<Ship> humanFleet = humanPlayer.setup(boardHeight, boardWidth, fleetSpecifications);
    List<Ship> aiFleet = aiPlayer.setup(boardHeight, boardWidth, fleetSpecifications);

    aiPlayer.boardHidden = new char[boardHeight][boardWidth];
    aiPlayer.boardHidden = aiPlayer.playerBoard.getBoardData();

    aiPlayer.playerBoard.allShips.addAll(aiFleet);

    aiPlayer.boardVisual =
        Arrays.copyOf(view.displayBoard(aiPlayer.playerBoard, aiPlayer.name()), boardHeight);
    humanPlayer.boardVisual =
        Arrays.copyOf(view.displayBoard(humanPlayer.playerBoard, humanPlayer.name()), boardHeight);

    view.displayMessage("The battle has begun . . .");

    while (true) {
      List<Coord> humanShots = humanPlayer.takeShots();
      List<Coord> aiShots = aiPlayer.takeShots();

      List<Coord> humanHits = aiPlayer.reportDamage(humanShots);
      List<Coord> aiHits = humanPlayer.reportDamage(aiShots);

      for (Coord c : humanShots) {
        if (humanHits.contains(c)) {
          aiPlayer.successfulHits(humanHits);
        } else {
          aiPlayer.boardHidden[c.getY()][c.getX()] = 'M';
        }
      }
      view.displaySuccessfulHits(aiPlayer.boardHidden, "Opponent");
      humanPlayer.successfulHits(aiHits);

      humanPlayer.updateIsSunk(aiPlayer.playerBoard.allShots);
      aiPlayer.updateIsSunk(humanPlayer.playerBoard.allShots);

      if (allShipsSunk(humanPlayer.playerBoard.allShips)) {
        humanPlayer.endGame(GameResult.LOSE, "All human ships sunk!");
        break;
      }

      if (allShipsSunk(aiPlayer.playerBoard.allShips)) {
        humanPlayer.endGame(GameResult.WIN, "All AI ships sunk!");
        break;
      }
    }
  }

  /**
   * Checks if all the ships in the fleet are sunk.
   *
   * @param fleet the list of ships in the fleet
   * @return true if all ships are sunk, false otherwise
   */
  private boolean allShipsSunk(List<Ship> fleet) {
    for (Ship ship : fleet) {
      if (!ship.isSunk()) {
        return false;
      }
    }
    return true;
  }
}