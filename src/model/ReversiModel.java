package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the model implementation of a Reversi game.
 */
public class ReversiModel implements Reversi {
  private boolean gameStarted = false;
  private boolean isGameOver = false;
  // row indexing: 0th index represents the top of the board
  // column indexing: 0th index represents the left most side of the board
  // since the columns of a hexagon are not aligned perfectly, the
  // columns of this board correspond with the diagonal of the hexagon
  // the left most diagonal of the visual hexagon corresponds to the 0th column in the board array
  private Tile[][] board;
  private Tile turn;
  // INVARIANT: turn is never Tile.EMPTY
  private final int sideLen;
  private int numPassInARow = 0;
  private final List<ModelFeatures> listeners = new ArrayList<>();

  /**
   * Constructs a ReversiModel with the given side length.
   *
   * @param sideLen the number of tiles per side of the hexagon model.
   * @throws IllegalArgumentException if the given sideLen is less than 3.
   */
  public ReversiModel(int sideLen) {
    if (sideLen < 3) {
      throw new IllegalArgumentException("Size of board cannot be less than 3");
    }
    this.sideLen = sideLen;
  }

  private void notifyListenersOnActivePlayerChanged() {
    for (ModelFeatures listener : listeners) {
      listener.playerChanged(turn);
    }
  }

  public void addFeatures(ModelFeatures listener) {
    listeners.add(listener);
  }

