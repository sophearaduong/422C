package assignment2;

public class Driver {
	public static void main(String[]args) {
		boolean x;
		if(args[0].equals("1")) {
			x = true;
		}
		else {
			x = false;
		}
		new Game(x);
	}
}
