package strategy;

import java.util.Optional;

import model.AxialCoords;
import model.ReadonlyReversi;
import model.Tile;

/**
 * Interface that represents a fallible strategy for the Reversi game.
 */
public interface FallibleReversiStrategy {
  /**
   * Chooses the next move to be played in a Reversi game based on a strategy.
   * If there are multiple best moves, the uppermost-leftmost
   * coordinate in the visual board is returned.
   *
   * @param model the current state of the Reversi game.
   * @param turn  the player's turn.
   * @return the coordinates of the chosen move represented by AxialCoord,
   *         if there is no move available, it returns an empty Optional.
   */
  Optional<AxialCoords> chooseMove(ReadonlyReversi model, Tile turn);
}
