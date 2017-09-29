package assignment2;

import java.util.*;

public class Game {
	
	private static boolean scan, debug, valid, game_start;
	private static Scanner input;
	private static Code[] history;
	
	private static void game_init() {
		scan = false;
		history = new Code[12];
		valid = false;
		game_start = false;
	}
	
	//Initialize input scanner
	private static void scan_init() {
		if(!scan) {
			input = new Scanner(System.in);
			scan = true;
		}
	}
	
	
	public Game (boolean debug_in) {
		game_init();
		scan_init();
		debug = debug_in;
		System.out.println("Welcome to Mastermind.");

		while(!valid) {
			System.out.println("Do you want to play a new game? (Y/N):");
			switch(input.next()) {
				case "Y":
					valid = true;
					game_start = true;
					break;
				case "N":
					valid = true;
					break;
				default:
					System.out.println("INVALID_INPUT");		
			}
		}
		if(game_start) {
			runGame();
		}
		
	}
	

	
	
	private static void runGame(){
		Arrays.fill(history, null); //clear history every game
		int guess = GameConfiguration.guessNumber; //reset number of guesses
		Pegs peg = new Pegs();
		Code comp = new Code(SecretCodeGenerator.getInstance().getNewSecretCode()); //generate code
		
		if(debug) {
			System.out.println("Secret code: " + comp.getCode());
		}
		while(guess > 0) {
			System.out.println(); //blank line
			System.out.println("You have " + guess + " guess(es) left.");
			System.out.println("Enter guess:");
			Code in = new Code(input.next());
			int g = in.valid();// validate guess
			switch(g) {
				case 0:
					System.out.println("INVALID_GUESS");
					break;
				case 1:
					guess--; 				   				//Reduce Guess count
					peg.process(in, comp);  				//Process Guess
					Code hist = new Code(in.getCode() + " -> " + peg.getString());
					System.out.println(hist.getCode()); //Output-print guess and # of pegs
					history[11-guess] = hist; //Store into history	
					break;
				case 2:
					//print out stored history
					for(int i = 0; i < 12-guess; i++) {
						System.out.println(history[i].getCode());}
					break;
			}
			//Win screen
			if(peg.end()) {
				System.out.println("You win!");
				break;
			}
		}
		//Lose screen
		if(guess == 0 && !(peg.end())) {
			System.out.println("You lose! The pattern was " + comp.getCode());
		}
		System.out.println(); //System.out.println();
		//Play again?
		game_start = true;
		while(game_start) {
			System.out.println("Do you want to play a new game? (Y/N):");
			switch(input.next()) {
				case "Y":
					runGame();
					break;
				case "N":
					game_start = false;
					break;
				default:
					System.out.println("INVALID_INPUT");
					System.out.println();
			}
		}
	}
	
	
}