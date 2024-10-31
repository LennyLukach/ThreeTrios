package model;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;

import view.ThreeTriosView;

/**
 * The test class for both ThreeTriosModel and ThreeTriosView.
 */
public class TestThreeTriosModel {
  ThreeTriosModel model;
  ThreeTriosModel modelInvalid;
  ThreeTriosView view;


  // setup model for all tests
  @Before
  public void setupThreeTriosConstructor() throws FileNotFoundException {
    model = new ThreeTriosModel("resources" + File.separator + "testDeck9+1.txt",
            "resources" + File.separator + "grid3x3Holes.txt");
    view = new ThreeTriosView(model);
  }

  // check that the model starts with the correct number of cards in each hand
  @Test
  public void testStartingHand() {
    model.startGame();
    Assert.assertEquals(model.getRedHand().size(), 15);
    Assert.assertEquals(model.getBlueHand().size(), 15);
  }


  // check that game state gets correctly changed after each event
  @Test
  public void testCorrectlyChangeGameStateAfterAction() {
    Assert.assertEquals(model.getGameState(), GameState.NOT_STARTED);
    model.startGame();
    Assert.assertEquals(model.getGameState(), GameState.PLACING);
    model.placeCard(0, 1, 0, PlayerColor.RED);
    Assert.assertEquals(model.getGameState(), GameState.BATTLE);

  }

  // checks that place card works as intented
  @Test
  public void testPlaceCard() {
    model.startGame();
    Card card = model.getRedHand().get(0);
    String name = card.getName();
    model.placeCard(0, 1, 0, PlayerColor.RED);
    Assert.assertEquals(model.getRedHand().size(), 14);
    Assert.assertEquals(model.getBlueHand().size(), 15);
    Assert.assertEquals(model.getGrid().getCard(0, 1).getName(), name);
  }

  // checks that battle flips over one card without a combo move
  @Test
  public void testBattleFlipOne() {
    model.startGame();

    model.placeCard(0, 1, 0, PlayerColor.RED);

    model.battle(0, 1);

    model.placeCard(1, 1, 0, PlayerColor.BLUE);

    model.battle(1, 1);

    model.placeCard(1, 2, 0, PlayerColor.RED);

    model.battle(1, 2);

    model.placeCard(2, 2, 0, PlayerColor.BLUE);

    model.battle(2, 2);

    Assert.assertEquals(model.getGrid().getCard(1, 2).getColor(), PlayerColor.BLUE);
  }

  // checks that battle can flip over cards in a combo move
  @Test
  public void testBattleChainFlipTwo() {
    model.startGame();

    model.placeCard(1, 2, 1, PlayerColor.RED);

    model.battle(1, 2);

    model.placeCard(1, 1, 0, PlayerColor.BLUE);

    model.battle(1, 1);
    Assert.assertEquals(model.getGrid().getCard(1, 1).getColor(), PlayerColor.BLUE);
    Assert.assertEquals(model.getGrid().getCard(1, 2).getColor(), PlayerColor.BLUE);


    model.placeCard(1, 0, 1, PlayerColor.RED);

    model.battle(1, 0);


    Assert.assertEquals(model.getGrid().getCard(1, 0).getColor(), PlayerColor.RED);
    Assert.assertEquals(model.getGrid().getCard(1, 1).getColor(), PlayerColor.RED);
    Assert.assertEquals(model.getGrid().getCard(1, 2).getColor(), PlayerColor.RED);
  }

  // checks that placeCard throws when placing to a hole
  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardToHole() {
    model.startGame();
    model.placeCard(0, 0, 0, PlayerColor.RED);
  }

  // checks that placeCard throws when placing to an occupied cell
  @Test(expected = IllegalStateException.class)
  public void testPlayCardToOccupied() {
    model.startGame();
    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.placeCard(0, 1, 0, PlayerColor.BLUE);
  }

  // checks that a full game can finish
  @Test
  public void testIsGameOver() {
    model.startGame();
    Assert.assertFalse(model.isGameOver());
    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.battle(0, 1);
    model.placeCard(0, 2, 0, PlayerColor.RED);
    model.battle(0, 2);
    model.placeCard(1, 0, 0, PlayerColor.RED);
    model.battle(1, 0);
    model.placeCard(1, 1, 0, PlayerColor.RED);
    model.battle(1, 1);
    model.placeCard(1, 2, 0, PlayerColor.RED);
    model.battle(1, 2);
    model.placeCard(2, 0, 0, PlayerColor.RED);
    model.battle(2, 0);
    model.placeCard(2, 2, 0, PlayerColor.RED);
    model.battle(2, 2);
    Assert.assertTrue(model.isGameOver());
  }

