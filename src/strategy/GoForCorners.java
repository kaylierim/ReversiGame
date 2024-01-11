package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.AxialCoords;
import model.ReadonlyReversi;
import model.Tile;

/**
 * Strategy class that chooses a corner move if one exists, otherwise it defers to
 * AvoidCellsNextToCorners to choose the move.
 */
public class GoForCorners extends Strategy {
  @Override
  public Optional<AxialCoords> chooseMove(ReadonlyReversi model, Tile turn) {
    int sideLen = model.getSideLen();
    int boardLen = sideLen + sideLen - 1;

    List<int[]> cornerMoves = new ArrayList<>();

    for (int i = 0; i < boardLen; i++) {
      for (int j = 0; j < boardLen; j++) {
        if (model.isLegalMove(i, j)) {
          if (isCorner(i, j, sideLen, boardLen)) {
            cornerMoves.add(new int[] { i, j });
          }
        }
      }
    }
    if (cornerMoves.isEmpty()) {
      return Optional.empty();
    }
    int[] minDistanceMove = Strategy.getMinDistanceMove(model, cornerMoves);
    return Optional.of(AxialCoords.convert(minDistanceMove[0], minDistanceMove[1],
            model.getSideLen()));
  }

  private boolean isCorner(int i, int j, int sideLen, int boardLen) {
    return (i == 0 && j == sideLen - 1) || (i == 0 && j == boardLen - 1)
            || (i == sideLen - 1 && j == boardLen - 1) || (i == boardLen - 1 && j == sideLen - 1)
            || (i == boardLen - 1 && j == 0) || (i == sideLen - 1 && j == 0);
  }
}
