package model;

public class GridCell {
  private boolean isHole;
  private Card card;


  public GridCell(boolean isHole) {
    this.isHole = isHole;
    this.card = null;
  }

  public boolean isHole() {
    return isHole;
  }

  public Card getCard() {
    return card;
  }

  public void setCard(Card card) {
    this.card = card;
  }
}
