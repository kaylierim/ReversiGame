package view;

/**
 * The Features interface implements various features related to a Reversi game.
 * Implementations of this interface are used in conjunction with a graphical user interface (GUI).
 */
public interface ViewFeatures {
  /**
   * Checks with the controller if this move is allowable before displaying it on the view.
   * @param q the axial coordinate q.
   * @param r the axial coordinate r.
   */
  void makeMoveFeatures(int q, int r);

  /**
   * Executes a pass, if allowable by the model.
   */
  void passFeatures();

  /**
   * Prints to the console the axial coordinates of where the user clicked.
   * @param q the axial coordinate q.
   * @param r the axial coordinate r.
   */
  void printToConsoleClick(int q, int r);

  /**
   * Prints to the console a message with what move was made by the user.
   * @param key the key that was pressed.
   */
  void printToConsoleKey(char key);
}
