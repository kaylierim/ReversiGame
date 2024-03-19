package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the model implementation of a Square Reversi game.
 */
public class SquareReversi implements Reversi {
  private boolean gameStarted = false;
  private boolean isGameOver = false;
  // uses row and col starting at index 0
  private Tile[][] board;
  private Tile turn;
  // INVARIANT: turn is never Tile.EMPTY
  private final int sideLen = 8;
  private int numPassInARow = 0;

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
  public void startGame() throws IllegalStateException {
    if (!gameStarted) {
      gameStarted = true;
    } else {
      throw new IllegalStateException();
    }

    turn = Tile.BLACK;
    initializeBoardAndPieces();
  }

  @Override
  public void pass() throws IllegalStateException {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();
    turn = turn.getOpposite();

    // keeps track of how many times pass was called in a row
    if (numPassInARow == 0) {
      numPassInARow++;
    } else if (numPassInARow == 1) {
      isGameOver = true;
    }
  }

  private void initializeBoardAndPieces() {
    board = new Tile[sideLen][sideLen];

    for (int i = 0; i < sideLen; i++) {
      for (int j = 0; j < sideLen; j++) {
        if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
          board[i][j] = Tile.BLACK;
        } else if ((i == 3 && j == 4) || (i == 4 && j == 3)) {
          board[i][j] = Tile.WHITE;
        } else {
          board[i][j] = Tile.EMPTY;
        }
      }
    }
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    throwIfGameHasNotStarted();
    if (isGameOver) {
      return true;
    }
    return !validMoveExists(turn) && !validMoveExists(turn.getOpposite());
  }

  @Override
  public Tile getTurn() throws IllegalStateException {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();
    if (turn == Tile.BLACK) {
      return Tile.BLACK;
    } else {
      return Tile.WHITE;
    }
  }

  @Override
  public Tile getTileAt(int row, int col) throws IllegalStateException, IllegalArgumentException {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();
    if (isOutOfBounds(row, col)) {
      throw new IllegalArgumentException();
    }
    return board[row][col];
  }

  @Override
  public Tile getWinner() throws IllegalStateException {
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

  private boolean isOutOfBounds(int row, int col) {
    return row < 0 || row >= sideLen || col >= sideLen || col < 0;
  }

  @Override
  public int getSideLen() {
    return 8;
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
    int score = 0;

    int[] directions = {-1, 0, 1};

    for (int dr : directions) {
      for (int dc : directions) {
        if (dr != 0 || dc != 0) {
          int r = row + dr;
          int c = col + dc;

          if (!isOutOfBounds(r, c)) {
            Tile opponent = player.getOpposite();

            if (board[r][c] == opponent) {
              while (r >= 0 && r < board.length && c >= 0 && c < board[0].length
                      && board[r][c] == opponent) {
                r += dr;
                c += dc;
              }

              // If a disc of the player's color is encountered, increment the score
              if (r >= 0 && r < board.length && c >= 0 && c < board[0].length
                      && board[r][c] == player) {
                while (r != row || c != col) {
                  r -= dr;
                  c -= dc;
                  score++;
                }
              }
            }
          }
        }
      }
    }
    return score;
  }

  @Override
  public List<int[]> getNeighbors(int row, int col) {
    List<int[]> neighbors = new ArrayList<>();

    int[] directions = {-1, 0, 1};

    for (int dr : directions) {
      for (int dc : directions) {
        if (dr != 0 || dc != 0) {
          int r = row + dr;
          int c = col + dc;

          if (!isOutOfBounds(row, col)) {
            neighbors.add(new int[]{r, c});
          }
        }
      }
    }
    return neighbors;
  }

  @Override
  public Tile[][] copyGameBoard() {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();

    Tile[][] boardCopy = new Tile[sideLen][sideLen];
    for (int i = 0; i < sideLen; i++) {
      System.arraycopy(board[i], 0, boardCopy[i], 0, sideLen);
    }
    return boardCopy;
  }

  @Override
  public void makeMove(int row, int col) throws IllegalStateException, IllegalArgumentException {
    throwIfGameHasNotStarted();
    throwIfGameIsOver();

    throwIfInvalidMove(row, col);

    int[] directions = {-1, 0, 1};

    for (int dr : directions) {
      for (int dc : directions) {
        updateTilesInMove(row, col, dr, dc);
      }
    }

    turn = turn.getOpposite();

    if (!validMoveExists(turn)) {
      pass();
    } else {
      numPassInARow = 0;
    }
  }

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

  private void updateTilesInMove(int row, int col, int dr, int dc) {
    int r = row + dr;
    int c = col + dc;
    Tile opponent = turn.getOpposite();

    // Check if the first adjacent disc is of the opponent's color
    if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == opponent) {
      // Continue in the direction until an empty space or a disc of
      // the player's color is encountered
      while (r >= 0 && r < board.length && c >= 0 && c < board[0].length
              && board[r][c] == opponent) {
        r += dr;
        c += dc;
      }

      // If a disc of the player's color is encountered, flip the discs in between
      if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == turn) {
        while (r != row || c != col) {
          r -= dr;
          c -= dc;
          board[r][c] = turn;
        }
      }
    }
  }

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

  private boolean checkConnectionExists(int row, int col) {
    int[] directions = {-1, 0, 1};

    for (int dr : directions) {
      for (int dc : directions) {
        if (dr != 0 || dc != 0) {
          int r = row + dr;
          int c = col + dc;

          while (r >= 0 && r < board.length && c >= 0 && c < board[0].length
                  && board[r][c] != Tile.EMPTY && board[r][c] != turn) {
            r += dr;
            c += dc;
          }
          if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == turn) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean checkNeighborsHaveOppositeColor(int row, int col) {
    int[] directions = {-1, 0, 1};

    for (int dr : directions) {
      for (int dc : directions) {
        if (dr != 0 || dc != 0) {
          int r = row + dr;
          int c = col + dc;

          if (r >= 0 && r < board.length && c >= 0 && c < board[0].length
                  && board[r][c] != Tile.EMPTY && board[r][c] != turn) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
