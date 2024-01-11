package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.AxialCoords;
import model.ReadonlyReversi;
import model.Tile;

/**
 * Strategy class that avoids making moves on tiles that border corner pieces. If no such moves
 * exist, it defers to CaptureMostPieces to choose the move.
 */
public class AvoidCellsNextToCorners extends Strategy {
  @Override
  public Optional<AxialCoords> chooseMove(ReadonlyReversi model, Tile turn) {
    int sideLen = model.getSideLen();
    int boardLen = sideLen + sideLen - 1;
    int s = sideLen - 1;
    int b = boardLen - 1;
    List<int[]> cornerCells = getCornerCells(s, b);
    List<int[]> nextToCornerCells = getNextToCornerCells(model, cornerCells);
    return getMove(model, sideLen, boardLen, nextToCornerCells);
  }

  private static Optional<AxialCoords> getMove(ReadonlyReversi model, int sideLen, int boardLen,
                                     List<int[]> nextToCornerCells) {
    List<int[]> availableMoves = new ArrayList<>();
    for (int i = 0; i < boardLen; i++) {
      for (int j = 0; j < boardLen; j++) {
        boolean isNextToCornerCells = false;
        for (int[] cell : nextToCornerCells) {
          if (cell[0] == i && cell[1] == j) {
            isNextToCornerCells = true;
            break;
          }
        }
        if (!isNextToCornerCells && model.isLegalMove(i, j)) {
          availableMoves.add(new int[] {i, j});
        }
      }
    }
    if (!availableMoves.isEmpty()) {
      int[] coords = getMinDistanceMove(model, availableMoves);
      return Optional.of(AxialCoords.convert(coords[0], coords[1], sideLen));
    }
    return Optional.empty();
  }

  private static List<int[]> getNextToCornerCells(ReadonlyReversi model, List<int[]> cornerCells) {
    List<int[]> nextToCornerCells = new ArrayList<>();
    for (int[] cell : cornerCells) {
      List<int[]> neighbors = model.getNeighbors(cell[0], cell[1]);
      nextToCornerCells.addAll(neighbors);
    }
    return nextToCornerCells;
  }

  private static List<int[]> getCornerCells(int s, int b) {
    List<int[]> cornerCells = new ArrayList<>();
    cornerCells.add(new int[] {0, s});
    cornerCells.add(new int[] {0, b});
    cornerCells.add(new int[] {s, 0});
    cornerCells.add(new int[] {s, b});
    cornerCells.add(new int[] {0, b});
    cornerCells.add(new int[] {b, s});
    return cornerCells;
  }
}
