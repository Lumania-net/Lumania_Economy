package net.lumania.economy.commands;

import net.kyori.adventure.text.Component;
import net.lumania.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReloadCommand implements CommandExecutor {

    /* Constructor */
    public ReloadCommand(JavaPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("economyreload")).setExecutor(this);
    }

    /* Methods */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(
                    Economy.getInstance().getMessages().getFormatted("Errors.ConsoleSender")
                            .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
            ));
            return false;
        }

        if (!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Reload"))) {
            player.sendMessage(Component.text(
                    Economy.getInstance().getMessages().getFormatted("Errors.NoPermissions")
                            .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
            ));
            return false;
        }

        Economy.getInstance().getMessages().reload();
        Economy.getInstance().getPermissions().reload();
        Economy.getInstance().getDatabase().reload();
        player.sendMessage(Component.text(
                Economy.getInstance().getMessages().getFormatted("Commands.Reload")
                        .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
        ));
        return true;
    }

}

