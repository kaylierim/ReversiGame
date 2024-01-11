package controller;

import java.util.Optional;

import javax.swing.KeyStroke;

import model.AIPlayer;
import model.AxialCoords;
import model.ModelFeatures;
import model.Player;
import model.Reversi;
import model.ReversiModel;
import model.Tile;
import view.ReversiGUIView;
import view.ViewFeatures;

/**
 * Represents the controller for a game of Reversi.
 * Implements ViewFeatures and ModelFeatures which is how
 * the controller and its counterparts flow in communication.
 */
public class ReversiController implements ViewFeatures, ModelFeatures {
  private final ReversiModel model;
  private final Player player;
  private final ReversiGUIView view;

  /**
   * Constructs a ReversiController. The constructor
   * sets itself as a listener for the model and view.
   *
   * @param model the model of the Reversi game.
   * @param player the player being controlled by this controller.
   * @param view the view corresponding to this controller.
   */
  public ReversiController(Reversi model, Player player, ReversiGUIView view) {
    this.model = (ReversiModel) model;
    this.player = player;
    this.view = view;

    this.view.addFeatures(this);
    this.view.setHotKey(KeyStroke.getKeyStroke("typed m"), "makeMove");
    this.view.setHotKey(KeyStroke.getKeyStroke("typed p"), "pass");

    this.model.addFeatures(this);
    makeAIMove(player.getTileColor());
  }

  @Override
  public void playerChanged(Tile currPlayer) {
    view.enablePlayerActions(player.getTileColor() == currPlayer);
    makeAIMove(currPlayer);
  }

  // makes the move on the board if the player changed to an AIPlayer
  private void makeAIMove(Tile currPlayer) {
    if (player instanceof AIPlayer && player.getTileColor() == currPlayer) {
      Optional<AxialCoords> coords = ((AIPlayer) player).chooseNextMove();
      if (coords.isPresent()) {
        makeMoveFeatures(coords.get().getQ(), coords.get().getR());
      } else {
        passFeatures();
      }
    }
  }

  @Override
  public void makeMoveFeatures(int q, int r) {
    if (player.getTileColor() == model.getTurn()) {
      try {
        model.makeMove(r + (model.getSideLen() - 1), q + (model.getSideLen() - 1));
      } catch (IllegalArgumentException | IllegalStateException e) {
        view.showIllegalMoveMessage();
      }
    }
  }

  @Override
  public void passFeatures() {
    if (player.getTileColor() == model.getTurn()) {
      model.pass();
    }
  }

  @Override
  public void printToConsoleClick(int q, int r) {
    System.out.println("Clicked Cell: (" + q + ", " + r + ")");
  }

  @Override
  public void printToConsoleKey(char key) {
    if (key == 'm') {
      System.out.println("A move was made");
    } else {
      System.out.println("Player passed");
    }
  }
}
