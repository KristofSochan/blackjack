import java.util.ArrayList;
import java.util.List;
import java.util.*;  
import java.util.Scanner;

/*
 * Tester class that creates BlackJackGame object and runs game
 *
 *  @author Kristof Sochan
 */
public class BlackJack {
  public static void main( String[] args ) {
    BlackJackGame game = new BlackJackGame();
    game.play();
  }
}

/*
 * Main game class that manages Deck, Player, and Dealer classes
 */
class BlackJackGame {
  private Deck deck;
  private Dealer dealer;
  private Player player;
  
  public BlackJackGame() {
    // Creates instance of Deck class
    deck = new Deck();    
    
    // Creates instance of Dealer class
    dealer = new Dealer();
    
    // Create the player hand (code not shown):
    player = new Player();
  }
  
  /*
   * Main structure of game that allows one round of game play
   * Ran every time user requests another game
   */
  public void play() {    
    // Deal 2 cards to the dealer
    Card dealtCard = deck.deal();
    dealer.addDealerCard( dealtCard );
    dealtCard = deck.deal();
    dealer.addDealerCard( dealtCard );
    
    // Deal 2 cards to the hand (don't show the cards yet)
    dealtCard = deck.deal();
    player.addCard( dealtCard );
    dealtCard = deck.deal();
    player.addCard( dealtCard );
   
    // Welcome message for players
    System.out.println("_____________________________________\n");
    System.out.println("Welcome to Sochan's Blackjack Casino");
    System.out.println("_____________________________________\n");
    
    System.out.println("Current chip count: " + player.getPlayerChipCount());
    player.getChipWager();
    
    System.out.println("\n");
    
    // Display dealer top card
    System.out.println("Dealer top card: ");
    System.out.println(dealer.getDealerTopCardString());
    System.out.println( "\tDealer: " + dealer.getDealerTopCardValue() );
    
    System.out.println("\n");
   
    // Then you can display the cards:
    System.out.println("Player hand: ");
    System.out.println(player.getPlayerHand());
    // and the value of the hand:
    System.out.println( "Player: " + player.getPlayerHandValue( ) );

    // then ask user for additional cards (repeatedly):
    while( player.getUserChoice().equals("Hit") ) {
      // If they "hit", then deal another card to the hand
      dealtCard = deck.deal();
      player.addCard( dealtCard );
      
      // Display dealer top card
      System.out.println("Dealer top card: ");
      System.out.println(dealer.getDealerTopCardString());
      System.out.println( "Dealer: " + dealer.getDealerTopCardValue() );
    
      System.out.println("\n");
   
      // Then you can display the cards:
      System.out.println("Player hand: ");
      System.out.println(player.getPlayerHand());
      // and the value of the hand:
      System.out.println( "Player: " + player.getPlayerHandValue( ) );
                  
      if (player.getPlayerHandValue() > 21) {
        System.out.println("Bust!");
        break;
      }
    }
    
    while (dealer.checkForDealer()) {
      dealtCard = deck.deal();
      dealer.addDealerCard( dealtCard );
      
      // Display dealer top card
      System.out.println("Dealer cards: ");
      System.out.println(dealer.getDealerHand());
       System.out.println( "Dealer: " + dealer.getDealerHandValue());
    
      System.out.println("\n");
    
      // Then you can display the cards:
      System.out.println("Player hand: ");
      System.out.println(player.getPlayerHand());
      // and the value of the hand:
      System.out.println( "Hand value: " + player.getPlayerHandValue( ) );
    }
    
    if (dealer.getDealerHandValue() > 21) {
      System.out.println("You win! Thanks for playing.");
      player.playerWin();
      player.updateChipCount("win");
      System.out.println("Chip count: " + player.getPlayerChipCount());
    }
    else if (dealer.getDealerHandValue() < player.getPlayerHandValue() ) {
      System.out.println("You win! Thanks for playing.");
      player.updateChipCount("win");
      System.out.println("Chip count: " + player.getPlayerChipCount());
    }
    else if (dealer.getDealerHandValue() > player.getPlayerHandValue()) {
      System.out.println("You lose! Thanks for playing.");
      dealer.dealerWin();
      player.updateChipCount("lose");
      System.out.println("Chip count: " + player.getPlayerChipCount());
    }
    else if (dealer.getDealerHandValue() == player.getPlayerHandValue()) {
      System.out.println("Push! Thanks for playing.");
      System.out.println("Chip count: " + player.getPlayerChipCount());
    }
    
    if (player.promptNewGame().equals("Y")) {
      reset();
    }

  }
  
