package model;

import java.util.List;

/**
 * Represents the read-only model interface for a game of Reversi.
 */
public interface ReadonlyReversi {
  /**
   * Signal if the game is over or not. The game is over if pass() is
   * called twice in a row or if neither player has any valid moves left.
   *
   * @return true if the game is over, false otherwise.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  boolean isGameOver() throws IllegalStateException;

  /**
   * Returns which player's turn it is.
   *
   * @return The current turn (BLACK or WHITE).
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  Tile getTurn() throws IllegalStateException;

  /**
   * Returns the tile at the specified coordinates.
   *
   * @param row the 0-based index (from the top) row of the desired tile.
   * @param col the 0-based index (from the left) column of the desired tile.
   * @return the tile at the given position (BLACK, WHITE, or EMPTY).
   * @throws IllegalStateException    if the game hasn't been started yet.
   * @throws IllegalArgumentException if the coordinates are out of bounds.
   */
  Tile getTileAt(int row, int col) throws IllegalStateException, IllegalArgumentException;

  /**
   * Returns the winner of this game.
   *
   * @return the color of the winner of the game (BLACK OR WHITE).
   * @throws IllegalStateException if the game hasn't been started yet,
   *                               game is not over, or there is no winner
   */
  Tile getWinner() throws IllegalStateException;

  /**
   * Returns the side length of the game board.
   *
   * @return the length of the side of the game board.
   */
  int getSideLen();

  /**
   * Determines if it is a legal move for the current player to play at the given coordinates.
   *
   * @return true if the move is legal, false if illegal.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  boolean isLegalMove(int row, int col);

  /**
   * Returns the score of the given player.
   *
   * @param player the player whose score is being returned.
   * @return the number of tiles possessed by the given player.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  int getScore(Tile player);

  /**
   * Determines if the current player can play a legal move.
   *
   * @return true if the current player can play a legal move, false otherwise.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  boolean currentPlayerHasLegalMove();

  /**
   * Returns the total number of pieces captured by a player's move.
   *
   * @param row the 0-based index (from the top) row of the desired tile.
   * @param col the 0-based index (from the left) column of the desired tile.
   * @param player who's turn it is
   * @return the number of pieces captured
   */
  int getScoreOfMove(int row, int col, Tile player);

  /**
   * Gets the coordinates of the neighbors of the given coordinates.
   * @param row the 0-based index (from the top) row of the desired tile.
   * @param col the 0-based index (from the left) column of the desired tile.
   * @return a list of corresponding neighbor coordinates
   */
  List<int[]> getNeighbors(int row, int col);

  /**
   * Returns a copy of the current Reversi game board.
   *
   * @return a copy of the current game board.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  Tile[][] copyGameBoard();
}
