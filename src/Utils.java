import java.util.Random;
import java.awt.Color;
import java.lang.Thread;

public final class Utils {
	// Takes in three color objects and checks if RGB values of third
	// object is within the RGB values of the other two color objects
	public static boolean withinRGBRange(Color sC, Color eC, Color vC){
		boolean result = false;

		// Start colors RGB.
		int sR = sC.getRed();
		int sG = sC.getGreen();
		int sB = sC.getBlue();
		// End colors RGB.
		int eR = eC.getRed();
		int eG = eC.getGreen();
		int eB = eC.getBlue();
		// Value color RGB.
		int vR = vC.getRed();
		int vG = vC.getGreen();
		int vB = vC.getBlue();

		boolean isWithinRed 	= (sR <= vR && eR >= vR);
		boolean isWithinGreen 	= (sG <= vG && eG >= vG);
		boolean isWithinBlue 	= (sB <= vB && eB >= vB);


		// Built true count level logic.
		// In other words, if trueLevel or more are true then result is true;
		// True level is hard coded to 3 for now.
		int trueCount = 0;
		int trueLevel = 3;

		if(isWithinRed){trueCount++;}
		if(isWithinGreen){trueCount++;}
		if(isWithinGreen){trueCount++;}

		if(trueCount>=trueLevel){result=true;}

		return result;
	}

	// Calculate an average.
	public static long average(long n, long d){
		long result = 0;
		if(d>0){
			result = n/d;
		}
		return result;
	}

	// Flip the Bth bit in N.
	public static byte flipBit(int n, int b){
		return (byte)(n ^ (1 << b));
	}

	// Convert milliseconds to string in hms form.
    public static String millisToHMS(long ms){
    	long h;
    	long m;
    	long s;
    	long r;

    	h = ms/3600000;
    	r = ms%3600000;
    	m = r/60000;
    	r = r%60000;
    	s = r/1000;

    	return h+"h "+m+"m "+s+"s ";
    }

	// Calculate a hash.
	public static byte byteHash(byte[] bytes){
		byte sum = bytes[0];
		for(int i=1;i<bytes.length;i++){
			sum ^= bytes[i];
		}
		return sum;
	}

	// Generate a random set of instructions of size c.
	public static byte[] randInstructionSet(int c){
		byte[] instrSet = new byte[c];
		Random rand = new Random();
		rand.nextBytes(instrSet);

		return instrSet;
	}

	public static byte[] copyInstructionSet(byte[] oldSet){
		byte[] newSet = new byte[oldSet.length];

		for(int i=1;i<oldSet.length;i++){
			newSet[i] = oldSet[i];
		}

		return newSet;
	}

	public static void printN(String s, int n){
		String output = "";
		for(int i=0;i<n;i++)
			output+=s;

		System.out.print(output);
	}
	
	public static void wait(int s)
	{
		waitMS(s*1000);
	}
	
	// Wait in seconds.
	public static void waitMS(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
