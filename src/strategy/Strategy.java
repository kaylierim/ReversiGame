package strategy;

import java.util.List;

import model.ReadonlyReversi;

/**
 * Abstract class of a Reversi strategy that implements FallibleReversiStrategy.
 */
public abstract class Strategy implements FallibleReversiStrategy {
  /**
   * Returns the move closest to the top left of the hexagonal board.
   * @param model Reversi model
   * @param moves List of coordinates to check.
   * @return Coordinates as a row and column pair.
   */
  protected static int[] getMinDistanceMove(ReadonlyReversi model, List<int[]> moves) {
    double minDistance = Integer.MAX_VALUE;
    int[] minDistanceMove = new int[2];
    // go through all the saved coordinates and determine which one is closest to the top left
    for (int[] move : moves) {
      double distance = Math.hypot(-move[0], (model.getSideLen() - 1) - move[1]);
      if (distance < minDistance) {
        minDistance = distance;
        minDistanceMove = new int[] { move[0], move[1] };
      }
    }
    return minDistanceMove;
  }
}
