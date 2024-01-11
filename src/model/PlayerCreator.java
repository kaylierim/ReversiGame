package model;

/**
 * Factory class of a Reversi player.
 */
public class PlayerCreator {
  /**
   * Enum class of all possible game types.
   */
  public enum PlayerType {
    // need strategy and player enum
    Human, AI
  }

  /**
   * Creates a Player based on the given PlayerType.
   */
  public static Player create(PlayerType type, Reversi model, Tile color) {
    switch (type) {
      case Human:
        return new HumanPlayer(color);
      case AI:
        return new AIPlayer(model, color);
      default:
        return null;
    }
  }
}
