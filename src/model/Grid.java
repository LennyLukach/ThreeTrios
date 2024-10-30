package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents the grid of the game.
 */
public class Grid {
  private final GridCell[][] grid;

  /**
   * Constructs a Grid object from a file.
   * The first two ints in the file represent the number of rows and columns.
   * Holes in a grid file are represented with X,
   * and card cells are represented with C.
   *
   * @param fileGrid takes a file representation of a grid
   * @throws FileNotFoundException if the file is not formatted correctly
   */
  public Grid(String fileGrid) throws FileNotFoundException {
    this.grid = loadGridFromFile(fileGrid);
  }

  private GridCell[][] loadGridFromFile(String filePath) throws FileNotFoundException {
    File file = new File(filePath);
    Scanner scanner = new Scanner(file);
    int rows = scanner.nextInt();
    int cols = scanner.nextInt();
    scanner.nextLine();

    GridCell[][] grid = new GridCell[rows][cols];
    for (int rowIdx = 0; rowIdx < rows; rowIdx++) {
      if (!scanner.hasNext()) {
        throw new IllegalArgumentException("Not enough rows in grid file");
      }
      String line = scanner.nextLine();
      char[] chars = line.toCharArray();

      if (chars.length != cols) {
        throw new IllegalArgumentException("Invalid number of columns in grid file");
      }

      for (int colIdx = 0; colIdx < cols; colIdx++) {
        if (chars[colIdx] == 'X') {
          grid[rowIdx][colIdx] = new GridCell(true);
        } else if (chars[colIdx] == 'C') {
          grid[rowIdx][colIdx] = new GridCell(false);
        } else {
          throw new IllegalArgumentException("Invalid character in grid file");
        }
      }
    }
    return grid;
  }

  /**
   * Returns the grid.
   *
   * @return an array of grid cells
   */
  public GridCell[][] getGrid() {
    return grid;
  }

  /**
   * Sets the card in the grid cell.
   *
   * @param row  the row index of the cell
   * @param col  the column index of the cell
   * @param card the card being played
   */
  public void setCard(int row, int col, Card card) {
    grid[row][col].setCard(card);
  }

  /**
   * Returns the card in the grid cell.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the card in the cell
   */
  public Card getCard(int row, int col) {
    Card card;
    try {
      card = grid[row][col].getCard();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Row or Col is out of bounds.");
    }
    return card;
  }

  /**
   * Checks if the grid cell is a hole.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return true if the cell is a hole and false if not
   */
  public boolean isHole(int row, int col) {
    try {
      return grid[row][col].isHole();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Row or Col is out of bounds.");
    }
  }

  /**
   * Returns the number of card cells in the grid.
   *
   * @return the number of cells that are NOT holes
   */
  public int getNumCardCells() {
    int numCardCells = 0;
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        if (!isHole(row, col)) {
          numCardCells++;
        }
      }
    }
    return numCardCells;
  }

}
