package model;

/**
 * Enum representing the possible tile states in a Reversi game: BLACK, WHITE, and EMPTY.
 */
public enum Tile {
  /**
   * The BLACK tile, represented as "X".
   */
  BLACK("X"),

  /**
   * The WHITE tile, represented as "O".
   */
  WHITE("O"),

  /**
   * An EMPTY tile, represented as "_".
   */
  EMPTY("_");

  private final String name;

  /**
   * Constructs a Tile with the specified name.
   *
   * @param name The string representation of the tile.
   */
  Tile(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * Returns the opposing player's tile color.
   */
  public Tile getOpposite() {
    if (this.equals(Tile.BLACK)) {
      return Tile.WHITE;
    } else {
      return Tile.BLACK;
    }
  }
}
