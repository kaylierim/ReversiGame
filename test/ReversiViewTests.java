import org.junit.Assert;
import org.junit.Test;

import model.ReversiModel;
import view.ReversiTextualView;

/**
 * Tests the ReversiModel class.
 */

public class ReversiViewTests {
  ReversiModel model = new ReversiModel(4);
  ReversiTextualView view = new ReversiTextualView(model);
  ReversiModel model5 = new ReversiModel(5);
  ReversiTextualView view5 = new ReversiTextualView(model5);

  @Test
  public void testCorrectStartingBoard() {
    model.startGame();
    Assert.assertEquals("   _ _ _ _\n" +
            "  _ _ _ _ _\n" +
            " _ _ X O _ _\n" +
            "_ _ O _ X _ _\n" +
            " _ _ X O _ _\n" +
            "  _ _ _ _ _\n" +
            "   _ _ _ _", view.render());
  }

  @Test
  public void testStartingBoardSize5() {
    model5.startGame();
    Assert.assertEquals("    _ _ _ _ _\n" +
            "   _ _ _ _ _ _\n" +
            "  _ _ _ _ _ _ _\n" +
            " _ _ _ X O _ _ _\n" +
            "_ _ _ O _ X _ _ _\n" +
            " _ _ _ X O _ _ _\n" +
            "  _ _ _ _ _ _ _\n" +
            "   _ _ _ _ _ _\n" +
            "    _ _ _ _ _", view5.render());
  }

  @Test
  public void testUpdatesWithMoves() {
    model.startGame();
    model.makeMove(1, 4);
    Assert.assertEquals("   _ _ _ _\n" +
            "  _ _ X _ _\n" +
            " _ _ X X _ _\n" +
            "_ _ O _ X _ _\n" +
            " _ _ X O _ _\n" +
            "  _ _ _ _ _\n" +
            "   _ _ _ _", view.render());
  }

  @Test
  public void testCorrectNumLines() {
    model.startGame();
    Assert.assertEquals(7, view.render().split("\n").length);

    model5.startGame();
    Assert.assertEquals(9, view5.render().split("\n").length);
  }
}
