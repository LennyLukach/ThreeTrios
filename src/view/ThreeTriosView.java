package view;

import model.*;


/**
 * Represents the view for the game Three Trios.
 */
public class ThreeTriosView {
  private StringBuilder builder;
  private ThreeTriosModel model;


  /**
   * Constructs a ThreeTriosView with the given model.
   * @param model
   */
  public ThreeTriosView(ThreeTriosModel model) {
    this.model = model;
  }

  /**
   * Returns a string representation of the game state.
   * @return
   */
  @Override
  public String toString() {
    PlayerColor curPlayer = model.getCurrentPlayer();
    builder = new StringBuilder();
    builder.append("Player: ").append(curPlayer).append("\n");
    for (int row = 0; row < model.getGrid().getGrid().length; row++) {
      for (int col = 0; col < model.getGrid().getGrid()[0].length; col++) {
        if (model.getGrid().isHole(row, col)) {
          builder.append(" ");
        } else {
          Card card = model.getGrid().getCard(row, col);
          if (card == null) {
            builder.append("_");
          } else {
            builder.append(card.getColor() == PlayerColor.RED ? "R" : "B");
          }
        }
      }
      builder.append("\n");
    }
    builder.append("Hand:\n");
    for (int i = 0; i < model.getRedHand().size(); i++) {
      builder.append(model.getRedHand().get(i));
      if (i < model.getRedHand().size() - 1) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }
}