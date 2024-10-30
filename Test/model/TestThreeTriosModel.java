package model;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class TestThreeTriosModel {
  ThreeTriosModel model;

  @Test
  public void testThreeTriosConstructor() throws FileNotFoundException {
    model = new ThreeTriosModel("testDeck9+1","grid3x3NoHoles.txt");
    model.startGame();

    assertEquals()
  }
}
