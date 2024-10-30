package model;

/**
 * Represents a cell in the grid of the game.
 */
public class GridCell {
  private boolean isHole;
  private Card card;


  /**
   * Constructs a GridCell object.
   *
   * @param isHole whether the constructed cell is a hole or not
   */
  public GridCell(boolean isHole) {
    this.isHole = isHole;
    this.card = null;
  }

  /**
   * Checks if the cell is a hole.
   *
   * @return returns true if cell is a hole and false if not
   */
  public boolean isHole() {
    return isHole;
  }

  /**
   * Gets the card in the cell.
   *
   * @return returns the card in the cell specified
   */
  public Card getCard() {
    return card;
  }

  /**
   * Sets the card in the cell.
   *
   * @param card the card to be placed in the cell
   */
  public void setCard(Card card) {
    this.card = card;
    this.isHole = false;
  }
}
