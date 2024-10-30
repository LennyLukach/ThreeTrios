package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the implementation for the model for the game Three Trios.
 */
public class ThreeTriosModel implements IThreeTrioModel {

  private PlayerColor currentPlayer;
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
    redHand = new ArrayList<>();
    blueHand = new ArrayList<>();
    this.currentPlayer = PlayerColor.RED;
    gameState = GameState.NOT_STARTED;
  }

  /**
   * Starts the game by filling the hands of the players.
   * The game state is set to placing.
   * The deck must have an even number of cards.
   * The grid must have an odd number of card cells.
   * The game must be in the not started state.
   */
  @Override
  public void startGame() {
    if (gameState != GameState.NOT_STARTED) { // if the game is in any other state besides not started, throw error
      throw new IllegalStateException("Game is not ready to start.");
    }
    if (deck.getDeck().size() % 2 != 0) { // deck size must be even
      throw new IllegalArgumentException("Deck size must be even");
    }
    if (grid.getNumCardCells() % 2 == 0) { // grid size must be odd
      throw new IllegalArgumentException("Number of card cells must be odd");
    }
    fillHands();
    gameState = GameState.PLACING;
  }

  /**
   * Places a card from the hand into the grid.
   * @param row       row index of grid cell to be placed
   * @param col       column index of grid cell to be placed
   * @param handIndex index of card in hand
   * @param color     color of player whose turn it is
   */
  @Override
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
    gameState = GameState.BATTLE;
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

  /**
   * Battle method that checks the surrounding cells of the card placed.
   * @param row   row index of card placed
   * @param col   column index of card placed
   */
  @Override
  public void battle(int row, int col) {
    if (gameState != GameState.BATTLE) {
      throw new IllegalStateException("Game is not in battle stage.");
    }
    if (row < 0 || row >= grid.getGrid().length || col < 0 || col >= grid.getGrid()[0].length) {
      throw new IllegalArgumentException("Row or Col is out of bounds.");
    }
    if (grid.getCard(row, col) == null) {
      throw new IllegalArgumentException("Cell does not have a card.");
    }

    // check north
    attackNorth(row, col, grid.getCard(row, col).getColor());

    // check south
    attackSouth(row, col, grid.getCard(row, col).getColor());

    // check east
    attackEast(row, col, grid.getCard(row, col).getColor());

    // check west
    attackWest(row, col, grid.getCard(row, col).getColor());

    gameState = GameState.PLACING;

    if (currentPlayer == PlayerColor.RED) {
      currentPlayer = PlayerColor.BLUE;
    } else {
      currentPlayer = PlayerColor.RED;
    }
  }

  private void attackNorth(int row, int col, PlayerColor color) {
    if (row == 0) { // check if row - 1 is out of bounds
      return;
    }
    if (grid.getCard(row - 1, col) == null) {
      return;
    }
    boolean cardIsOpponents = grid.getCard(row - 1, col).getColor() != color;
    boolean cardExists = grid.getCard(row - 1, col) != null;
    if (cardIsOpponents && cardExists) {
      int attack = grid.getCard(row, col).getAttack(Direction.NORTH);
      int defense = grid.getCard(row - 1, col).getAttack(Direction.SOUTH);
      if (attack > defense) {
        grid.getCard(row - 1, col).setColor(color);
        battle(row - 1, col);
      }
    }
  }

  private void attackSouth(int row, int col, PlayerColor color) {
    if (row == grid.getGrid().length - 1) { // check if row + 1 is out of bounds
      return;
    }
    if (grid.getCard(row + 1, col) == null) {
      return;
    }
    boolean cardIsOpponents = grid.getCard(row + 1, col).getColor() != color;
    if (cardIsOpponents) {
      int attack = grid.getCard(row, col).getAttack(Direction.SOUTH);
      int defense = grid.getCard(row + 1, col).getAttack(Direction.NORTH);
      if (attack > defense) {
        grid.getCard(row + 1, col).setColor(color);
        battle(row + 1, col);
      }
    }
  }

  private void attackEast(int row, int col, PlayerColor color) {
    if (col == grid.getGrid()[0].length - 1) { // check if col + 1 is out of bounds
      return;
    }
    if (grid.getCard(row, col + 1) == null) {
      return;
    }
    boolean cardIsOpponents = grid.getCard(row, col + 1).getColor() != color;
    boolean cardExists = grid.getCard(row, col + 1) != null;
    if (cardIsOpponents && cardExists) {
      int attack = grid.getCard(row, col).getAttack(Direction.EAST);
      int defense = grid.getCard(row, col + 1).getAttack(Direction.WEST);
      if (attack > defense) {
        grid.getCard(row, col + 1).setColor(color);
        battle(row, col + 1);
      }
    }
  }

  private void attackWest(int row, int col, PlayerColor color) {
    if (col == 0) { // check if col - 1 is out of bounds
      return;
    }
    if (grid.getCard(row, col - 1) == null) {
      return;
    }
    boolean cardIsOpponents = grid.getCard(row, col - 1).getColor() != color;
    boolean cardExists = grid.getCard(row, col - 1) != null;
    if (cardIsOpponents && cardExists) {
      int attack = grid.getCard(row, col).getAttack(Direction.WEST);
      int defense = grid.getCard(row, col - 1).getAttack(Direction.EAST);
      if (attack > defense) {
        grid.getCard(row, col - 1).setColor(color);
        battle(row, col - 1);
      }
    }
  }


  /**
   * Determines if the game is over.
   * @return
   */
  @Override
  public boolean isGameOver() {
    for (int row = 0; row < grid.getGrid().length; row++) {
      for (int col = 0; col < grid.getGrid()[0].length; col++) {
        if (grid.getCard(row, col) == null && !grid.isHole(row, col)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Determines the winner of the game.
   * @return
   */
  public PlayerColor determineWinner() {
    int redCount = 0;
    int blueCount = 0;
    PlayerColor winner = null;

    for (int row = 0; row < grid.getGrid().length; row++) {
      for (int col = 0; col < grid.getGrid()[0].length; col++) {
        Card card = grid.getCard(row, col);
        if (card != null) {
          if (card.getColor() == PlayerColor.RED) {
            redCount++;
          } else if (card.getColor() == PlayerColor.BLUE) {
            blueCount++;
          }
        }
      }
    }

    redCount += redHand.size();
    blueCount += blueHand.size();

    if (redCount > blueCount) {
      winner = PlayerColor.RED;
    } else if (blueCount > redCount) {
      winner = PlayerColor.BLUE;
    } else {
      return null;
    }

    return winner;
  }

  /**
   * Gets the red hand.
   * @return
   */
  @Override
  public List<Card> getRedHand() {
    return redHand;
  }

  /**
   * Gets the blue hand.
   * @return
   */
  @Override
  public List<Card> getBlueHand() {
    return blueHand;
  }

  /**
   * Gets the game state.
   * @return
   */
  @Override
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Gets the grid.
   * @return
   */
  @Override
  public Grid getGrid() {
    return this.grid;
  }

  /**
   * Gets the current player.
   * @return
   */
  @Override
  public PlayerColor getCurrentPlayer() {
    return currentPlayer;
  }

  private void fillHands() {
    while (!deck.getDeck().isEmpty()) {
      redHand.add(deck.draw().setColor(PlayerColor.RED));
      blueHand.add(deck.draw().setColor(PlayerColor.BLUE));
    }
  }
}
