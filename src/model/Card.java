package model;

import java.util.List;


public class Card {
  private final String name;
  private final List<Integer> attackValues;
  private PlayerColor color;

  public Card(String name, List<Integer> values, PlayerColor color) {
    this.name = name;
    this.attackValues = values;
    this.color = color;
  }

  // get the name of a card
  public String getName() {
    return name;
  }

  // get the attack value of a card by putting index for direction: N0, S1, E2, W3
  public int getAttack(int direction) {
    return attackValues.get(direction);
  }

  // get the color of a card
  public PlayerColor getColor() {
    return color;
  }

  // set the color of a card

  /**
   * Sets color of card to provided player's color.
   *
   * @param color given player's color
   * @return the card
   */
  public Card setColor(PlayerColor color) {
    this.color = color;
    return this;
  }

}
