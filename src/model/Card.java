package model;

import java.util.List;


/**
 * Represents a card in the game.
 */
public class Card {
  private final String name;
  private final List<Integer> attackValues;
  private PlayerColor color;

  /**
   * Constructs a card with a name, attack values, and color.
   *
   * @param name   name of card
   * @param values attack values of card
   * @param color  color of card
   */
  public Card(String name, List<Integer> values, PlayerColor color) {
    this.name = name;
    this.attackValues = values;
    this.color = color;
  }

  /**
   * Gets the name of the card.
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the attack value of the card in the given direction.
   * @param direction
   * @return
   */
  public int getAttack(Direction direction) {
    return attackValues.get(direction.ordinal());
  }

  /**
   * Gets the color of the card.
   * @return
   */
  public PlayerColor getColor() {
    return color;
  }


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

  /**
   * Returns a string representation of the card.
   *
   * @return
   */
  @Override
  public String toString() {
    return this.getName() + " " +
            this.getAttack(Direction.NORTH) + " " +
            this.getAttack(Direction.SOUTH) + " " +
            this.getAttack(Direction.EAST) + " " +
            this.getAttack(Direction.WEST);
  }
}
