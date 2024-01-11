import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import controller.ReversiController;
import model.AIPlayer;
import model.AxialCoords;
import model.HumanPlayer;
import model.ModelFeatures;
import model.Player;
import model.Reversi;
import model.ReversiModel;
import model.Tile;
import view.ReversiGUIView;
import view.ReversiGraphics;
import view.ViewFeatures;

/**
 * Tests the ReversiController features implementations.
 */
public class ReversiFeaturesControllerTests {
  @Test
  public void testMakeMoveFeatures() {
    Reversi model = new ReversiModel(4);
    model.startGame();
    ReversiGUIView view = new ReversiGraphics(model);
    Player player = new HumanPlayer(Tile.BLACK);
    ViewFeatures features = new ReversiController(model, player, (ReversiGraphics) view);

    AxialCoords coords = AxialCoords.convert(1, 4, 4);
    // mocks interactions between controller and view
    features.makeMoveFeatures(coords.getQ(), coords.getR());
    // checks that model gets updated
    Assert.assertEquals(Tile.BLACK, model.getTileAt(1, 4));
  }

  @Test
  public void testPassFeatures() {
    Reversi model = new ReversiModel(4);
    model.startGame();
    ReversiGUIView view = new ReversiGraphics(model);
    Player player = new HumanPlayer(Tile.BLACK);
    ViewFeatures features = new ReversiController(model, player, (ReversiGraphics) view);

    features.passFeatures();
    Assert.assertEquals(Tile.WHITE, model.getTurn());
  }

  @Test
  public void testOutputToConsoleKey() {
    Reversi model = new ReversiModel(4);
    model.startGame();
    ReversiGUIView view = new ReversiGraphics(model);
    Player player = new HumanPlayer(Tile.BLACK);
    ViewFeatures features = new ReversiController(model, player, (ReversiGraphics) view);

    OutputStream os = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(os);
    System.setOut(ps);
    features.printToConsoleKey('m');
    String actualOutput = os.toString();
    Assert.assertEquals("A move was made\n", actualOutput);
  }

  @Test
  public void testOutputToConsoleClick() {
    Reversi model = new ReversiModel(4);
    model.startGame();
    ReversiGUIView view = new ReversiGraphics(model);
    Player player = new HumanPlayer(Tile.BLACK);
    ViewFeatures features = new ReversiController(model, player, (ReversiGraphics) view);

    OutputStream os = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(os);
    System.setOut(ps);
    features.printToConsoleClick(1, 4);
    String actualOutput = os.toString();
    Assert.assertEquals("Clicked Cell: (1, 4)\n", actualOutput);
  }

  @Test
  public void testModelFeatures() {
    Reversi model = new ReversiModel(4);
    model.startGame();
    ReversiGUIView view = new ReversiGraphics(model);
    Player player = new AIPlayer(model, Tile.BLACK);
    ModelFeatures features = new ReversiController(model, player, (ReversiGraphics) view);

    features.playerChanged(Tile.WHITE);
    Assert.assertEquals(Tile.WHITE, model.getTurn());
  }
}
