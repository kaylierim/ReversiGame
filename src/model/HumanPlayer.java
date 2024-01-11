package model;

/**
 * Represents a human player in a Reversi game.
 */
public class HumanPlayer implements Player {
  private final Tile color;

  public HumanPlayer(Tile color) {
    this.color = color;
  }

  @Override
  public Tile getTileColor() {
    return color;
  }
}
