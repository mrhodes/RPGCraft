package net.tigerstudios.RPGCraft.utils;


import org.bukkit.event.Listener;

/**
 * Checks for plugins whenever one is enabled
 */
public class listener_Plugin implements Listener {
    public listener_Plugin() { }

    /*public void PluginEnableEvent(PluginEnableEvent event) {
        if(RPGCraft.getiConomy() == null) {
                  	
        	Plugin iConomy = RPGCraft.getBukkitServer().getPluginManager().getPlugin("iConomy");

            if (iConomy != null) {
                if(iConomy.isEnabled()) {
                    RPGCraft.setiConomy((iConomy)iConomy);
                    System.out.println("[RPGCraft] --->   Successfully linked with iConomy.");
                }
            }
        }
    }*/
}
