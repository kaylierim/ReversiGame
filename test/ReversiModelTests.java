import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import model.ReversiModel;
import model.Tile;

/**
 * Tests the ReversiModel class.
 */
public class ReversiModelTests {
  ReversiModel model = new ReversiModel(4);
  ReversiModel model3 = new ReversiModel(3);

  @Test
  public void testStartGame() {
    model.startGame();
    // checks that it starts with correct player
    Assert.assertEquals(model.getTurn(), Tile.BLACK);
    // checks that if the game has already been started an exception is thrown
    Assert.assertThrows(IllegalStateException.class, () -> model.startGame());
    // checks that the starting pieces are in the correct places
    Assert.assertEquals(model.getTileAt(2, 3), Tile.BLACK);
    Assert.assertEquals(model.getTileAt(2, 4), Tile.WHITE);
    Assert.assertEquals(model.getTileAt(3, 4), Tile.BLACK);
    Assert.assertEquals(model.getTileAt(4, 3), Tile.WHITE);
    Assert.assertEquals(model.getTileAt(4, 2), Tile.BLACK);
    Assert.assertEquals(model.getTileAt(3, 2), Tile.WHITE);
    // checks that other pieces are empty
    Assert.assertEquals(model.getTileAt(3, 5), Tile.EMPTY);
    Assert.assertEquals(model.getTileAt(0, 3), Tile.EMPTY);
    Assert.assertEquals(model.getTileAt(3, 3), Tile.EMPTY);
  }

  @Test
  public void testPass() {
    model.startGame();
    model.makeMove(1, 4);
    model.pass();
    Assert.assertEquals(model.getTurn(), Tile.BLACK);
    model.pass();
    Assert.assertThrows(IllegalStateException.class, () -> model.getTurn());
  }

  @Test
  public void testIsGameOver() {
    model.startGame();
    model.makeMove(1, 4);
    model.makeMove(2, 5);
    model.makeMove(4, 4);
    model.makeMove(5, 2);
    model.makeMove(4, 1);
    model.makeMove(2, 2);
    model.makeMove(1, 6);
    model.makeMove(5, 4);
    model.makeMove(6, 1);
    Assert.assertFalse(model.isGameOver());
    model.makeMove(0, 4);
    model.makeMove(2, 1);
    model.makeMove(6, 2);
    model.makeMove(0, 5);
    model.makeMove(6, 0);
    model.makeMove(4, 5);
    Assert.assertFalse(model.isGameOver());
    model.makeMove(1, 2);
    Assert.assertFalse(model.isGameOver());
    model.makeMove(0, 3);
    model.makeMove(3, 6);
    Assert.assertFalse(model.isGameOver());
    model.makeMove(4, 0);
    model.makeMove(5, 0);
    model.pass();
    model.makeMove(3, 0);
    model.makeMove(2, 6);
    Assert.assertFalse(model.isGameOver());
    model.makeMove(0, 6);
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(Tile.WHITE, model.getWinner());
  }