  public void reset() {
    deck = new Deck();
    play();
  }
}
/*
 * Dealer class that interacts with Hand class. Intermediary between DealerTester and Hand
 */
class Dealer {
  private Hand dealerHand;
  private int dealerWinTally = 0;
  
  public Dealer() {
    Hand dealerHand = new Hand();
    this.dealerHand = dealerHand;
  }
  
  public void addDealerCard(Card addedCard) {
    dealerHand.add(addedCard);
  }
  
  /*
   * Dealer logic that determines whether the dealer should draw a card or not
   * 
   * @return true if dealerHand value is less than 16 or is a soft 17
   * @return false if dealerHand is 17 or greater with no ace
   */  
  public boolean checkForDealer() {
    int dealerHandValue = dealerHand.getHandValue();
    if (dealerHandValue <= 16) return true;
    else if (dealerHandValue == 17 && dealerHand.checkForAce() == true) return true;
    else return false;
  }
  
 /*
  * Accessor method that calls Hand class for the current dealerHand ArrayList
  *
  * @return String of all current cards in hand
  */
  public String getDealerTopCardString() {
    String formattedDealerCard = dealerHand.getTopCard().toString();
    return formattedDealerCard;
  }
  
  public int getDealerTopCardValue() {
    Card dealerCard = dealerHand.getTopCard();
    return dealerCard.getValue();
  }
  
  public String getDealerHand() {
    return dealerHand.toString();
  }
  
  public int getDealerHandValue() {
    return dealerHand.getHandValue();
  }
  
  public void dealerWin() {
    dealerWinTally++;
  }
  
  public int getDealerWins() {
    return dealerWinTally;
  }
}

/*
 * Player class that manages built in Hand class
 */
class Player {
  private Hand playerHand;
  private String playerName;
  private Scanner input;
  private int playerWinTally = 0;
  private int chipCount = 100;
  private int chipWager;
  
  public Player() {
    Hand playerHand = new Hand();
    this.playerHand = playerHand;
    playerName = "player1";
  }
  
  public Player(String name) { // Overloaded constructor if player name is given
    Hand playerHand = new Hand();
    this.playerHand = playerHand;
    playerName = name;
  }
  
  /*
   * Scanner method to ask for users choice (Hit or Stand)
   * 
   * @return Player's decision to deal another card or end the game
   */  
  public String getUserChoice() {
    Scanner input = new Scanner(System.in);
    System.out.println("\"Hit\" or \"Stand\"?"); 
    String userChoice = input.nextLine();
    return userChoice;
  }
  
  public void getChipWager() {
    Scanner input = new Scanner(System.in);
    System.out.print("Enter a wager: "); 
    chipWager = input.nextInt();
  }
  
  public String promptNewGame() {
    Scanner input = new Scanner(System.in);
    System.out.println("Play again? (Y/N)");
    String newGame = input.nextLine();
    return newGame;
  }
  public void addCard(Card addedCard) {
    playerHand.add(addedCard);
  }
  
  /*
   * Accessor method that calls Hand class for hand value
   *
   * @return integer of current hand value
   */
  public int getPlayerHandValue() {
    return playerHand.getHandValue();
  }
  
  /*
   * Accessor method that calls Hand class for the current playerHand ArrayList
   *
   * @return String of all current cards in hand
   */
  public String getPlayerHand() {
    return playerHand.toString();
  }

  public void playerWin() {
    playerWinTally++;
  }
  
  public int getPlayerWins() {
    return playerWinTally;
  }
  
  public int getPlayerChipCount() {
    return chipCount;
  }
  
  /*
   * Awards player or taking chips based off game result
   * @param winLoss "win" or "loss" passed in from BlackJackGame
   */
  public void updateChipCount(String winLoss) {
    if (winLoss.equals("win") && getPlayerHandValue() == 21) {
      chipCount += (int)(chipWager*1.5);
    }
    else if (winLoss.equals("win")) {
      chipCount += chipWager;
    }
    else {
      chipCount -= chipWager;
    }
  }
}

/*
 * Hand class that stores hand ArrayList, adds cards to hand, and returns current hand info
 */
