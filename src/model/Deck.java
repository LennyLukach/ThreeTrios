package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Deck {

  private final List<Card> deck;

  public Deck(String filepath) throws FileNotFoundException {
    this.deck = loadCardsFromFile(filepath);
  }


  // loads all the cards from a file. The file should be in the following format:
  // 5 strings, the name, and then the string value of the attack in each direction
  // name,attackN,attackS,attackE,attackW
  public static List<Card> loadCardsFromFile(String filePath) throws FileNotFoundException {
    List<Card> cards = new ArrayList<>();

    // read the file and create a card for each line
    // add all 5 args to a list
    // the first index is the name
    // parse the next 4 as ints and those will be the attack values
    File file = new File(filePath);
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] values = line.split(" ");

      if (values.length != 5) { // there must be 5 values, else format is wrong
        throw new IllegalArgumentException("Invalid file format");
      }

      String name = values[0];
      List<Integer> attackValues = new ArrayList<>();
      for (int i = 1; i < values.length; i++) {
        if (values[i].equals("A")) {
          attackValues.add(10);
        }
        else if (Integer.parseInt(values[i]) < 0 && Integer.parseInt(values[i]) > 10) {
          attackValues.add(Integer.parseInt(values[i]));
        }
        else { // throw exception if the value is not 1-9 or A
          throw new IllegalArgumentException("Invalid attack value");
        }
      }
      cards.add(new Card(name, attackValues, null));
    }
    scanner.close();
    return cards;
  }

  public void shuffle() {
    Collections.shuffle(deck);
  }

  public List<Card> getDeck() {
    return deck;
  }

  public Card draw() {
    return deck.removeFirst();
  }

}
