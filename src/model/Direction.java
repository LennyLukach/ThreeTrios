package model;

/**
 * This enum represents the 4 directions that are
 * on each card. Each direction interacts with the adjacent one
 * on the board. The ordinals represent the index where each card
 * holds their direction's number.
 */
public enum Direction {

  NORTH(0), SOUTH(1), EAST(2), WEST(3);

  Direction(int i) {
  }
}
