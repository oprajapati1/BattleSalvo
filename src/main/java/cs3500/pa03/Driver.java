package cs3500.pa03;

import cs3500.pa03.Controller.Game;
import cs3500.pa03.Model.AIPlayer;
import cs3500.pa03.Model.Board;
import cs3500.pa03.Model.HumanPlayer;
import cs3500.pa03.Model.Player;
import cs3500.pa03.View.View;
import java.io.IOException;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) throws IOException {
    View view = new View();
    HumanPlayer humanPlayer = new HumanPlayer("Your", view);
    AIPlayer aiPlayer = new AIPlayer("Opponent", view);
    Game game = new Game(view, humanPlayer, aiPlayer);
    game.start();
  }
}