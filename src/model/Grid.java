package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Grid {
  private GridCell[][] grid;

  public Grid(String fileGrid) throws FileNotFoundException {
    this.grid = loadGridFromFile(fileGrid);
  }

  private GridCell[][] loadGridFromFile(String filePath) throws FileNotFoundException {
    File file = new File(filePath);
    Scanner scanner = new Scanner(file);
    int rows = scanner.nextInt();
    int cols = scanner.nextInt();

    GridCell[][] grid = new GridCell[rows][cols];

    while (scanner.hasNext()) {
      String line = scanner.nextLine();

      for (int rowIdx = 0; rowIdx < rows; rowIdx++) {
        for (int colIdx = 0; colIdx < cols; colIdx++) {
          if (line.toCharArray()[colIdx] == 'X') {
            grid[rowIdx][colIdx] = new GridCell(true);
          } else if (line.toCharArray()[colIdx] == 'C') {
            grid[rowIdx][colIdx] = new GridCell(false);
          }
          else {
            throw new IllegalArgumentException("Invalid character in grid file");
          }
        }
      }
    }
    return grid;
  }

  public GridCell[][] getGrid() {
    return grid;
  }

  public void setCard(int row, int col, Card card) {
    grid[row][col].setCard(card);
  }

  public Card getCard(int row, int col) {
    return grid[row][col].getCard();
  }

  public boolean isHole(int row, int col) {
    return grid[row][col].isHole();
  }

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
