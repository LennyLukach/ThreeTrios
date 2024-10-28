package model;

import java.util.List;

import static model.Card.loadCardsFromFile;

public class Deck {

  private List<Card> deck;
  public Deck(List<Card> deck) {
    this.deck = deck;
  }
}
