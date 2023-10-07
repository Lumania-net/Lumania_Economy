package net.lumania.economy.commands.payall;

import net.kyori.adventure.text.Component;
import net.lumania.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PayallCommand implements CommandExecutor {

    /* Constructor */
    public PayallCommand(JavaPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("payall")).setExecutor(this);
    }

    /* Methods */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(
                    Economy.getInstance().getMessages().getFormatted("Errors.ConsoleSender")
                            .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
            ));
            return false;
        }


        return true;
    }

}
