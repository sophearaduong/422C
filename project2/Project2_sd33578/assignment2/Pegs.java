package assignment2;

import java.util.Arrays;

public class Pegs {
	private String pegs;
	private int wht, blk;
	
	public Pegs() {
		wht = 0;
		blk = 0;
		pegs = blk + "b_" + wht + "w";
	}
	
	public String getString() {
		return pegs;
	}
	
	public boolean end() {
		String win = GameConfiguration.pegNumber + "b_0w"; 
		return pegs.equals(win);
	}
	
	
	public void process(Code in, Code comp) {		
		int [] code_array = new int[GameConfiguration.pegNumber];
		int [] guess_array = new int[GameConfiguration.pegNumber];
		Arrays.fill(code_array, 1);
		Arrays.fill(guess_array, 1);
		
		//count black pegs
		for(int i = 0; i < GameConfiguration.pegNumber; i++) {
			if(in.getCode().charAt(i) == comp.getCode().charAt(i)) {
				blk ++;
				code_array[i] = 0;
				guess_array[i] = 0;}
			}
		
		//count white pegs - guess array loop
		for(int e = 0; e < GameConfiguration.pegNumber; e++) {
			if(guess_array[e] == 1) {
				//code array loop
				for(int a = 0; a < GameConfiguration.pegNumber; a++) {
					if(code_array[a] == 1) {
						if(in.getCode().charAt(e) == comp.getCode().charAt(a)) {
							wht++;
							guess_array[e] = 0;
							code_array[a] = 0;
							break;}}
				}}	
		}
		pegs = blk + "b_" + wht + "w";
		blk = 0;
		wht = 0;
	}
	
}
