package model;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * This is the implementation for the model for the game Three Trios.
 */
public class ThreeTriosModel {

  private final Player redPlayer;
  private final Player bluePlayer;
  private List<Card> redHand;
  private List<Card> blueHand;
  private Deck deck;
  private Grid grid;
  private GameState gameState;

  /**
   * Constructor for the ThreeTriosModel.
   *
   * @param deckFile file path for the deck config
   * @param gridFile file path for the grid config
   * @throws FileNotFoundException if the file is not found
   */
  public ThreeTriosModel(String deckFile, String gridFile) throws FileNotFoundException {
    this.deck = new Deck(deckFile);
    this.grid = new Grid(gridFile);
    this.redPlayer = new Player(PlayerColor.RED, redHand);
    this.bluePlayer = new Player(PlayerColor.BLUE, blueHand);
    gameState = GameState.NOT_STARTED;
  }

  /**
   * Starts the game by checking exceptions & initializing the hands, shuffling deck,
   * and making the grid
   *
   * @throws IllegalArgumentException if the deck size is not even or the grid size is not odd
   */
  public void startGame() {
    if (gameState != GameState.NOT_STARTED) { // if the game is in any other state besides not started, throw error
      throw new IllegalStateException("Game is not ready to start.");
    }
    if (deck.getDeck().size() % 2 != 0) { // deck size must be even
      throw new IllegalArgumentException("Deck size must be even");
    }
    if (grid.getNumCardCells() % 2 == 0) { // grid size must be odd
      throw new IllegalArgumentException("Grid size must be odd");
    }
    fillHands();
  }

  /**
   * Places a card of the player's color onto a valid cell on the grid.
   * Cards cannot be placed on holes, which vary depending on the grid.
   * Cards can only be played to grid during placing state.
   *
   * @param row       row index of grid cell to be played to
   * @param col       column index of grid cell to be played to
   * @param handIndex index of card in hand
   * @param color     color of player whose turn it is
   */
  public void placeCard(int row, int col, int handIndex, PlayerColor color) {
    if (gameState != GameState.PLACING) {
      throw new IllegalStateException("Game is not in placing stage.");
    }
    if (grid.isHole(row, col)) {
      throw new IllegalArgumentException("Cannot play card to a hole.");
    }
    if (row < 0 || row >= grid.getGrid().length || col < 0 || col >= grid.getGrid()[0].length) {
      throw new IllegalArgumentException("Row or Col is out of bounds.");
    }


    if (color == PlayerColor.RED) {
      placeCardHelper(row, col, redHand, handIndex);
    } else if (color == PlayerColor.BLUE) {
      placeCardHelper(row, col, blueHand, handIndex);
    } else {
      throw new IllegalArgumentException("Color cannot be null.");
    }
  }

  private void placeCardHelper(int row, int col, List<Card> hand, int handIndex) {
    if (hand.isEmpty()) {
      throw new IllegalArgumentException("Hand is empty.");
    }
    if (handIndex < 0 || handIndex >= hand.size()) {
      throw new IllegalArgumentException("Hand index is out of bounds.");
    }
    if (grid.getCard(row, col) != null) {
      throw new IllegalArgumentException("Cell is already occupied.");
    }

    Card card = hand.get(handIndex);
    grid.setCard(row, col, card);
    hand.remove(handIndex);
  }

  public void battle() {
    if (gameState != GameState.BATTLE) {
      throw new IllegalStateException("Game is not in battle stage.");
    }
  }

  public void isGameOver() {

  }

  public void determineWinner() {

  }

  // i changed setColor to return a card so
  // we could draw to the hand and set the color of the
  // card at the same time. lmk if you want to change this
  private void fillHands() {
    while (!deck.getDeck().isEmpty()) {
      redHand.add(deck.draw().setColor(PlayerColor.RED));
      blueHand.add(deck.draw().setColor(PlayerColor.BLUE));
    }
  }
}
