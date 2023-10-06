package net.lumania.economy.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    /* Methods */
    public abstract String getName();
    public abstract void perform(Player player, String args[]);

}
