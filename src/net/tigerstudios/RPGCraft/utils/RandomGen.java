package net.tigerstudios.RPGCraft.utils;

import java.util.Random;

public class RandomGen {
	private static Random rndGen;
	
	private static byte [] percent;
	private static byte [] d4, d6, d8, d10, d12, d20;
	private static int cur_percent, cur_d4, cur_d6, cur_d8, cur_d10, cur_d12, cur_d20;
		
	public static byte getRandom(final byte die)
	{
		switch(die)
		{
		case 100:	if(cur_percent >= 2048) cur_percent = 0;
					return percent[cur_percent++];
			
		case 20:	if(cur_d20 >= 2048)	cur_d20 = 0;
					return d20[cur_d20++];
		
		case 12:	if(cur_d12 >= 2048)	cur_d12 = 0;
					return d12[cur_d12++];
					
		case 10:	if(cur_d10 >= 2048)	cur_d10 = 0;
					return d10[cur_d10++];
					
		case 8:		if(cur_d8 >= 2048)	cur_d8 = 0;
					return d8[cur_d8++];
					
		case 6:		if(cur_d6 >= 2048)	cur_d6 = 0;
					return d6[cur_d6++];
					
		case 4:		if(cur_d4 >= 2048)	cur_d4 = 0;
					return d4[cur_d4++];			
		default:
			return 0;
		} // switch(die)
	} // public static byte getRandom(final byte die)
		
	
	public static void initialize(final long seed)
	{
		rndGen = new Random();
		rndGen.setSeed(seed);
		
		// Create 2k percent values;
		percent 	= new byte [2048];		d4 			= new byte [2048];	
		d6 			= new byte [2048];		d8 			= new byte [2048];	
		d10			= new byte [2048];		d12			= new byte [2048];		
		d20			= new byte [2048];	
		
		resetDice();
	} // public static void initialize(final long seed)
	
	public static void resetDice()
	{	int count = 0;
		cur_percent = 0;	cur_d4 = 0; cur_d6 = 0; cur_d8 = 0; cur_d10 = 0; cur_d20 = 0;
		
		while(count < 2048)
		{	percent[count] 	= (byte)rndGen.nextInt(101);
			d4[count] 		= (byte)(rndGen.nextInt(4) + 1);
			d6[count] 		= (byte)(rndGen.nextInt(6) + 1);
			d8[count] 		= (byte)(rndGen.nextInt(8) + 1);
			d10[count] 		= (byte)(rndGen.nextInt(10) + 1);
			d12[count] 		= (byte)(rndGen.nextInt(12) + 1);
			d20[count] 		= (byte)(rndGen.nextInt(20) + 1);
			count++;
		} // while(count < 2048)
	} // public static void resetDice()
} // public class RandomGen
