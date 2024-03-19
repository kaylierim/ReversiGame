package view;

import javax.swing.KeyStroke;

/**
 * Represents the primary GUI view interface for playing a game of Reversi.
 */
public interface ReversiGUIView {

  /**
   * Shows the user a visualization of the current Reversi board.
   */
  void render();

  /**
   * Sets hot keys that are used when interacting with the view.
   * @param key the key and what kind of action it is performing.
   * @param featureName the name of the feature.
   */
  void setHotKey(KeyStroke key, String featureName);

  /**
   * Adds a listener to this view.
   * @param features the listener.
   */
  void addFeatures(ViewFeatures features);

  /**
   * Prevents players from interacting with the view when it is not their turn.
   * Enables player-view interactions when it is the players turn.
   * @param enable true if the view should be enabled, false otherwise.
   */
  void enablePlayerActions(boolean enable);

  /**
   * Shows an illegal move message to the user.
   */
  void showIllegalMoveMessage();

  /**
   * Toggles hints for the user.
   * When enabled, it displays the added score of a selected tile.
   * @param enable true if the hints should be enabled, false otherwise.
   */
  void toggleHints(boolean enable);
}