  // checks that a finished game can determine Red Player wins
  @Test
  public void testDetermineWinnerRed() {
    model.startGame();
    model.placeCard(0, 1, 0, PlayerColor.BLUE);
    model.battle(0, 1);
    model.placeCard(0, 2, 0, PlayerColor.RED);
    model.battle(0, 2);
    model.placeCard(1, 0, 0, PlayerColor.RED);
    model.battle(1, 0);
    model.placeCard(1, 1, 0, PlayerColor.RED);
    model.battle(1, 1);
    model.placeCard(1, 2, 0, PlayerColor.RED);
    model.battle(1, 2);
    model.placeCard(2, 0, 0, PlayerColor.RED);
    model.battle(2, 0);
    model.placeCard(2, 2, 0, PlayerColor.RED);
    model.battle(2, 2);
    Assert.assertEquals(model.determineWinner(), PlayerColor.RED);
  }

  // checks that a finished game can determine Blue Player wins
  @Test
  public void testDetermineWinnerBlue() {
    model.startGame();
    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.battle(0, 1);
    model.placeCard(0, 2, 0, PlayerColor.BLUE);
    model.battle(0, 2);
    model.placeCard(1, 0, 0, PlayerColor.RED);
    model.battle(1, 0);
    model.placeCard(1, 1, 0, PlayerColor.BLUE);
    model.battle(1, 1);
    model.placeCard(1, 2, 0, PlayerColor.RED);
    model.battle(1, 2);
    model.placeCard(2, 0, 0, PlayerColor.RED);
    model.battle(2, 0);
    model.placeCard(2, 2, 0, PlayerColor.BLUE);
    model.battle(2, 2);
    Assert.assertEquals(model.determineWinner(), PlayerColor.BLUE);
  }

  // checks that a finished tied game returns null
  @Test
  public void testDetermineWinnerTied() {
    model.startGame();
    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.battle(0, 1);
    model.placeCard(0, 2, 0, PlayerColor.RED);
    model.battle(0, 2);
    model.placeCard(1, 0, 0, PlayerColor.RED);
    model.battle(1, 0);
    model.placeCard(1, 1, 0, PlayerColor.RED);
    model.battle(1, 1);
    model.placeCard(1, 2, 0, PlayerColor.RED);
    model.battle(1, 2);
    model.placeCard(2, 0, 0, PlayerColor.RED);
    model.battle(2, 0);
    model.placeCard(2, 2, 0, PlayerColor.RED);
    model.battle(2, 2);
    Assert.assertNull(model.determineWinner());
  }

  // checks that model throws when the grid is invalid
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGridArr() throws FileNotFoundException {
    modelInvalid = new ThreeTriosModel("resources" + File.separator + "testDeck9+1.txt",
            "resources" + File.separator + "grid3x3InvalidArr.txt");
    modelInvalid.startGame();
  }

  // checks that the model throws when grid is even
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGridEven() throws FileNotFoundException {
    modelInvalid = new ThreeTriosModel("resources" + File.separator + "testDeck9+1.txt",
            "resources" + File.separator + "grid5x5Even.txt");
    modelInvalid.startGame();
  }

  // checks that the view correctly outputs a text representation of the game
  @Test
  public void testToStringBasicGame() {
    model.startGame();

    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.battle(0, 1);
    Assert.assertEquals(
            "Player: BLUE\n" +
                    " R_\n" +
                    "___\n" +
                    "_ _\n" +
                    "Hand:\n" +
                    "WindBird 7 2 5 3\n" +
                    "WorldDragon 8 3 10 7\n" +
                    "KingMan 7 3 9 10\n" +
                    "FireBird 7 2 5 3\n" +
                    "C 7 3 9 10\n" +
                    "B 7 2 5 3\n" +
                    "E 8 3 10 7\n" +
                    "G 7 3 9 10\n" +
                    "I 7 2 5 3\n" +
                    "t 7 3 9 10\n" +
                    "u 7 2 5 3\n" +
                    "p 8 3 10 7\n" +
                    "s 7 3 9 10\n" +
                    "v 7 2 5 3", view.toString());
  }

