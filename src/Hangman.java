import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Hangman {

	private String playerName; 		//to be implemented
	private int playerScore = 0;	//to be implemented
	private String word;
	private int triesLeft = 9;
	private boolean wordHasBeenFound= false;
	private ArrayList<String> guessedLetters = new ArrayList<>();
	private Scanner gameScanner = new Scanner(System.in);
	
	public Hangman() {
		this.word = new Word().getWord();
	}
	
	public Hangman (String wordString) {
		this.word = wordString;
	}
	
	public void startProgram() {
		System.out.println("---- Welcome to the world's best Hangman game ever! ----\n");
	    System.out.print(buildStartMenu());
	    String input;
	    
	    do {
		    System.out.print("\nPlease press a key to select an option: ");
	    	input = this.gameScanner.next();
	    	if (input.equals("1")) {
		    	newGameMenu();
	    	} else if (input.equals("2")) {
		    	highScoreMenu();
	    	} else if (input.equals("0")) {
		    	if(wantsToQuit()) {
					quitGame();
		    	} else {
		    		input = "-1";
		    		continue;
		    	}
	    	} else {
	    		System.out.print("Invalid input.\n");	    		
	    	}
	    } while (!(input.equals("1") || input.equals("2") || input.equals("0")));  
	}
	
	public void resetGame() {
		this.playerScore = 0;
		this.word = new Word().getWord();
		this.triesLeft = 9;
		this.guessedLetters.clear();
		startProgram();
	}
	
	public void quitGame() {
		System.out.println("\nBye bye!");
		this.gameScanner.close();
		System.exit(0);
	}
	
	public void playSingleGame() {
		System.out.println(buildGameRoundBoard(updateWord(this.word, this.guessedLetters)));
		while (this.triesLeft > 0 && !this.wordHasBeenFound) {
			System.out.print("\nPlease enter a letter or guess the entire word\n(or write 'resetgame' to return to start menu or 'quitgame' to quit the program): ");
			String nextLetter = this.gameScanner.next();
			
			//player enters a word
			if (nextLetter.length() > 1) {
				if (wordIsGuessed(nextLetter)) {
					this.wordHasBeenFound = true;
					winGame(nextLetter.toUpperCase());
					// TODO: IMPLEMENT BONUS FOR GUESSING ENTIRE WORD					
				} else if (nextLetter.equals("resetgame")) {
					if(wantsToReset()) {
						System.out.println("\nresetting the game...");
						resetGame();
					} else {
						continue;
					}
				} else if (nextLetter.equals("quitgame")) {
			    	if(wantsToQuit()) {
			    		quitGame();
			    	} else {
			    		continue;
			    	}
				} else {
					System.out.println("\n  ** Nope, that's not the correct word! **");
					this.triesLeft--;
				}
			//player enters a single Character
			} else {
				//input is a valid letter
				if (inputIsValid(nextLetter, this.guessedLetters)) {
					//letter exists in word
					if(this.word.contains(nextLetter)) {
						this.guessedLetters.add(nextLetter);
						Collections.sort(this.guessedLetters);
						//check for completed word
						if(wordIsGuessed(updateWord(this.word, this.guessedLetters))) {
							System.out.println("\n  ** Bravo, you've guessed it!! **");
							this.wordHasBeenFound = true;
							winGame(updateWord(this.word, this.guessedLetters));
						} else {
							System.out.println("\n  ** :) Nice guess! **");							
						}
					//letter does not exist in word
					} else {
						this.guessedLetters.add(nextLetter);
						Collections.sort(this.guessedLetters);
						System.out.println("\n  ** :( Nope, no " + nextLetter + "'s in this word! **");
						this.triesLeft--;								
					}					
				//invalid input
				} else {
					System.out.println("\n  ** Enter a valid letter that you haven't tried before. **");
				}
			}
			System.out.println(buildGameRoundBoard(updateWord(this.word, this.guessedLetters)));
		}
		//player runs out of tries = looses
		if (this.triesLeft == 0) {
			loseGame();
		}
	}
  
	/**
	 * Updates the String displaying the hidden game word to the Player. For each
	 * letter that the Player has not guessed, a hyphen is shown instead.	  
	 * @param gameWord			The game's current word 
	 * @param guessedLetters	an ArrayList containing every previously guessed letter
	 * @return					A String representation of the game word according
	 * to the letters that the Player has guessed so far.
	 */
	public String updateWord(String gameWord, ArrayList<String> guessedLetters) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < gameWord.length(); i++) {
			if(guessedLetters.contains(Character.toString(gameWord.charAt(i)))) {
				sb.append(Character.toUpperCase(gameWord.charAt(i)));
			} else {
				sb.append("-");
			}
		}
		return sb.toString();	
	}
	
	/**
	 * Returns true if the player writes a letter and if that letter has not 
	 * been guessed before. Returns false otherwise.
	 * @param input				a String of length 1 written in the console by the Player
	 * @param guessedLetters	an ArrayList containing every previously guessed letter
	 * @return					true if input is a letter and it has not been previously
	 * guessed, false otherwise
	 */
	public boolean inputIsValid(String input, ArrayList<String> guessedLetters) {
		return (Character.isLetter(input.charAt(0)) && !guessedLetters.contains(input));
	}
	
	public boolean wordIsGuessed(String guess) {
		return guess.equalsIgnoreCase(this.word);
	}
	
	public void winGame(String word) {
		//TODO: implement high score
		
		System.out.print(buildWinGameBoard(word));
	    String input;
	    
	    do {
		    System.out.print("\nPlease press a key to select an option: ");
	    	input = this.gameScanner.next();
	    	if (input.equals("1")) {
		    	resetGame();
	    	} else if (input.equals("0")) {
		    	if(wantsToQuit()) {
		    		quitGame();
		    	} else {
		    		input = "-1";
		    		continue;		    		
		    	}
	    	} else {
	    		System.out.print("Invalid input.\n");
	    		continue;
	    	}
	    } while (!(input.equals("1") || input.equals("0")));
	}

	public void loseGame() {
		System.out.print(buildLoseGameBoard());		
	    String input;
	    
	    do {
	    	System.out.print("Please press a key to select an option: ");
	    	input = this.gameScanner.next();
	    	if (input.equals("1")) {
		    	resetGame();
	    	} else if (input.equals("0")) {
	    		if(wantsToQuit()) {
	    			quitGame();
		    	} else {
		    		input = "-1";
		    		continue;		    		
		    	}
    		} else {
	    		System.out.print("Invalid input.\n");	    		
    		}
	    } while (!(input.equals("1") || input.equals("0")));
	}	
	
	public boolean wantsToReset() {
		System.out.print("Do you really want to end this game and return to the start menu? (\"y\" = yes, \"n\" = no): ");
		boolean wantsToReset = false;
		String input;
		
		do {
			input = this.gameScanner.next();
			if (input.equals("y")) {
				wantsToReset = true;										
			} else if (input.equals("n")) {
				wantsToReset = false;
			} else {
				System.out.print("Invalid input.\nDo you really want to end this game and return to the start menu? (\"y\" = yes, \"n\" = no): ");
			}
		} while (!(input.equals("y") || input.equals("n")));
		return wantsToReset;
	}

	public boolean wantsToQuit() {
		System.out.print("Do you really want to quit the application? (\"y\" = yes, \"n\" = no): ");
		boolean wantsToQuit = false;
		String input;
		
		do {
			input = this.gameScanner.next();
			if (input.equals("y")) {
				wantsToQuit = true;
			} else if (input.equals("n")) {
				wantsToQuit = false;
			} else {
				System.out.print("Invalid input. Do you really want to quit the application? (\"y\" = yes, \"n\" = no): ");
			}
		} while (!(input.equals("y") || input.equals("n")));
		return wantsToQuit;	
	}
		
	public void newGameMenu() {
	    System.out.print(buildNewGameMenu());
	    String input;
	    
	    do {
		    System.out.print("\nPlease press a key to select an option: ");
	    	input = this.gameScanner.next();
	    	if (input.equals("1")) {
		    	playSingleGame();
	    	} else if (input.equals("2")) {
		    	System.out.println("To be implemented");
	    	} else if (input.equals("0")) {
		    	resetGame();
	    	} else {
	    		System.out.print("Invalid input.\n");	    		
	    	}
	    } while (!(input.equals("1") || input.equals("2") || input.equals("0")));
	}
	
	public void highScoreMenu() {
	    System.out.print(buildHighScoreMenu());
	    String input;
	    
	    do {
		    System.out.print("\nPlease press a key to select an option: ");
	    	input = this.gameScanner.next();
	    	if (input.equals("1")) {
		    	System.out.println("To be implemented");
	    	} else if (input.equals("2")) {
	    		System.out.println("To be implemented");
	    	} else if (input.equals("0")) {
	    		resetGame();
	    	} else {
	    		System.out.print("Invalid input.\n");	    		
	    	}
	    } while (!(input.equals("1") || input.equals("2") || input.equals("0")));
	}
	
	public String buildStartMenu() {
		StringBuilder menu = new StringBuilder();
	    menu.append("\n");
	    menu.append("========== START ===========\n");
	    menu.append("|| 1 - Play a new game    ||\n");
	    menu.append("||                        ||\n");
	    menu.append("|| 2 - View high scores   ||\n");
	    menu.append("||                        ||\n");
	    menu.append("|| 0 - Quit application   ||\n");
	    menu.append("============================\n");
	    return menu.toString();
	}

	public String buildNewGameMenu() {
	    StringBuilder menu = new StringBuilder();
	    menu.append("\n");
	    menu.append("========= NEW GAME ==========\n");
	    menu.append("|| 1 - Play a single game  ||\n");
	    menu.append("||                         ||\n");
	    menu.append("|| 2 - Play a 5-word game  ||\n");
	    menu.append("||                         ||\n");
	    menu.append("|| 0 - Back to main menu   ||\n");
	    menu.append("=============================\n");
	    return menu.toString();
	}
	
	public String buildHighScoreMenu() {
	    StringBuilder menu = new StringBuilder();
	    menu.append("\n");
	    menu.append("============ HIGH SCORES =============\n");
	    menu.append("|| 1 - Show single game highscores  ||\n");
	    menu.append("||                                  ||\n");
	    menu.append("|| 2 - Show 5-word game highscores  ||\n");
	    menu.append("||                                  ||\n");
	    menu.append("|| 0 - Back to main menu            ||\n");
	    menu.append("======================================\n");
	    return menu.toString();
	}
	
	public String buildGameRoundBoard(String word) {
		StringBuilder gameBoard = new StringBuilder();
		gameBoard.append("\n");
		gameBoard.append("========================================== SINGLE GAME ===================================\n");
		gameBoard.append("||                  ||                                                                    \n");
		gameBoard.append("||                  || CURRENT WORD: " + word + "                                         \n");
		gameBoard.append("||                  ||                                                                    \n");
		gameBoard.append("||    (hangman)     ||                                                                    \n");
		gameBoard.append("||                  || Previous letters: " + this.guessedLetters.toString() + "           \n");
		gameBoard.append("||                  ||                                                                    \n");
		gameBoard.append("||                  || Number of tries left: " + this.triesLeft + "                       \n");
		gameBoard.append("||                  ||                                                                    \n");
		gameBoard.append("||                  ||                                                                    \n");
		gameBoard.append("==========================================================================================");
		return gameBoard.toString();
	}
	
	public String buildWinGameBoard(String word) {
		StringBuilder victoryBoard = new StringBuilder();
		victoryBoard.append("\n");
		victoryBoard.append("========================================== SINGLE GAME ===================================\n");
		victoryBoard.append("||                  ||                                                                    \n");
		victoryBoard.append("||                  ||     ********************** YOU WON!!!! **********************      \n");
		victoryBoard.append("||                  ||                                                                    \n");
		victoryBoard.append("||                  || You found " + word + " in " + (10 - this.triesLeft) + " tries.      \n");
		victoryBoard.append("||    (hangman)     ||                                                                    \n");
		victoryBoard.append("||                  || (HIGHSCORE TO BE IMPLEMENTED)                                      \n");
		victoryBoard.append("||                  ||                                                                    \n");
		victoryBoard.append("||                  || ------------------------------                                     \n");
		victoryBoard.append("||                  || 1 - Return to start menu                                           \n");
		victoryBoard.append("||                  ||                                                                    \n");
		victoryBoard.append("||                  || 0 - Quit the application                                           \n");		
		victoryBoard.append("||                  ||                                                                    \n");
		victoryBoard.append("==========================================================================================\n");
		return victoryBoard.toString();
	}
	
	public String buildLoseGameBoard() {
		StringBuilder loseBoard = new StringBuilder();
		loseBoard.append("\n");
		loseBoard.append("========================================== SINGLE GAME ===================================\n");
		loseBoard.append("||                  ||                                                                    \n");
		loseBoard.append("||                  ||     ********************** YOU LOSE :( **********************      \n");
		loseBoard.append("||                  ||                                                                    \n");
		loseBoard.append("||                  || Word was " + this.word.toUpperCase() + "                                         \n");
		loseBoard.append("||    (hangman)     ||                                                                    \n");
		loseBoard.append("||                  || ------------------------------                                     \n");
		loseBoard.append("||                  || 1 - Return to start menu                                           \n");
		loseBoard.append("||                  ||                                                                    \n");
		loseBoard.append("||                  || 0 - Quit the application                                           \n");
		loseBoard.append("||                  ||                                                                    \n");
		loseBoard.append("==========================================================================================\n");
		return loseBoard.toString();
	}
}