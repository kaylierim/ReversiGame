package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.*;

import model.ReadonlyReversi;

/**
 * The ReversiGraphics class represents the GUI and view for a Reversi game. It extends JFrame
 * and displays the game board using a HexPanel. The GUI provides visualization for the Reversi
 * game based on the provided read-only model.
 */
public class ReversiGraphics extends JFrame implements ReversiGUIView {
  private final ReadonlyReversi model;
  private HexPanel hexPanel;
  private boolean hints = false;

  /**
   * Constructs a new instance of ReversiGraphics with the specified
   * ReadonlyReversi model.
   *
   * @param model the ReadonlyReversi model representing the state of the Reversi game.
   */
  public ReversiGraphics(ReadonlyReversi model) {
    this.model = Objects.requireNonNull(model);
    render();
  }

  @Override
  public void render() {
    this.setTitle("Reversi Game");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(800, 800);
    this.setLocationRelativeTo(null);
    this.hexPanel = new HexPanel(model);
    this.setLayout(new BorderLayout());
    this.add(hexPanel, BorderLayout.CENTER);
    JButton hintsButton = new JButton("Toggle hints");
    this.add(hintsButton, BorderLayout.SOUTH);
    hintsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        toggleHints(!hints);
        hints = !hints;
        hexPanel.requestFocusInWindow();
        System.out.printf("Hints: %s\n", hints ? "on" : "off");
      }
    });
    this.setVisible(true);
  }

  @Override
  public void setHotKey(KeyStroke key, String featureName) {
    this.hexPanel.getInputMap().put(key, featureName);
  }

  @Override
  public void addFeatures(ViewFeatures features) {
    this.hexPanel.addListener(features);
  }

  @Override
  public void enablePlayerActions(boolean enable) {
    hexPanel.enablePlayerActions(enable);
  }

  @Override
  public void showIllegalMoveMessage() {
    JOptionPane.showMessageDialog(this, "Illegal Move for player",
            "Message", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void toggleHints(boolean enable) {
    hexPanel.enableHints(enable);
  }
}
