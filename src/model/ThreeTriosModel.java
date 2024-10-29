package model;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * This is the implementation for the model for the game Three Trios.
 */
public class ThreeTriosModel implements IThreeTrioModel {

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

  public void battle(int row, int col, PlayerColor color) {
    if (gameState != GameState.BATTLE) {
      throw new IllegalStateException("Game is not in battle stage.");
    }

    // check north
    attackNorth(row, col, color);

    // check south
    attackSouth(row, col, color);

    // check east
    attackEast(row, col, color);

    // check west
    attackWest(row, col, color);
  }

  private void attackNorth(int row, int col, PlayerColor color) {
    boolean cardIsOpponents = grid.getCard(row - 1, col).getColor() != color;
    boolean cardExists = grid.getCard(row - 1, col) != null;
    if (cardIsOpponents && cardExists) {
      int attack = grid.getCard(row, col).getAttack(Direction.NORTH);
      int defense = grid.getCard(row - 1, col).getAttack(Direction.SOUTH);
      if (attack > defense) {
        grid.getCard(row - 1, col).setColor(color);
        battle(row - 1, col, color);
      }
    }
  }

  private void attackSouth(int row, int col, PlayerColor color) {
    boolean cardIsOpponents = grid.getCard(row + 1, col).getColor() != color;
    boolean cardExists = grid.getCard(row + 1, col) != null;
    if (cardIsOpponents && cardExists) {
      int attack = grid.getCard(row, col).getAttack(Direction.SOUTH);
      int defense = grid.getCard(row - 1, col).getAttack(Direction.NORTH);
      if (attack > defense) {
        grid.getCard(row + 1, col).setColor(color);
        battle(row + 1, col, color);
      }
    }
  }

  private void attackEast(int row, int col, PlayerColor color) {
    boolean cardIsOpponents = grid.getCard(row, col + 1).getColor() != color;
    boolean cardExists = grid.getCard(row, col + 1) != null;
    if (cardIsOpponents && cardExists) {
      int attack = grid.getCard(row, col).getAttack(Direction.EAST);
      int defense = grid.getCard(row, col + 1).getAttack(Direction.WEST);
      if (attack > defense) {
        grid.getCard(row, col + 1).setColor(color);
        battle(row, col + 1, color);
      }
    }
  }

  private void attackWest(int row, int col, PlayerColor color) {
    boolean cardIsOpponents = grid.getCard(row, col - 1).getColor() != color;
    boolean cardExists = grid.getCard(row, col - 1) != null;
    if (cardIsOpponents && cardExists) {
      int attack = grid.getCard(row, col).getAttack(Direction.WEST);
      int defense = grid.getCard(row, col - 1).getAttack(Direction.EAST);
      if (attack > defense) {
        grid.getCard(row, col - 1).setColor(color);
        battle(row, col - 1, color);
      }
    }
  }

  public boolean isGameOver() {
    for (int row = 0; row < grid.getGrid().length; row++) {
      for (int col = 0; col < grid.getGrid()[0].length; col++) {
        if (grid.getCard(row, col) == null) {
          return false;
        }
      }
    }
    return true;
  }

  public PlayerColor determineWinner() {
    int redCount = 0;
    int blueCount = 0;
    PlayerColor winner = null;

    for (int row = 0; row < grid.getGrid().length; row++) {
      for (int col = 0; col < grid.getGrid()[0].length; col++) {
        if (grid.getCard(row, col).getColor() == PlayerColor.RED) {
          redCount++;
        } else if (grid.getCard(row, col).getColor() == PlayerColor.BLUE) {
          blueCount++;
        }
      }
    }

    redCount += redHand.size();
    blueCount += blueHand.size();

    if (redCount > blueCount) {
      winner = PlayerColor.RED;
    } else if (blueCount > redCount) {
      winner = PlayerColor.BLUE;
    }

    return winner;
  }


  // i changed setColor to return a card so
  // we could draw to the hand and set the color of the
  // card at the same time.
  private void fillHands() {
    while (!deck.getDeck().isEmpty()) {
      redHand.add(deck.draw().setColor(PlayerColor.RED));
      blueHand.add(deck.draw().setColor(PlayerColor.BLUE));
    }
  }
}