  private void throwIfGameHasNotStarted() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }
  }

  private void throwIfGameIsOver() throws IllegalStateException {
    if (isGameOver) {
      throw new IllegalStateException("Game is over.");
    }
  }

  @Override
  public void startGame() {
    if (!gameStarted) {
      gameStarted = true;
    } else {
      throw new IllegalStateException();
    }

    turn = Tile.BLACK;
    notifyListenersOnActivePlayerChanged();
    initializeBoard();
    initializeStartingPieces();
  }

  private void initializeBoard() {
    int boardLen = sideLen + sideLen - 1;
    board = new Tile[boardLen][boardLen];

    // initialize top half
    int startCol = sideLen - 1;
    for (int i = 0; i < sideLen - 1; i++) {
      for (int j = startCol; j < boardLen; j++) {
        board[i][j] = Tile.EMPTY;
      }
      startCol--;
    }

    // initialize middle row
    for (int i = 0; i < boardLen; i++) {
      board[sideLen - 1][i] = Tile.EMPTY;
    }

    // initialize bottom half
    int endCol = boardLen - 1;
    for (int i = sideLen; i < boardLen; i++) {
      for (int j = 0; j < endCol; j++) {
        board[i][j] = Tile.EMPTY;
      }
      endCol--;
    }
  }

  private void initializeStartingPieces() {
    int boardLen = sideLen + sideLen - 1;
    for (int i = 0; i < boardLen; i++) {
      for (int j = 0; j < boardLen; j++) {
        if ((i == sideLen - 2 && j == sideLen - 1) || (i == sideLen - 1 && j == sideLen)
                || (i == sideLen && j == sideLen - 2)) {
          board[i][j] = Tile.BLACK;
        } else if ((i == sideLen - 2 && j == sideLen) || (i == sideLen - 1 && j == sideLen - 2)
                || (i == sideLen && j == sideLen - 1)) {
          board[i][j] = Tile.WHITE;
        }
      }
    }
  }

  @Override
  public void pass() {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();
    turn = turn.getOpposite();
    notifyListenersOnActivePlayerChanged();

    // keeps track of how many times pass was called in a row
    if (numPassInARow == 0) {
      numPassInARow++;
    } else if (numPassInARow == 1) {
      isGameOver = true;
    }
  }

  @Override
  public void makeMove(int row, int col) {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();

    // checking if it is bordering an opposing tile color,
    // is an empty tile, and has a tile to connect to
    throwIfInvalidMove(row, col);

    // place the tile and update the necessary tiles to their new color
    updateTilesInMove(row, col);

    turn = turn.getOpposite();
    notifyListenersOnActivePlayerChanged();

    // calls pass on the other player if they have no valid moves
    if (!validMoveExists(turn)) {
      pass();
    } else {
      numPassInARow = 0;
    }
  }

  @Override
  public Tile[][] copyGameBoard() {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();

    int boardLen = sideLen + sideLen - 1;
    Tile[][] boardCopy = new Tile[boardLen][boardLen];
    for (int i = 0; i < boardLen; i++) {
      System.arraycopy(board[i], 0, boardCopy[i], 0, boardLen);
    }
    return boardCopy;
  }

  private boolean checkConnectionExists(int row, int col) {
    for (int i = 0; i < 6; i++) {
      int rowOffset = getRowOffsets(i);
      int colOffset = getColOffsets(i);

      if (isOutOfBounds(row + rowOffset, col + colOffset)) {
        continue;
      }

      if (getConnectionInDirection(row, col, rowOffset, colOffset) != null) {
        return true;
      }
    }
    return false;
  }

  // returns the row offset according to the current neighbor iteration.
  private int getRowOffsets(int i) {
    if (i == 0 || i == 1) {
      return -1;
    } else if (i == 3 || i == 4) {
      return 1;
    }
    return 0;
  }

  // returns the col offset according to the current neighbor iteration.
  private int getColOffsets(int i) {
    if (i == 1 || i == 2) {
      return 1;
    } else if (i == 4 || i == 5) {
      return -1;
    }
    return 0;
  }

  // returns a list of coordinates for the pieces going in the direction to the connecting piece
  // if there is no available move in this direction, it returns null
  private List<int[]> getConnectionInDirection(int row, int col,
                                               int rowOffset, int colOffset) {
    List<int[]> coords = new ArrayList<>();
    Tile currTile = board[row + rowOffset][col + colOffset];
    int distanceFrom = 1;
    boolean foundOppositeTile = false;
    while (currTile != turn) {
      if (currTile == Tile.EMPTY) {
        break;
      }
      foundOppositeTile = true;
      int rowCoord = row + rowOffset * distanceFrom;
      int colCoord = col + colOffset * distanceFrom;
      if (isOutOfBounds(rowCoord, colCoord)) {
        break;
      }
      coords.add(new int[]{rowCoord, colCoord});
      currTile = board[rowCoord][colCoord];
      distanceFrom++;
    }
    if (currTile == turn && foundOppositeTile) {
      coords.add(new int[]{row, col});
      return coords;
    }
    return null;
  }

  // checks that the tile borders a tile of the opposite color
  private boolean checkNeighborsHaveOppositeColor(int row, int col) {
    Tile opp = turn.getOpposite();
    int countNumOppositeColorNeighbors = 0;
    for (int[] neighbor : getNeighbors(row, col)) {
      if (board[neighbor[0]][neighbor[1]] == opp) {
        countNumOppositeColorNeighbors++;
      }
    }
    return countNumOppositeColorNeighbors >= 1;
  }

  // returns an ordered list starting at top left going clockwise
  @Override
  public List<int[]> getNeighbors(int row, int col) {
    List<int[]> neighbors = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      int neighborRow = row + getRowOffsets(i);
      int neighborCol = col + getColOffsets(i);
      if (!isOutOfBounds(neighborRow, neighborCol)) {
        neighbors.add(new int[] {neighborRow, neighborCol});
      }
    }
    return neighbors;
  }

  // helper for updateTilesInMove()
  private void updateTiles(List<int[]> tiles) {
    for (int[] coord : tiles) {
      board[coord[0]][coord[1]] = turn;
    }
  }

  // helper for makeMove() that updates the tiles in-between the placed tile and connection tile
  private void updateTilesInMove(int row, int col) {
    for (int i = 0; i < 6; i++) {
      int rowOffset = getRowOffsets(i);
      int colOffset = getColOffsets(i);
      if (isOutOfBounds(row + rowOffset, col + colOffset)) {
        continue;
      }
      List<int[]> tilesInMove = getConnectionInDirection(row, col, rowOffset, colOffset);
      if (tilesInMove != null) {
        updateTiles(tilesInMove);
      }
    }
  }

  // helper for makeMove()
  private void throwIfInvalidMove(int row, int col)
          throws IllegalStateException, IllegalArgumentException {
    if (isOutOfBounds(row, col)) {
      throw new IllegalArgumentException("Move is out of bounds.");
    }
    if (board[row][col] != Tile.EMPTY) {
      throw new IllegalStateException("This tile is not empty");
    }
    if (!checkNeighborsHaveOppositeColor(row, col)) {
      throw new IllegalStateException("Your tile has to border the opposite color");
    }
    if (!checkConnectionExists(row, col)) {
      throw new IllegalStateException("This is an illegal move");
    }
  }

  // helper for isValidMove()
  private boolean isOutOfBounds(int row, int col) {
    if (row < 0 || row >= board.length || col >= board.length || col < 0) {
      return true;
    }
    return board[row][col] == null;
  }

  @Override
  public boolean isGameOver() {
    throwIfGameHasNotStarted();
    if (isGameOver) {
      return true;
    }
    return !validMoveExists(turn) && !validMoveExists(turn.getOpposite());
  }

  // checks if any moves are possible for the given player
  private boolean validMoveExists(Tile player) {
    Tile currentTurn = getTurn();
    if (turn != player) {
      turn = turn.getOpposite();
    }
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        if (!isOutOfBounds(i, j)) {
          if (board[i][j] == Tile.EMPTY) {
            try {
              throwIfInvalidMove(i, j);
              turn = currentTurn;
              return true;
            } catch (IllegalArgumentException | IllegalStateException e) {
              // do nothing because we need to check if any
              // player can make any moves at all coordinates
            }
            turn = currentTurn;
          }
        }
      }
    }
    return false;
  }

  @Override
  public Tile getTurn() {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();
    if (turn == Tile.BLACK) {
      return Tile.BLACK;
    } else {
      return Tile.WHITE;
    }
  }

  @Override
  public Tile getTileAt(int row, int col) {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();
    if (isOutOfBounds(row, col)) {
      throw new IllegalArgumentException();
    }
    return board[row][col];
  }

  @Override
  public Tile getWinner() {
    throwIfGameHasNotStarted();
    if (!isGameOver()) {
      throw new IllegalStateException();
    }

    int scoreWhite = getScore(Tile.WHITE);
    int scoreBlack = getScore(Tile.BLACK);

    if (scoreWhite > scoreBlack) {
      return Tile.WHITE;
    } else if (scoreWhite < scoreBlack) {
      return Tile.BLACK;
    } else {
      throw new IllegalStateException("There is no winner. Game ended in a draw.");
    }
  }

  @Override
  public int getSideLen() {
    return sideLen;
  }

  @Override
  public boolean isLegalMove(int row, int col) {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();

    try {
      throwIfInvalidMove(row, col);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return false;
    }
    return true;
  }

  @Override
  public int getScore(Tile player) {
    throwIfGameHasNotStarted();

    int countWhite = 0;
    int countBlack = 0;

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        if (!isOutOfBounds(i, j)) {
          if (board[i][j] == Tile.WHITE) {
            countWhite++;
          } else if (board[i][j] == Tile.BLACK) {
            countBlack++;
          }
        }
      }
    }

    if (player == Tile.WHITE) {
      return countWhite;
    } else {
      return countBlack;
    }
  }

  @Override
  public boolean currentPlayerHasLegalMove() {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();

    return validMoveExists(turn);
  }

  @Override
  public int getScoreOfMove(int row, int col, Tile player) {
    int numTilesCaptured = 0;
    for (int i = 0; i < 6; i++) {
      int rowOffset = getRowOffsets(i);
      int colOffset = getColOffsets(i);
      if (isOutOfBounds(row + rowOffset, col + colOffset)) {
        continue;
      }
      List<int[]> tilesInMove = getConnectionInDirection(row, col, rowOffset, colOffset);
      if (tilesInMove != null) {
        for (int[] tile : tilesInMove) {
          Tile tileInMove = board[tile[0]][tile[1]];
          if (tileInMove != player && tileInMove != Tile.EMPTY) {
            numTilesCaptured++;
          }
        }
      }
    }
    return numTilesCaptured;
  }
}
