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

public class PayCommand implements CommandExecutor {

    /* Constructor */
    public PayCommand(JavaPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(this);
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
            if(!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Pay.Toggle"))) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NoPermissions")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return false;
            }

            if(Economy.getInstance().getEconomy().hasToggledPay(player.getUniqueId())) {
                Economy.getInstance().getEconomy().setToggledPay(player.getUniqueId(), false);
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Commands.Pay.Toggle.On")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
            } else {
                Economy.getInstance().getEconomy().setToggledPay(player.getUniqueId(), true);
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Commands.Pay.Toggle.Off")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
            }
            return true;
        } else if(args.length == 2) {
            if(!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Pay.Player"))) {
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

            if(target == player) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.PaySelf")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return false;
            }

            if(Economy.getInstance().getEconomy().hasToggledPay(player.getUniqueId())) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.TargetHasToggledPay")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                .replace("%target%", target.getName())
                ));
                return false;
            }

            try {
                int coins = Integer.parseInt(args[1]);

                if(coins < 0) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                    return false;
                }

                if(Economy.getInstance().getEconomy().getCoins(player.getUniqueId()) < coins) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Errors.NotEnoughCoins")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                    return false;
                }

                Economy.getInstance().getEconomy().removeCoins(player.getUniqueId(), coins);
                Economy.getInstance().getEconomy().addCoins(player.getUniqueId(), coins);
                if(coins == 1) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Pay.Player.Send1")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", target.getName())
                    ));
                    target.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Pay.Player.Other1")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", player.getName())
                    ));
                } else {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Pay.Player.Send")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", target.getName())
                                    .replace("%coins%", String.valueOf(coins))
                    ));
                    target.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Pay.Player.Other")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", player.getName())
                                    .replace("%coins%", String.valueOf(coins))
                    ));
                }
                return true;
            } catch (NumberFormatException exception) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
            }
        }
        return true;
    }

}
