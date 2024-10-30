package model;

/**
 * Represents a cell in the grid of the game.
 */
public class GridCell {
  private boolean isHole;
  private Card card;


  /**
   * Constructs a GridCell object.
   * @param isHole
   */
  public GridCell(boolean isHole) {
    this.isHole = isHole;
    this.card = null;
  }

  /**
   * Checks if the cell is a hole.
   * @return
   */
  public boolean isHole() {
    return isHole;
  }

  /**
   * Gets the card in the cell.
   * @return
   */
  public Card getCard() {
    return card;
  }

  /**
   * Sets the card in the cell.
   * @param card
   */
  public void setCard(Card card) {
    this.card = card;
    this.isHole = false;
  }
}
