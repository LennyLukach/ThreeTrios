package model;

import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;

import java.io.File;
import java.io.FileNotFoundException;

import view.ThreeTriosView;


public class TestThreeTriosModel {
  ThreeTriosModel model;
  ThreeTriosModel modelInvalid;
  ThreeTriosView view;


  // setup model for all tests
  @Before
  public void setupThreeTriosConstructor() throws FileNotFoundException {
    model = new ThreeTriosModel("resources" + File.separator + "testDeck9+1.txt", "resources" + File.separator + "grid3x3Holes.txt");
    view = new ThreeTriosView(model);
  }

  // check that the model starts with the correct number of cards in each hand
  @Test
  public void testStartingHand() {
    model.startGame();
    Assert.assertEquals(model.getRedHand().size(), 10);
    Assert.assertEquals(model.getBlueHand().size(), 10);
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

  @Test
  public void testPlaceCard() {
    model.startGame();
    Card card = model.getRedHand().get(0);
    String name = card.getName();
    model.placeCard(0, 1, 0, PlayerColor.RED);
    Assert.assertEquals(model.getRedHand().size(), 9);
    Assert.assertEquals(model.getBlueHand().size(), 10);
    Assert.assertEquals(model.getGrid().getCard(0, 1).getName(), name);
  }

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

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardToHole() {
    model.startGame();
    model.placeCard(0, 0,0, PlayerColor.RED);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardToOccupied() {
    model.startGame();
    model.placeCard(0, 1,0, PlayerColor.RED);
    model.placeCard(0, 1,0, PlayerColor.BLUE);
  }


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

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGridArr() throws FileNotFoundException {
    modelInvalid = new ThreeTriosModel("resources/testDeck9+1.txt", "resources/grid3x3InvalidArr.txt");
    modelInvalid.startGame();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGridEven() throws FileNotFoundException {
    modelInvalid = new ThreeTriosModel("resources" + File.separator + "testDeck9+1.txt", "resources" + File.separator + "grid5x5Even.txt");
    modelInvalid.startGame();
  }

  @Test
  public void testToStringBasicGame() {
    model.startGame();

    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.battle(0, 1);
    Assert.assertEquals(view.toString(),
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
            "I 7 2 5 3"
    );
  }

  @Test
  public void testToStringAfterBattle() {
    model.startGame();

    model.placeCard(0, 1, 0, PlayerColor.RED);
    model.battle(0, 1);
    model.placeCard(0, 2, 0, PlayerColor.BLUE);
    model.battle(0, 2);
    Assert.assertEquals(view.toString(),
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
            "I 7 2 5 3"
    );
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameWhenAlreadyStarted() {
    model.startGame();
    model.startGame();
  }

  @Test(expected = IllegalStateException.class)
  public void testPlaceCardWhenNotPlacingStage() {
    model.placeCard(0, 0, 0, PlayerColor.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardOutOfBounds() {
    model.startGame();
    model.placeCard(-1, -1, 0, PlayerColor.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardWithNullColor() {
    model.startGame();
    model.placeCard(0, 0, 0, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testBattleWhenNotBattleStage() {
    model.startGame();
    model.battle(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBattleOutOfBounds() {
    model.startGame();
    model.placeCard(0, 0, 0, PlayerColor.RED);
    model.battle(0, 0);
    model.battle(-1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBattleOnEmptyCell() {
    model.startGame();
    model.placeCard(0, 0, 0, PlayerColor.RED);
    model.battle(0, 0);
    model.battle(1, 1);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullDeckFile() throws FileNotFoundException {
    ThreeTriosModel modelNull = new ThreeTriosModel(null, "resources" + File.separator + "resources" + File.separator + "grid3x3NoHoles.txt");
    modelNull.startGame();
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullGridFile() throws FileNotFoundException {
    ThreeTriosModel modelNull = new ThreeTriosModel("resources" + File.separator + "testDeck9+1.txt", null);
    modelNull.startGame();
  }

  @Test(expected = FileNotFoundException.class)
  public void testConstructorWithNonExistentDeckFile() throws FileNotFoundException {
    new ThreeTriosModel("resources" + File.separator + "nonExistentDeck.txt", "resources" + File.separator + "grid3x3NoHoles.txt");
  }
}