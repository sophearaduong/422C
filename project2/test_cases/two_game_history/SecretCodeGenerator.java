package assignment2;

import java.util.Random;

public class SecretCodeGenerator {
	private static SecretCodeGenerator instance = new SecretCodeGenerator();
	public static SecretCodeGenerator getInstance() { return instance; }
    //Use this method for each game only once.
	//The correct way to call this is: SecretCodeGenerator.getInstance().getNewSecretCode()
    private static int i = 0;
	public String getNewSecretCode()
	{
        if(i == 0)
        {
            i++;
            return "OPGB";
        }
        return "RBBY";
	}
}
