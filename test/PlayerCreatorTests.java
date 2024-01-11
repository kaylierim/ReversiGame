import org.junit.Assert;
import org.junit.Test;

import model.AIPlayer;
import model.HumanPlayer;
import model.Reversi;
import model.PlayerCreator;
import model.ReversiModel;
import model.Tile;

/**
 * Tests the PlayerCreator class.
 */
public class PlayerCreatorTests {
  @Test
  public void testCreator() {
    PlayerCreator creator = new PlayerCreator();
    Reversi model = new ReversiModel(6);

    HumanPlayer humanExpected = new HumanPlayer(Tile.BLACK);
    HumanPlayer humanActual = (HumanPlayer) creator.create(PlayerCreator.PlayerType.Human,
            model, Tile.BLACK);
    Assert.assertEquals(humanExpected.getTileColor(), humanActual.getTileColor());

    AIPlayer aiExpected = new AIPlayer(model, Tile.WHITE);
    AIPlayer aiActual = (AIPlayer) creator.create(PlayerCreator.PlayerType.AI, model, Tile.WHITE);
    Assert.assertEquals(aiExpected.getTileColor(), aiActual.getTileColor());
  }
}
