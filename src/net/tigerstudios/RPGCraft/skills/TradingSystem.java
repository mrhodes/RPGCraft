package net.tigerstudios.RPGCraft.skills;


public class TradingSystem {
	private static purchaseHistory ph = null;
	
	// need tp keep a running list of average sales for every item sold.
	// When a traders Window load, it will communicate with this system
	// It will send an item, and expect a return price.
	
	// Another feature of this System is to manage each players Skill level in
	// Trading.  Once a sale is complete then a skill increase, or decrease will happen.
	// This is based on the average/actual sale price.
	
	public static void soldItem(int id, long price)
	{	ph.totalSale[id]++;
		ph.totalCopper[id] += price;
	} // public static void soldItem(int id, long price)
	
	
	public static long getAverageSale(int id)
	{	long avg = ph.totalSale[id] / ph.totalCopper[id];
		return avg;
	} // public long getAverageSale(int id)	
	
	
	public TradingSystem()
	{	
		
	}
} // public class TradingSystem

class purchaseHistory
{
	public int totalSale[];			// Total amount of items item has been sold.
	public long totalCopper[];	// All time copper paid for this item
	
}
