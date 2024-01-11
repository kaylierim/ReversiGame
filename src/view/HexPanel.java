package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;

import model.AxialCoords;
import model.ReadonlyReversi;
import model.Tile;

/**
 * The HexPanel class displays the Reversi game board using a hexagonal grid. It extends JPanel
 * and handles mouse events for highlighting and clicking on hexagons. This class is part of the
 * graphical user interface for the Reversi game.
 */
public class HexPanel extends JPanel {
  private final int sideLen;
  private final int boardLen;
  private final double hexSize = 5.0;
  private final double circleRadius = hexSize * 0.5;
  private final ReadonlyReversi model;
  private final Hexagon hexagon;
  private final Shape piece = new Ellipse2D.Double(
          -circleRadius,     // left
          -circleRadius,     // top
          2 * circleRadius,  // width
          2 * circleRadius); // height
  private int highlightedQ = Integer.MIN_VALUE;
  private int highlightedR = Integer.MIN_VALUE;
  private AxialCoords highlightedHex = null;
  private boolean clickedOutOfBounds = false;
  private final List<ViewFeatures> listeners = new ArrayList<>();
  private boolean playerActionsEnabled = true;

  /**
   * Constructs a HexPanel using information from the provided model.
   * @param model ReadonlyReversi model
   */
  public HexPanel(ReadonlyReversi model) {
    sideLen = model.getSideLen();
    boardLen = sideLen + sideLen - 1;
    setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(800, 800));
    hexagon = new Hexagon(hexSize * 0.97);
    this.model = model;
    MouseEventsListener mouseListener = new MouseEventsListener();
    this.addMouseListener(mouseListener);
    setUpKeyEvents();
  }

  private void setUpKeyEvents() {
    this.getInputMap().put(KeyStroke.getKeyStroke("typed m"), "makeMove");
    this.getInputMap().put(KeyStroke.getKeyStroke("typed p"), "pass");

    this.getActionMap().put("makeMove", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if ((highlightedQ != Integer.MIN_VALUE && highlightedR != Integer.MIN_VALUE)
                && playerActionsEnabled) {
          for (ViewFeatures f : listeners) {
            f.makeMoveFeatures(highlightedQ, highlightedR);
            f.printToConsoleKey('m');
          }
        }
      }
    });

    this.getActionMap().put("pass", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (playerActionsEnabled) {
          for (ViewFeatures f : listeners) {
            f.passFeatures();
            f.printToConsoleKey('p');
          }
        }
      }
    });
  }

  public void addListener(ViewFeatures listener) {
    listeners.add(listener);
  }

  public void enablePlayerActions(boolean enable) {
    playerActionsEnabled = enable;
    repaint();
  }

  private Dimension getPreferredLogicalSize() {
    return new Dimension(boardLen * 10, boardLen * 10);
  }

  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.translate(getWidth() / 2., getHeight() / 2.);
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    ret.scale(1, 1);
    return ret;
  }

  private AffineTransform transformPhysicalToLogical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(1, 1);
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);
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
    for (int i = 0; i < boardLen; i++) {
      for (int j = 0; j < boardLen; j++) {
        try {
          Tile tile = model.getTileAt(i, j);
          AxialCoords coords = AxialCoords.convert(i, j, sideLen);
          drawAxialHexagon(g2d, coords);

          // highlights proper hexagon and deselects when necessary
          handleMouseClicks(g2d, coords, tile);

          // draws a circle on top of the tile if it is claimed by a player
          if (tile != Tile.EMPTY) {
            if (tile == Tile.BLACK) {
              drawCircle(g2d, coords, Color.BLACK);
            } else {
              drawCircle(g2d, coords, Color.WHITE);
            }
          }
        } catch (IllegalArgumentException e) {
          // do nothing because there is no tile at this coordinate in the board
        }
      }
    }
  }

  private void handleMouseClicks(Graphics2D g2d, AxialCoords coords, Tile tile) {
    if (playerActionsEnabled) {
      if (coords.getQ() == highlightedQ && coords.getR() == highlightedR && tile == Tile.EMPTY) {
        // highlights proper hexagon
        g2d.setColor(Color.CYAN);
        drawAxialHexagon(g2d, coords);
        g2d.setColor(Color.LIGHT_GRAY);

        // if highlighted hexagon is clicked again, unhighlight it
        if (highlightedHex != null && highlightedHex.getQ() == highlightedQ
                && highlightedHex.getR() == highlightedR) {
          drawAxialHexagon(g2d, coords);
        }
        highlightedHex = coords;
      }

      // unhighlight the hexagon if clicked out of bounds
      if (clickedOutOfBounds) {
        drawAxialHexagon(g2d, coords);
      }
    }
  }

  // draws a hexagon at the selected axial coordinate
  private void drawAxialHexagon(Graphics2D g2d, AxialCoords coords) {
    Point2D center = convertAxial(coords);
    AffineTransform oldTransform = g2d.getTransform();
    g2d.translate(center.getX(), center.getY());
    g2d.fill(hexagon);
    g2d.setTransform(oldTransform);
  }

  // draws a circle at the given axial coordinate
  private void drawCircle(Graphics2D g2d, AxialCoords coords, Color color) {
    Color oldColor = g2d.getColor();
    g2d.setColor(color);
    AffineTransform oldTransform = g2d.getTransform();
    Point2D center = convertAxial(coords);
    g2d.translate(center.getX(), center.getY());
    g2d.fill(piece);
    g2d.setTransform(oldTransform);
    g2d.setColor(oldColor);
  }

  // returns the center point of the given axial coordinate
  private Point2D convertAxial(AxialCoords coords) {
    return new Point2D.Double(coords.getQ() * Math.sqrt(3) * hexSize + Math.sqrt(3)
            * hexSize / 2.0 * coords.getR(), coords.getR() * 3.0 * hexSize / 2.0);
  }

  private AxialCoords getHexagonAtLogical(Point2D logicalPoint) {
    AxialCoords hexagon = null;

    for (int i = 0; i < boardLen; i++) {
      for (int j = 0; j < boardLen; j++) {
        AxialCoords coords = AxialCoords.convert(i, j, sideLen);
        Point2D hexagonCenter = convertAxial(coords);
        double distance = logicalPoint.distance(hexagonCenter);

        if (distance < hexSize) {
          hexagon = coords;
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
      AxialCoords hex = getHexagonAtLogical(logicalP);
      // if a hexagon exists at the given coordinates highlight it
      if (hex != null) {
        highlightedQ = hex.getQ();
        highlightedR = hex.getR();
        repaint();
        for (ViewFeatures f : listeners) {
          f.printToConsoleClick(highlightedQ, highlightedR);
        }
      } else {
        // if a hexagon does not exist, the click was made out of bounds and should unhighlight
        clickedOutOfBounds = true;
        repaint();
        clickedOutOfBounds = false;
      }
    }
  }
}