  // checks that view correctly represents a battle
  @Test
  public void testToStringAfterBattle() {
    model.startGame();

    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.battle(0, 1);
    model.placeCard(0, 2, 0, PlayerColor.BLUE);
    model.battle(0, 2);
    Assert.assertEquals(
            "Player: RED\n" +
                    " RB\n" +
                    "___\n" +
                    "_ _\n" +
                    "Hand:\n" +
                    "WindBird 7 2 5 3\n" +
                    "WorldDragon 8 3 10 7\n" +
                    "KingMan 7 3 9 10\n" +
                    "FireBird 7 2 5 3\n" +
                    "C 7 3 9 10\n" +
                    "B 7 2 5 3\n" +
                    "E 8 3 10 7\n" +
                    "G 7 3 9 10\n" +
                    "I 7 2 5 3\n" +
                    "t 7 3 9 10\n" +
                    "u 7 2 5 3\n" +
                    "p 8 3 10 7\n" +
                    "s 7 3 9 10\n" +
                    "v 7 2 5 3", view.toString());
  }

  // checks that startGame throws when game has already started
  @Test(expected = IllegalStateException.class)
  public void testStartGameWhenAlreadyStarted() {
    model.startGame();
    model.startGame();
  }

  // checks that placeCard throws when not in placing stage
  @Test(expected = IllegalStateException.class)
  public void testPlaceCardWhenNotPlacingStage() {
    model.placeCard(0, 0, 0, PlayerColor.RED);
  }

  // checks that placeCard throws when row is out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardRowOutOfBounds() {
    model.startGame();
    model.placeCard(-1, 0, 0, PlayerColor.RED);
  }

  // checks that placeCard throws when col is out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardColOutOfBounds() {
    model.startGame();
    model.placeCard(0, -1, 0, PlayerColor.RED);
  }

  // checks that placeCard throws when color is null
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardWithNullColor() {
    model.startGame();
    model.placeCard(0, 0, 0, null);
  }

  // checks that battle throws when not in battle stage
  @Test(expected = IllegalStateException.class)
  public void testBattleWhenNotBattleStage() {
    model.startGame();
    model.battle(0, 0);
  }

  // checks that battle throws when row is out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void testBattleRowOutOfBounds() {
    model.startGame();
    model.placeCard(0, 0, 0, PlayerColor.RED);
    model.battle(0, 0);
    model.battle(-1, 0);
  }

  // checks that battle throws when Col is out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void testBattleColOutOfBounds() {
    model.startGame();
    model.placeCard(0, 0, 0, PlayerColor.RED);
    model.battle(0, 0);
    model.battle(0, -1);
  }

  // checks that battle throws when cell is empty
  @Test(expected = IllegalArgumentException.class)
  public void testBattleOnEmptyCell() {
    model.startGame();
    model.placeCard(0, 0, 0, PlayerColor.RED);
    model.battle(0, 0);
    model.battle(1, 1);
  }

  // checks that model throws when deckFile is null
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullDeckFile() throws FileNotFoundException {
    ThreeTriosModel modelNull = new ThreeTriosModel(null,
            "resources" + File.separator + "grid3x3NoHoles.txt");
    modelNull.startGame();
  }

  // checks that model throws when gridFile is null
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullGridFile() throws FileNotFoundException {
    ThreeTriosModel modelNull = new ThreeTriosModel(
            "resources" + File.separator + "testDeck9+1.txt", null);
    modelNull.startGame();
  }

  // checks that model throws when deck file does not exist
  @Test(expected = FileNotFoundException.class)
  public void testConstructorWithNonExistentDeckFile() throws FileNotFoundException {
    new ThreeTriosModel("resources" + File.separator + "nonExistentDeck.txt",
            "resources" + File.separator + "grid3x3NoHoles.txt");
  }

  // checks that model throws when deck file does not exist
  @Test(expected = FileNotFoundException.class)
  public void testConstructorWithNonExistentGridFile() throws FileNotFoundException {
    new ThreeTriosModel("resources" + File.separator + "testDeck9+1.txt",
            "resources" + File.separator + "nonExistentGrid.txt");
  }

  @Test
  public void testDeckTooSmall() throws FileNotFoundException {
    model = new ThreeTriosModel("resources" + File.separator + "smallDeck.txt",
            "resources" + File.separator + "grid3x3Holes.txt");
    model.startGame();
  }
}