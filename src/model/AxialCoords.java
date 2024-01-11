package model;

import java.util.Objects;

/**
 * The AxialCoords class represents coordinates in an axial coordinate system.
 * The coordinates are represented by q and r, where q corresponds to the diagonal column
 * (which starts at the top left and moves down rightward) and r corresponds to the row.
 * The center of the hexagon is (0, 0). The top left hexagon from the center is (0, -1),
 * the top right (1, 1), the right (1, 0), the bottom right (0, 1), the bottom left (-1, 1),
 * and the left (-1, 0), to give you an example.
 */
public class AxialCoords {
  private final int q;
  private final int r;

  /**
   * Constructs a new instance of AxialCoords with the specified q and r coordinates.
   *
   * @param q the q-coordinate.
   * @param r the r-coordinate.
   */
  public AxialCoords(int q, int r) {
    this.q = q;
    this.r = r;
  }

  /**
   * Gets the q-coordinate of the axial coordinates.
   *
   * @return the q-coordinate.
   */
  public int getQ() {
    return q;
  }

  /**
   * Gets the r-coordinate of the axial coordinates.
   *
   * @return the r-coordinate.
   */
  public int getR() {
    return r;
  }

  /**
   * Converts row and column indices to axial coordinates.
   *
   * @param row     the row index.
   * @param col     the column index.
   * @param sideLen the side length of the hexagon.
   * @return an AxialCoords object representing the converted coordinates.
   */
  public static AxialCoords convert(int row, int col, int sideLen) {
    int q = col - (sideLen - 1);
    int r = row - (sideLen - 1);
    return new AxialCoords(q, r);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AxialCoords that = (AxialCoords) o;
    return q == that.q && r == that.r;
  }

  @Override
  public int hashCode() {
    return Objects.hash(q, r);
  }

  @Override
  public String toString() {
    return "(" + q + ", " + r + ")";
  }
}
