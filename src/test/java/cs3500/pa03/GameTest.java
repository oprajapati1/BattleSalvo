package cs3500.pa03;

import cs3500.pa03.Controller.Game;
import cs3500.pa03.Model.AIPlayer;
import cs3500.pa03.Model.Board;
import cs3500.pa03.Model.Coord;
import cs3500.pa03.Model.HumanPlayer;
import cs3500.pa03.Model.Ship;
import cs3500.pa03.Model.ShipType;
import cs3500.pa03.View.View;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for the Game class.
 */
public class GameTest {
  @Mock
  View view;

  @Mock
  HumanPlayer humanPlayer;

  @Mock
  AIPlayer aiPlayer;

  private Game game;

  @BeforeEach
  void setUp() {
    game = new Game(view, humanPlayer, aiPlayer);
    this.humanPlayer.playerBoard = new Board();
    this.aiPlayer.playerBoard = new Board();
  }

  void testStart_GameOverWithHumanWin() throws IOException {
    List<Ship> humanFleet = Arrays.asList(
        new Ship(ShipType.CARRIER, Arrays.asList(new Coord(0, 0), new Coord(0, 1),
            new Coord(0, 2), new Coord(0, 3), new Coord(0, 4))),
        new Ship(ShipType.BATTLESHIP, Arrays.asList(new Coord(1, 0), new Coord(1, 1),
            new Coord(1, 2), new Coord(1, 3))),
        new Ship(ShipType.DESTROYER, Arrays.asList(new Coord(2, 0), new Coord(2, 1),
            new Coord(2, 2))),
        new Ship(ShipType.SUBMARINE, Arrays.asList(new Coord(3, 0), new Coord(3, 1),
            new Coord(3, 2)))
    );
    List<Ship> aiFleet = Arrays.asList(
        new Ship(ShipType.CARRIER, Arrays.asList(new Coord(0, 0), new Coord(0, 1),
            new Coord(0, 2), new Coord(0, 3), new Coord(0, 4))),
        new Ship(ShipType.BATTLESHIP, Arrays.asList(new Coord(1, 0), new Coord(1, 1),
            new Coord(1, 2), new Coord(1, 3))),
        new Ship(ShipType.DESTROYER, Arrays.asList(new Coord(2, 0), new Coord(2, 1),
            new Coord(2, 2))),
        new Ship(ShipType.SUBMARINE, Arrays.asList(new Coord(3, 0), new Coord(3, 1),
            new Coord(3, 2)))
    );

    when(view.getBoardDimension("height")).thenReturn(10);
    when(view.getBoardDimension("width")).thenReturn(10);
    when(humanPlayer.setup(anyInt(), anyInt(), any())).thenReturn(humanFleet);
    when(aiPlayer.setup(anyInt(), anyInt(), any())).thenReturn(aiFleet);
    when(humanPlayer.takeShots()).thenReturn(Arrays.asList(new Coord(0, 0),
        new Coord(1, 0), new Coord(2, 0), new Coord(3, 0)));
    when(aiPlayer.takeShots()).thenReturn(Collections.emptyList());
    when(humanPlayer.reportDamage(Collections.emptyList())).thenReturn(Collections.emptyList());
    when(aiPlayer.reportDamage(Arrays.asList(new Coord(0, 0), new Coord(1, 0), new Coord(2, 0),
        new Coord(3, 0)))).thenReturn(Arrays.asList(new Coord(0, 0), new Coord(1, 0), new Coord(2, 0), new Coord(3, 0)));
    when(humanPlayer.playerBoard.allShots).thenReturn(Arrays.asList(new Coord(0, 0), new Coord(1, 0),
        new Coord(2, 0), new Coord(3, 0)));
    when(aiPlayer.playerBoard.allShots).thenReturn(Arrays.asList(new Coord(0, 0), new Coord(1, 0),
        new Coord(2, 0), new Coord(3, 0)));
    when(humanPlayer.playerBoard.allShips).thenReturn(humanFleet);
    when(aiPlayer.playerBoard.allShips).thenReturn(aiFleet);
    when(aiPlayer.playerBoard.getBoardData()).thenReturn(new char[10][10]);
    when(view.displayBoard(any(), any())).thenReturn(new char[10][10]);

    game.start();

    verify(humanPlayer).endGame(any(), any());
    verify(aiPlayer).endGame(any(), any());
    verify(view, atLeastOnce()).displaySuccessfulHits(any(), any());
  }
}