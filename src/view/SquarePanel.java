package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import model.ReadonlyReversi;
import model.Tile;

/**
 * The HexPanel class displays the Reversi game board using a square grid. It extends JPanel
 * and handles mouse events for highlighting and clicking on squares. This class is part of the
 * graphical user interface for the Reversi game.
 */
public class SquarePanel extends JPanel {
  private final ReadonlyReversi model;
  private final int sideLen;
  private final double squareSize = 10;
  private final double circleRadius = squareSize * 0.4;
  private final Shape piece = new Ellipse2D.Double(
          0.5,     // left
          0.5,     // top
          2 * circleRadius,  // width
          2 * circleRadius); // height
  private int highlightedQ = Integer.MIN_VALUE;
  private int highlightedR = Integer.MIN_VALUE;
  private int[] highlightedHex = new int[]{-1, -1};
  private boolean clickedOutOfBounds = false;

  /**
   * Constructs a SquarePanel using information from the provided model.
   * @param model ReadonlyReversi model
   */
  public SquarePanel(ReadonlyReversi model) {
    this.model = model;
    sideLen = model.getSideLen();
    setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(800, 800));
    MouseEventsListener mouseListener = new MouseEventsListener();
    this.addMouseListener(mouseListener);
  }

  private Dimension getPreferredLogicalSize() {
    return new Dimension(sideLen * 10, sideLen * 10);
  }

  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    return ret;
  }

  private AffineTransform transformPhysicalToLogical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    return ret;
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.transform(transformLogicalToPhysical());
    g2d.setColor(Color.DARK_GRAY);
    g2d.fill(g2d.getClipBounds());
    g2d.setColor(Color.LIGHT_GRAY);

    // draws hexagons based on the model's game board
    for (int i = 0; i < sideLen; i++) {
      for (int j = 0; j < sideLen; j++) {
        try {
          Tile tile = model.getTileAt(i, j);
          drawAxialSquare(g2d, i, j);

          // highlights proper hexagon and deselects when necessary
          handleMouseClicks(g2d, i, j, tile);

          // draws a circle on top of the tile if it is claimed by a player
          if (tile != Tile.EMPTY) {
            if (tile == Tile.BLACK) {
              drawCircle(g2d, i, j, Color.BLACK);
            } else {
              drawCircle(g2d, i, j, Color.WHITE);
            }
          }
        } catch (IllegalArgumentException e) {
          // do nothing because there is no tile at this coordinate in the board
        }
      }
    }
  }

  private void handleMouseClicks(Graphics2D g2d, int row, int col, Tile tile) {
    if (row == highlightedQ && col == highlightedR && tile == Tile.EMPTY) {
      // highlights proper hexagon
      g2d.setColor(Color.CYAN);
      drawAxialSquare(g2d, row, col);
      g2d.setColor(Color.LIGHT_GRAY);

      // if highlighted hexagon is clicked again, unhighlight it
      if (highlightedHex[0] != -1 && highlightedHex[1] != -1 && highlightedHex[0] == highlightedQ
              && highlightedHex[1] == highlightedR) {
        drawAxialSquare(g2d, row, col);
      }
      highlightedHex = new int[]{row, col};
    }

    // unhighlight the hexagon if clicked out of bounds
    if (clickedOutOfBounds) {
      drawAxialSquare(g2d, row, col);
    }
  }

  private void drawAxialSquare(Graphics2D g2d, int row, int col) {
    Point2D center = convertAxial(row, col);
    AffineTransform oldTransform = g2d.getTransform();
    g2d.translate(center.getX(), center.getY());
    int gap = 1;
    g2d.fillRect(0, 0, (int) (squareSize - gap), (int) (squareSize - gap));
    g2d.setTransform(oldTransform);
  }

  // draws a circle at the given axial coordinate
  private void drawCircle(Graphics2D g2d, int row, int col, Color color) {
    Color oldColor = g2d.getColor();
    g2d.setColor(color);
    AffineTransform oldTransform = g2d.getTransform();
    Point2D center = convertAxial(row, col);
    g2d.translate(center.getX(), center.getY());
    g2d.fill(piece);
    g2d.setTransform(oldTransform);
    g2d.setColor(oldColor);
  }


  private Point2D convertAxial(int row, int col) {
    return new Point2D.Double(row * squareSize, col * squareSize);
  }

  private int[] getSquareAtLogical(Point2D logicalPoint) {
    int[] hexagon = new int[]{-1, -1};

    for (int i = 0; i < sideLen; i++) {
      for (int j = 0; j < sideLen; j++) {
        Point2D hexagonCenter = convertAxial(i, j);
        double distance = logicalPoint.distance(hexagonCenter);

        if (distance < squareSize) {
          hexagon[0] = i - 1;
          hexagon[1] = j - 1;
        }
      }
    }
    return hexagon;
  }

  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      Point physicalP = e.getPoint();
      Point2D logicalP = transformPhysicalToLogical().transform(physicalP, null);
      int[] hex = getSquareAtLogical(logicalP);
      // if a hexagon exists at the given coordinates highlight it
      if (hex[0] != -1) {
        highlightedQ = hex[0];
        highlightedR = hex[1];
        repaint();
        System.out.println("Clicked Cell: (" + (highlightedQ + 1) + ", " + 
                (highlightedR + 1) + ")");
      } else {
        // if a hexagon does not exist, the click was made out of bounds and should unhighlight
        clickedOutOfBounds = true;
        repaint();
        clickedOutOfBounds = false;
      }
    }
  }
}