class Hand {
  private ArrayList<Card> hand;
  private boolean hasAce;
  
  public Hand() {
    hand = new ArrayList<>();
    hasAce = false;
  }
  
  public void add(Card addedCard) {
    hand.add(addedCard);
  }
  
  /*
   * Calculates the current value of the player's hand
   *
   * @return integer of hand value, adjusted if over 21 and deck has an ace
   */
  public int getHandValue() {
    int handValue = 0;
    for (Card cards : hand) {
      if (handValue > 21) {
        for (Card cardsChecker : hand) {
          if (cardsChecker.getValue() == 11) {
            hasAce = true;
            handValue -= 10;
          }
        }
      }
      
      handValue += cards.getValue();
    }
    return handValue;
  }
  
  /* 
   * toString method that displays all current cards in hand line by line
   *
   * @return Formatted string of current hand
   */
  public String toString() {
    String formattedHand = "";
    for (int i = 0; i < hand.size(); i++) {
      formattedHand += hand.get(i).toString() + "\n";
    }
    return formattedHand;
  }
  
  public boolean checkForAce() { // Accessor method
    return hasAce;
  }
  
  public Card getTopCard() {
    return hand.get(0);
  }
}

/*
 * Holds all information of a given card
 */
class Card {
  // Instance variables
  private String suit; 
  private int rank;
  private int value;
  private String pointString;
  
  /*
   * Constructor for Card class that distinguishes between
   * number and face cards
   */
  public Card(int cardRank, String cardSuit) { // Constructor
    rank = cardRank;
    suit = cardSuit;
    if (cardRank == 1) {
      value = 11;
    }
    if (cardRank <= 10) {
      value = rank;
    }
    else {
      value = 10; // For face cards
    }
  }
  
  /*
   * Formatted toString method for Card info
   * @return Card with correct value and suit
   */
  public String toString() { // Formatted card information
    String[] cardNames = {"Ace", "Two", "Three", "Four", "Five",
                          "Six", "Seven", "Eight", "Nine", "Ten", "Jack",
                          "Queen", "King"};
    if (rank == 1) {
      pointString = " (point value = 11)"; // For aces
    }
    else {
      pointString = " (point value = " + value + ")"; // All other cards
    }
    // Uses cardNames array to get number word or face name from rank
    return cardNames[rank-1] + " of " + suit + pointString;
  }
  
  public int getValue() { // Accessor method
    return value;
  }
  
}

/*
 * Holds 52 Card objects in an ArrayList deckOfCards
 */
class Deck {
  // Instance variables
  private ArrayList<Card> deckOfCards;
  private String returnDeck;
  
  /*
   * Deck Constructor that creates deck of 52 cards then shuffles
   */
  public Deck() {
    deckOfCards = new ArrayList<>();
    int currentRank = 1;
    int currentSuit = 0;
    for (int i = 0; i < 52; i++) {
      String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades", ""};
      
      // Creates cards and then adds to deckOfCards one at a time
      Card newCard = new Card(currentRank, suits[currentSuit]);
      deckOfCards.add(newCard);
      if (currentRank == 13) { // To change suits after king
        currentRank = 1;
        currentSuit++;
      }
     else {
       currentRank++; // Increment rank by 1
     }
    
    // Shuffle the ordered deck of cards
    this.shuffle();
    
    }
  }
  
  public Card deal() { // Deal a card to the user
    Card dealedCard = deckOfCards.get(deckOfCards.size()-1); // Temporary card variable
    deckOfCards.remove(deckOfCards.size()-1); // Removes dealed card from deck
    return dealedCard;
  }
  
  /*
   * Formatted list of 52 cards in deck
   * @return String of all cards
   */
  public String toString() { 
    String returnDeck = "";
    for (Card card : deckOfCards) {
      returnDeck += card.toString() + "\n";
    }
    return returnDeck;
  }
  
  public void shuffle() { // Uses the shuffle method in Collections to randomize deckOfCards
    Collections.shuffle(deckOfCards);
  }
  
  /*
   * Mutator method that uses the efficient selection shuffle
   */
  public void efficientShuffle() {
    for (int k = 51; k > 0; k--) {
      int r = (int)(Math.random()*k);
      Card tempCard = deckOfCards.get(k);
      deckOfCards.set(k, deckOfCards.get(r));
      deckOfCards.set(r, tempCard);
    }
  }
  
  
}