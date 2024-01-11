package model;

import java.util.Optional;

import strategy.AvoidCellsNextToCorners;
import strategy.CaptureMostPieces;
import strategy.GoForCorners;

/**
 * Represents an artificial intelligence (AI) player in a Reversi game.
 */
public class AIPlayer implements Player {

  private final ReadonlyReversi model;
  private final Tile color;

  public AIPlayer(ReadonlyReversi model, Tile color) {
    this.model = model;
    this.color = color;
  }

  @Override
  public Tile getTileColor() {
    return color;
  }

  /**
   * Relies on the implemented strategies to choose the next best move for this player.
   * If no move is returned, then the player does not currently have any legal moves or
   * the game is over.
   * @return Coordinates of the AI player's next move, or Optional.empty() if no moves exist.
   */
  public Optional<AxialCoords> chooseNextMove() {
    AvoidCellsNextToCorners strat1 = new AvoidCellsNextToCorners();
    GoForCorners strat2 = new GoForCorners();
    CaptureMostPieces strat3 = new CaptureMostPieces();

    if (strat1.chooseMove(model, color).isPresent()) {
      return strat1.chooseMove(model, color);
    } else if (strat2.chooseMove(model, color).isPresent()) {
      return strat2.chooseMove(model, color);
    } else if (strat3.chooseMove(model, color).isPresent()) {
      return strat3.chooseMove(model, color);
    } else {
      // in the controller if the move that is returned is empty then it does not have any
      // more moves and should pass or game should be over
      return Optional.empty();
    }
  }
}
