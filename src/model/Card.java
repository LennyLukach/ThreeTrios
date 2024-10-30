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
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the attack value of the card in the given direction.
   *
   * @param direction an Enum direction of either NORTH, SOUTH, EAST, or WEST
   * @return the index of the direction
   */
  public int getAttack(Direction direction) {
    return attackValues.get(direction.ordinal());
  }

  /**
   * Gets the color of the card.
   *
   * @return Either RED or BLUE, depending on who owns the card
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
   * @return the name and the ints at each direction index
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
