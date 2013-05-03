package net.tigerstudios.RPGCraft.utils;

import java.util.Random;
import org.bukkit.entity.Player;



public class MathMethods {
	public static Random rnd = null;
	public static boolean inRange(Player p, double x, double y, double z, double range) {
		double pX = p.getLocation().getX();
		double pY = p.getLocation().getY();
		double pZ = p.getLocation().getZ();		
		return Math.pow(pX - x,2) + Math.pow(pY - y,2) + Math.pow(pZ - z,2) <= range*range;
	}
	
	
	public static boolean isLookingAtAndInRange(Player player, double x, double y, double z, double range) {
		return inRange(player, x, y, z, range) && lookingAt(player, x, y, z);
	}
	
	
	public static boolean lookingAt(Player p, double x, double y, double z) 
	{
		double pX = p.getLocation().getX();
		//double pY = p.getLocation().getY();
		double pZ = p.getLocation().getZ();	
		
		double dx = Math.abs(pX - x);
		double dz = Math.abs(pZ - z);

		double angle = Math.toDegrees(Math.atan(dx/dz));

		if (x <= pX && z <= pZ) {
			angle = 180 - angle;
		} else if (x <= pX && z >= pZ) {
//			angle = angle;
		} else if (x >= pX && z >= pZ) {
			angle = 360 - angle;
		} else {
			angle = 180 + angle;
		}

		double pRot = p.getLocation().getYaw() % 360;
		if (pRot < 0) pRot += 360D;

		if (Math.abs(pRot - angle) < 15) {
			return true;
		} else {
			return false;
		}
	}// public static boolean lookingAt(Player p, double x, double y, double z) 
	
		
	public static void setup()
	{
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
	}
}
