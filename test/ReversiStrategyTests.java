import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import strategy.AvoidCellsNextToCorners;
import model.AxialCoords;
import strategy.CaptureMostPieces;
import strategy.GoForCorners;
import model.Reversi;
import model.ReversiModel;

/**
 * Tests strategies of Reversi plays.
 */
public class ReversiStrategyTests {

  Reversi intermediateGame = new ReversiModel(6);
  Reversi regGame = new ReversiModel(4);
  Reversi model4 = new ReversiModel(4);
  CaptureMostPieces cmp = new CaptureMostPieces();
  GoForCorners gfc = new GoForCorners();
  AvoidCellsNextToCorners avoidNTC = new AvoidCellsNextToCorners();

  @Before
  public void init() {
    regGame.startGame();
    intermediateGame.startGame();
    intermediateGame.makeMove(4, 7);
    intermediateGame.makeMove(3, 8);
    intermediateGame.makeMove(6, 3);
    intermediateGame.makeMove(6, 2);
    intermediateGame.makeMove(7, 4);
    intermediateGame.makeMove(8, 3);
    intermediateGame.makeMove(4, 8);
    intermediateGame.makeMove(4, 4);
    intermediateGame.makeMove(9, 2);
    intermediateGame.makeMove(4, 9);
    intermediateGame.makeMove(6, 1);
    intermediateGame.makeMove(8, 4);
    intermediateGame.makeMove(2, 9);
    intermediateGame.makeMove(10, 1);
    intermediateGame.makeMove(4, 10);
    intermediateGame.makeMove(6, 0);
    intermediateGame.makeMove(3, 6);
    intermediateGame.makeMove(2, 7);
    intermediateGame.makeMove(4, 3);
    intermediateGame.makeMove(3, 4);
    intermediateGame.makeMove(1, 8);
    intermediateGame.makeMove(0, 9);
    intermediateGame.makeMove(2, 6);
    intermediateGame.makeMove(1, 10);
    intermediateGame.makeMove(7, 2);
  }

  @Test
  public void testCaptureMostPieces() {
    Assert.assertEquals(Optional.of(AxialCoords.convert(8, 1, 6)),
            cmp.chooseMove(intermediateGame, intermediateGame.getTurn()));
    regGame.makeMove(1, 4);
    regGame.makeMove(2, 5);
    regGame.makeMove(4, 4);
    regGame.makeMove(5, 2);
    regGame.makeMove(4, 1);
    Assert.assertEquals(Optional.of(AxialCoords.convert(2, 2, 4)),
            cmp.chooseMove(regGame, regGame.getTurn()));
    regGame.makeMove(2, 2);
    regGame.makeMove(1, 6);
    regGame.makeMove(5, 4);
    regGame.makeMove(6, 1);
    // The below test asserts that the capture most
    // pieces strategy returns the top left most coordinate since there is more
    // than one move that captures the max number of tiles
    Assert.assertEquals(Optional.of(AxialCoords.convert(0, 4, 4)),
            cmp.chooseMove(regGame, regGame.getTurn()));
  }

  @Test
  public void testAvoidCellsNextToCorners() {
    Assert.assertEquals(Optional.of(AxialCoords.convert(1, 4, 4)),
            avoidNTC.chooseMove(regGame, regGame.getTurn()));
    regGame.makeMove(1, 4);
    Assert.assertEquals(Optional.of(AxialCoords.convert(2, 5, 4)),
            avoidNTC.chooseMove(regGame, regGame.getTurn()));
    regGame.makeMove(2, 5);
    // The below test asserts that the capture most
    // pieces strategy returns the top left most coordinate since there is more
    // than one move that captures the max number of tiles
    Assert.assertEquals(Optional.of(AxialCoords.convert(2, 2, 4)),
            avoidNTC.chooseMove(regGame, regGame.getTurn()));
    regGame.makeMove(2, 2);
    Assert.assertEquals(Optional.of(AxialCoords.convert(4, 1, 4)),
            avoidNTC.chooseMove(regGame, regGame.getTurn()));
  }

  @Test
  public void testGoForCorners() {
    model4.startGame();
    model4.makeMove(5, 2);
    model4.makeMove(6, 2);
    model4.makeMove(6, 1);
    // The below test asserts that the capture most
    // pieces strategy returns the top left most coordinate since there is more
    // than one move that captures the max number of tiles
    Assert.assertEquals(Optional.of(AxialCoords.convert(6, 0, 4)),
            gfc.chooseMove(model4, model4.getTurn()));
    model4.makeMove(6, 0);
    model4.makeMove(2, 2);
    model4.makeMove(1, 2);
    model4.makeMove(2, 1);
    Assert.assertEquals(Optional.of(AxialCoords.convert(3, 0, 4)),
            gfc.chooseMove(model4, model4.getTurn()));
  }
}
