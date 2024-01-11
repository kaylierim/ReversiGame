package view;

import model.ReadonlyReversi;

/**
 * A simple text-based rendering of the Reversi game.
 */
public class ReversiTextualView implements ReversiTextView {

  private final ReadonlyReversi model;

  /**
   * Constructs a textual view for the Reversi game using the provided ReversiModel.
   *
   * @param model The ReversiModel to be associated with this view.
   */
  public ReversiTextualView(ReadonlyReversi model) {
    this.model = model;
  }

  @Override
  public String toString() {
    int sideLen = model.getSideLen();
    int numSpaces = sideLen - 1;
    StringBuilder sb = new StringBuilder();
    String space = " ";

    printTopHalf(sideLen, numSpaces, sb, space);
    printMiddleRow(sideLen, sb);
    printBottomHalf(sideLen, sb, space);
    return sb.toString();
  }

  private void printTopHalf(int sideLen, int numSpaces, StringBuilder sb, String space) {
    int boardLen = sideLen + sideLen - 1;
    for (int i = 0; i < sideLen - 1; i++) {
      sb.append(space.repeat(numSpaces));
      for (int j = numSpaces; j < boardLen; j++) {
        sb.append(model.getTileAt(i, j).toString());
        if (j != boardLen - 1) {
          sb.append(space);
        }
      }
      sb.append("\n");
      numSpaces--;
    }
  }

  private void printMiddleRow(int sideLen, StringBuilder sb) {
    for (int i = 0; i < sideLen * 2 - 1; i++) {
      int row = sideLen - 1;
      if (i != sideLen * 2 - 1 - 1) {
        sb.append(model.getTileAt(row, i).toString()).append(" ");
      } else {
        sb.append(model.getTileAt(row, i).toString());
      }
    }
    sb.append("\n");
  }

  private void printBottomHalf(int sideLen, StringBuilder sb, String space) {
    int numSpaces = 1;
    int boardLen = sideLen + sideLen - 1;
    int endCol = boardLen - 1;
    for (int i = sideLen; i < boardLen; i++) {
      sb.append(space.repeat(numSpaces));
      for (int j = 0; j < endCol; j++) {
        sb.append(model.getTileAt(i, j).toString());
        if (j != endCol - 1) {
          sb.append(space);
        }
      }
      endCol--;
      if (i != boardLen - 1) {
        sb.append("\n");
      }
      numSpaces++;
    }
  }

  @Override
  public String render() {
    return this.toString();
  }
}
