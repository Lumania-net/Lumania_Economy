package net.lumania.economy.listeners;

import net.lumania.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerHandler implements Listener {

    /* Constructor */
    public PlayerHandler(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /* Listeners */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerJoinEvent event) {
        Economy.getInstance().getEconomy().setupPlayer(event.getPlayer().getUniqueId());
    }

}
