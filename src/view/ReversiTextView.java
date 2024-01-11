package view;

/**
 * Represents the primary text view interface for playing a game of Reversi.
 */
public interface ReversiTextView {

  /**
   * Creates a textual representation of the Reversi game board.
   * @return a String containing the current state of the Reversi game board.
   */
  String render();
}
