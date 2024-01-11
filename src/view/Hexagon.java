package view;

import java.awt.geom.Path2D;

/**
 * The Hexagon class represents a hexagon shape, extending the Path2D.Double
 * class. The hexagon is defined by six vertices in a closed path. The hexagon is created with
 * its center at the origin (0,0) and is oriented with one side parallel to the x-axis.The size of
 * the hexagon determines the distance from the center to any of its vertices.
 */
public class Hexagon extends Path2D.Double {

  /**
   * Constructs a new instance of Hexagon with the specified size.
   *
   * @param size the size of the hexagon, representing the distance from the center
   *             to any of its vertices.
   */
  public Hexagon(double size) {
    double angle = Math.toRadians(60); // 60 degrees for a hexagon
    double offset = Math.toRadians(90);

    // Start from the top-left corner of the hexagon
    double x = size * Math.cos(angle + offset);
    double y = size * Math.sin(angle + offset);
    moveTo(x, y);

    // Create the hexagon by adding points
    for (int i = 1; i < 6; i++) {
      x = size * Math.cos(angle * (i + 1) + offset);
      y = size * Math.sin(angle * (i + 1) + offset);
      lineTo(x, y);
    }

    // Close the path to form a closed shape
    closePath();
  }
}
