package view;

import java.util.Objects;

import javax.swing.*;

import model.ReadonlyReversi;

/**
 * The ReversiSquareGraphics class represents the GUI and view for a Reversi game. It extends JFrame
 * and displays the game board using a SquarePanel. The GUI provides visualization for the Reversi
 * game based on the provided read-only model.
 */
public class ReversiSquareGraphics extends JFrame implements ReversiGUIView {

  private final ReadonlyReversi model;

  public ReversiSquareGraphics(ReadonlyReversi model) {
    this.model = Objects.requireNonNull(model);
    render();
  }

  @Override
  public void render() {
    this.setTitle("Reversi Game");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(800, 800);
    this.setLocationRelativeTo(null);
    SquarePanel squarePanel = new SquarePanel(model);
    this.add(squarePanel);
    this.setVisible(true);
  }

  // not implementing these because we are not doing level 3 of assignment 9
  @Override
  public void setHotKey(KeyStroke key, String featureName) {
    // not implementing these because we are not doing level 3 of assignment 9
  }

  @Override
  public void addFeatures(ViewFeatures features) {
    // not implementing these because we are not doing level 3 of assignment 9

  }

  @Override
  public void enablePlayerActions(boolean enable) {
    // not implementing these because we are not doing level 3 of assignment 9
  }

  @Override
  public void showIllegalMoveMessage() {
    // not implementing these because we are not doing level 3 of assignment 9
  }

  @Override
  public void toggleHints(boolean enable) {
    // not implementing these because we are not doing level 3 of assignment 9
  }
}
