package assignment2;

public class Code {
	
	private String x;
	
	public Code(String a) {
		x = a;
	}
	
	
	public String getCode() {
		return x;
	}
	
	public int valid() {
		if(x.equals("HISTORY")) {
			return 2;
		}
		if(x.length() != GameConfiguration.pegNumber) {
			//Input too long or too short
			return 0;
		}
		String[] y = x.split("");
		int found = 0;
		for(int a = 0; a < y.length; a++) {
			for(int b = 0; b < GameConfiguration.colors.length; b++) {
				if(y[a].equals(GameConfiguration.colors[b])) {
					found = 1;
					break;}
			}
			if(found == 0) {
				return 0;}
			else {
				found = 0;}
		}
		return 1;
		
	}
	
}
