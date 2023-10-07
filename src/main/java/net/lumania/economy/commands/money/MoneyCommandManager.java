package net.lumania.economy.commands.money;

import net.kyori.adventure.text.Component;
import net.lumania.economy.Economy;
import net.lumania.economy.commands.SubCommand;
import net.lumania.economy.commands.money.subs.MoneyAddSub;
import net.lumania.economy.commands.money.subs.MoneyRemoveSub;
import net.lumania.economy.commands.money.subs.MoneySetSub;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoneyCommandManager implements CommandExecutor {

    /* Variables */
    private final List<SubCommand> subCommands;

    /* Constructor */
    public MoneyCommandManager(JavaPlugin plugin) {
        subCommands = new ArrayList<>();
        subCommands.add(new MoneyAddSub());
        subCommands.add(new MoneySetSub());
        subCommands.add(new MoneyRemoveSub());
        Objects.requireNonNull(plugin.getCommand("money")).setExecutor(this);
    }

    /* Methods */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(
                    Economy.getInstance().getMessages().getFormatted("Errors.ConsoleSender")
                            .replace("%prefix%", "Prefix")
            ));
            return false;
        }

        if(args.length == 0) {
            if(!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Money.Show.Self"))) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NoPermissions")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return false;
            }

            int coins = Economy.getInstance().getEconomy().getCoins(player.getUniqueId());
            if(coins == 1) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Commands.Money.Show.Self1")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
            } else {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Commands.Money.Show.Self")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                .replace("%coins%", String.valueOf(coins))
                ));
            }
            return true;
        } else {
            for(SubCommand command : subCommands) {
                if(args[0].equalsIgnoreCase(command.getName())) {
                    command.perform(player, args);
                    return true;
                }
            }

            if(!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Money.Show.Other"))) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NoPermissions")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.TargetOffline")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                .replace("%target%", args[0])
                ));
                return false;
            }

            int coins = Economy.getInstance().getEconomy().getCoins(target.getUniqueId());
            if(coins == 1) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Commands.Money.Show.Other1")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                .replace("%target%", target.getName())
                ));
            } else {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Commands.Money.Show.Other")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                .replace("%coins%", String.valueOf(coins))
                                .replace("%target%", target.getName())
                ));
            }
        }
        return true;
    }

}
