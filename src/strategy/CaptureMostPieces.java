package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.AxialCoords;
import model.ReadonlyReversi;
import model.Tile;

/**
 * Strategy class that chooses the move that captures the most pieces. If no moves are available,
 * an IllegalStateException is thrown.
 */
public class CaptureMostPieces extends Strategy {
  @Override
  public Optional<AxialCoords> chooseMove(ReadonlyReversi model, Tile turn) {
    int largestScore = Integer.MIN_VALUE;
    int boardLen = model.getSideLen() + model.getSideLen() - 1;

    List<int[]> movesWithLargestScore = new ArrayList<>();

    for (int i = 0; i < boardLen; i++) {
      for (int j = 0; j < boardLen; j++) {
        if (model.isLegalMove(i, j)) {
          int score = model.getScoreOfMove(i, j, turn);
          // if largest score so far save the coordinates of the turn and
          // delete any previously saved coordinates
          if (score > largestScore) {
            largestScore = score;
            movesWithLargestScore = new ArrayList<>();
            movesWithLargestScore.add(new int[]{i, j});
            // if equal to the largest score so far save the coordinates
          } else if (score == largestScore) {
            movesWithLargestScore.add(new int[]{i, j});
          }
        }
      }
    }

    if (movesWithLargestScore.isEmpty()) {
      return Optional.empty();
    }
    int[] minDistanceMove = Strategy.getMinDistanceMove(model, movesWithLargestScore);
    return Optional.of(AxialCoords.convert(minDistanceMove[0], minDistanceMove[1],
            model.getSideLen()));
  }
}
