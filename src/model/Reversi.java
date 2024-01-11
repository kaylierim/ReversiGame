package model;

/**
 * Represents the mutable model interface for playing a game of Reversi.
 */
public interface Reversi extends ReadonlyReversi {

  /**
   * Starts a Reversi game with all the starting pieces. Black plays first.
   *
   * @throws IllegalStateException if the game has already started.
   */
  void startGame() throws IllegalStateException;

  /**
   * Changes the turn to the opposing player.
   *
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  void pass() throws IllegalStateException;

  /**
   * Makes a move at the specified row and column in the Reversi game only if it is valid.
   *
   * @param row the 0-based index (from the top) row of the desired tile.
   * @param col the 0-based index (from the left) column of the desired tile.
   * @throws IllegalStateException if the game hasn't been started yet.
   * @throws IllegalArgumentException if the coordinates are out of bounds.
   */
  void makeMove(int row, int col) throws IllegalStateException, IllegalArgumentException;
}