package view;

import model.SquareReversi;

/**
 * A simple text-based rendering of a Square Reversi game.
 */
public class SquareReversiTextualView implements ReversiTextView {
  SquareReversi model;

  public SquareReversiTextualView(SquareReversi model) {
    this.model = model;
  }

  @Override
  public String render() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < model.getSideLen(); i++) {
      for (int j = 0; j < model.getSideLen(); j++) {
        sb.append(model.getTileAt(i, j).toString());
        if (j != model.getSideLen() - 1) {
          sb.append(" ");
        }
      }
      if (i != model.getSideLen() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
