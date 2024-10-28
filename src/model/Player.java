package model;

import java.util.List;

public class Player {
  private final PlayerColor playerColor;
  private final List<Card> hand;

  public Player(PlayerColor color, List<Card> hand) {
    this.playerColor = color;
    this.hand = hand;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public List<Card> getHand() {
    return hand;
  }

  public void draw(Deck deck) {
    hand.add(deck.draw());
  }


}
