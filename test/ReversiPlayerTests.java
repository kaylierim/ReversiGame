import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import model.AIPlayer;
import model.AxialCoords;
import model.HumanPlayer;
import model.Reversi;
import model.ReversiModel;
import model.Tile;

/**
 * Tests the ReversiPlayer class.
 */
public class ReversiPlayerTests {
  Reversi model = new ReversiModel(6);
  HumanPlayer player1 = new HumanPlayer(Tile.BLACK);
  AIPlayer player2 = new AIPlayer(model, Tile.WHITE);

  @Test
  public void testHumanPlayer() {
    Assert.assertEquals(Tile.BLACK, player1.getTileColor());
  }

  @Test
  public void testAIPlayer() {
    Assert.assertEquals(Tile.WHITE, player2.getTileColor());
    model.startGame();
    model.makeMove(4, 7);
    Assert.assertEquals(Optional.of(new AxialCoords(1, -2)), player2.chooseNextMove());
  }
}
