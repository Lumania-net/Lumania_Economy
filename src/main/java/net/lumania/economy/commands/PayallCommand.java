package net.lumania.economy.commands;

import net.kyori.adventure.text.Component;
import net.lumania.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PayallCommand implements CommandExecutor {

    /* Constructor */
    public PayallCommand(JavaPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("payall")).setExecutor(this);
    }

    /* Methods */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(
                    Economy.getInstance().getMessages().getFormatted("Errors.ConsoleSender")
                            .replace("%prefix%", "Prefix")
            ));
            return false;
        }

        if(args.length == 1) {
            if(!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Payall"))) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NoPermissions")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return false;
            }

            try {
                int coins = Integer.parseInt(args[0]);

                if(coins < 0) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                    return false;
                }

                int totalCoins = 0;
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(p != player && !Economy.getInstance().getEconomy().hasToggledPay(p.getUniqueId())) totalCoins += coins;
                }

                if(Economy.getInstance().getEconomy().getCoins(player.getUniqueId()) < totalCoins) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Errors.NotEnoughCoins")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                    return false;
                }

                Economy.getInstance().getEconomy().removeCoins(player.getUniqueId(), totalCoins);
                if(coins == 1) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Payall1")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                } else {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Payall")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%coins%", String.valueOf(coins))
                    ));
                }

                Bukkit.getOnlinePlayers().forEach(p -> {
                    Economy.getInstance().getEconomy().addCoins(p.getUniqueId(), coins);
                    if(coins == 1) {
                        p.sendMessage(Component.text(
                               Economy.getInstance().getMessages().getFormatted("Commands.Pay.Other1")
                                       .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                       .replace("%target%", player.getName())
                        ));
                    } else {
                        p.sendMessage(Component.text(
                                Economy.getInstance().getMessages().getFormatted("Commands.Pay.Other")
                                        .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                        .replace("%target%", player.getName())
                                        .replace("%coins%", String.valueOf(coins))
                        ));
                    }
                });
            } catch(NumberFormatException exception) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return false;
            }
        }
        return true;
    }

}