  @Test
  public void testValidMove() {
    model.startGame();
    model.makeMove(1, 4);
    model.makeMove(5, 2);
    model.makeMove(6, 1);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testIllegalMove() {
    model.startGame();
    // Illegal black moves:
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(4, 0));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(3, 3));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(2, 6));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(3, 1));

    // make some moves:
    model.makeMove(4, 4);
    model.makeMove(5, 4);
    model.makeMove(2, 5);

    // Illegal white moves:
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(3, 3));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(2, 6));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(0, 4));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(6, 3));
  }

  @Test
  public void testInvalidMoveAtNonEmptyTile() {
    model.startGame();
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(3, 2));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(2, 4));
    model.pass();
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(4, 2));
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(4, 3));
  }

  @Test
  public void testThrowsIfGameHasNotStarted() {
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(3, 2));
    Assert.assertThrows(IllegalStateException.class, () -> model.isGameOver());
    Assert.assertThrows(IllegalStateException.class, () -> model.getTurn());
    Assert.assertThrows(IllegalStateException.class, () -> model.pass());
  }

  @Test
  public void testGetTileAt() {
    model.startGame();
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getTileAt(0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getTileAt(-1, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getTileAt(0, 8));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getTileAt(3, 7));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getTileAt(7, 3));
  }

  @Test
  public void testOutOfBoundsMove() {
    model.startGame();
    Assert.assertThrows(IllegalArgumentException.class, () -> model.makeMove(0, 0));
  }

  @Test
  public void testGetSideLen() {
    model.startGame();
    Assert.assertEquals(4, model.getSideLen());

    ReversiModel model5 = new ReversiModel(5);
    Assert.assertEquals(5, model5.getSideLen());
  }

  @Test
  public void testConstructor() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new ReversiModel(2));
    Assert.assertThrows(IllegalArgumentException.class, () -> new ReversiModel(-1));
  }

  @Test
  public void testIsGameOverSize3() {
    model3.startGame();
    model3.makeMove(0, 3);
    Assert.assertFalse(model3.isGameOver());
    model3.makeMove(1, 4);
    model3.makeMove(3, 3);
    model3.makeMove(4, 1);
    model3.makeMove(3, 0);
    Assert.assertFalse(model3.isGameOver());
    model3.makeMove(1, 1);
    Assert.assertTrue(model3.isGameOver());
    Assert.assertEquals(Tile.WHITE, model3.getWinner());
  }

  @Test
  public void testBlackWins() {
    model3.startGame();
    model3.makeMove(0, 3);
    model3.makeMove(1, 4);
    model3.pass();
    model3.makeMove(4, 1);
    Assert.assertFalse(model3.isGameOver());
    model3.makeMove(3, 3);
    model3.makeMove(1, 1);
    model3.makeMove(3, 0);
    Assert.assertTrue(model3.isGameOver());
    Assert.assertEquals(Tile.BLACK, model3.getWinner());
  }

  @Test
  public void testGetWinner() {
    model.startGame();
    model.makeMove(2, 5);
    Assert.assertThrows(IllegalStateException.class, () -> model.getWinner());
  }

  @Test
  public void testPassTwiceInARow() {
    model.startGame();
    model.pass();
    model.pass();
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testPassTwiceInARowWithMoves() {
    model.startGame();
    model.makeMove(1, 4);
    model.pass();
    Assert.assertFalse(model.isGameOver());
    model.pass();
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testInvariant() {
    model.startGame();
    Assert.assertNotEquals(Tile.EMPTY, model.getTurn());
    model.makeMove(2, 5);
    Assert.assertNotEquals(Tile.EMPTY, model.getTurn());
    model.makeMove(5, 2);
    Assert.assertNotEquals(Tile.EMPTY, model.getTurn());
  }

  @Test
  public void testGetScore() {
    model3.startGame();
    model3.makeMove(0, 3);
    model3.makeMove(1, 4);
    model3.pass();
    model3.makeMove(4, 1);
    Assert.assertEquals(6, model3.getScore(Tile.WHITE));
    Assert.assertEquals(3, model3.getScore(Tile.BLACK));
    model3.makeMove(3, 3);
    model3.makeMove(1, 1);
    Assert.assertEquals(8, model3.getScore(Tile.WHITE));
    Assert.assertEquals(3, model3.getScore(Tile.BLACK));
  }

  @Test
  public void testCurrentPlayerHasLegalMove() {
    model.startGame();
    model.makeMove(1, 4);
    model.makeMove(2, 5);
    Assert.assertTrue(model.currentPlayerHasLegalMove());
    model.makeMove(4, 4);
    model.makeMove(5, 2);
    model.makeMove(4, 1);
    Assert.assertTrue(model.currentPlayerHasLegalMove());
    model.makeMove(2, 2);
    model.makeMove(1, 6);
    model.makeMove(5, 4);
    model.makeMove(6, 1);
    Assert.assertTrue(model.currentPlayerHasLegalMove());
  }

  @Test
  public void testGetScoreOfMove() {
    model3.startGame();
    model.startGame();
    model.makeMove(1, 4);
    model.makeMove(2, 5);
    Assert.assertEquals(2, model.getScoreOfMove(4, 4, Tile.BLACK));
    model.makeMove(4, 4);
    Assert.assertEquals(3, model.getScoreOfMove(5, 2, Tile.WHITE));
  }

  @Test
  public void testCopyGameBoard() {
    model3.startGame();
    model3.makeMove(0, 3);
    model3.makeMove(1, 4);
    model3.makeMove(3, 3);
    model3.makeMove(4, 1);
    model3.makeMove(3, 0);
    Tile[][] copy = model3.copyGameBoard();
    Assert.assertEquals(copy[3][0], Tile.BLACK);
  }

  @Test
  public void testGetNeighbors() {
    model.startGame();
    List<int[]> neighbors1 = model.getNeighbors(2, 2);
    Assert.assertEquals(6, neighbors1.size());

    List<int[]> neighbors4 = model.getNeighbors(10, 10);
    Assert.assertTrue(neighbors4.isEmpty());
  }

  @Test
  public void testIsLegalMove() {
    model3.startGame();
    Assert.assertTrue(model3.isLegalMove(0, 3));
    model3.makeMove(0, 3);
    Assert.assertFalse(model3.isLegalMove(0, 3));
    Assert.assertTrue(model3.isLegalMove(1, 4));
  }
}
