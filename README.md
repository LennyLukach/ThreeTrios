# ThreeTrioGame

`ThreeTrioModel` implements the core functionality and rules for a two-player card-based grid game, 
referred to as **Three Trios**. This code lays out the structure for managing gameplay, 
enforcing game mechanics, and determining victory conditions.

## Overview

The primary challenge this codebase addresses is managing the state, actions, and turn-based logic 
in a complex card game. Key assumptions include:
- **Gameplay Knowledge**: Familiarity with basic turn-based and grid-based mechanics, 
such as turn alternation, card placement, and adjacent cell targeting.
- **Extensibility**: The codebase is structured to allow extensions, 
such as additional battle rules, new grid shapes, or varied winning conditions.
- **Prerequisites**: This model requires basic understanding of Java as well as filesystems.

### Main Features

1. **Game Initialization** - Sets up the game with decks, hands, and a grid.
2. **Card Placement and Battle Mechanics** - Manages the rules for placing and 
battling cards on the grid.
3. **Game States** - Tracks and manages the progression of the game.
4. **Player Turns** - Alternates between players and tracks the current player.
5. **Win Condition Checking** - Determines if the game has ended and which player won.

## Classes


## Example Usage

Below is a quick code example illustrating how to set up and play a turn in the **ThreeTrios** 
game model.

```java
ThreeTrioModel model = new ThreeTrioModel(exampleDeckFile, exampleGridFile);
model.startGame();

// Retrieve the current player and make a move
PlayerColor currentPlayer = model.getCurrentPlayer();
model.placeCard(2, 3, 0, currentPlayer);  // place card at grid (2,3) from player's hand

// Engage in battle phase if the game is not over
if (!model.isGameOver()) {
    model.battle(2, 3, currentPlayer);
}

// Check if game ended and display the winner
if (model.isGameOver()) {
    PlayerColor winner = model.determineWinner();
    System.out.println("The winner is: " + winner);
}
