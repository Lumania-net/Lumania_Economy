package net.lumania.economy.commands.money.subs;

import net.kyori.adventure.text.Component;
import net.lumania.economy.Economy;
import net.lumania.economy.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MoneyRemoveSub extends SubCommand {

    /* Methods */
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length == 2) {
            if(!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Money.Remove.Self"))) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NoPermissions")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return;
            }

            try {
                int coins = Integer.parseInt(args[1]);

                if(coins < 0) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                    return;
                }

                Economy.getInstance().getEconomy().removeCoins(player.getUniqueId(), coins);
                if(coins == 1) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Money.Remove.Self1")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                } else {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Money.Remove.Self")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%coins%", String.valueOf(coins))
                    ));
                }
            } catch (NumberFormatException exception) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
            }
        } else if(args.length == 3) {
            if(!player.hasPermission(Economy.getInstance().getPermissions().getString("Commands.Money.Remove.Other"))) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NoPermissions")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.TargetOffline")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                .replace("%target%", args[1])
                ));
                return;
            }

            try {
                int coins = Integer.parseInt(args[2]);

                if(coins < 0) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                    ));
                    return;
                }

                Economy.getInstance().getEconomy().removeCoins(target.getUniqueId(), coins);
                if(coins == 1) {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Money.Remove.Other1")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", target.getName())
                    ));
                    target.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Money.Remove.OtherReceiver1")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", player.getName())
                    ));
                } else {
                    player.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Money.Remove.Other")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", target.getName())
                                    .replace("%coins%", String.valueOf(coins))
                    ));
                    target.sendMessage(Component.text(
                            Economy.getInstance().getMessages().getFormatted("Commands.Money.Remove.OtherReceiver")
                                    .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                                    .replace("%target%", player.getName())
                                    .replace("%coins%", String.valueOf(coins))
                    ));
                }
            } catch (NumberFormatException exception) {
                player.sendMessage(Component.text(
                        Economy.getInstance().getMessages().getFormatted("Errors.NumberFormatException")
                                .replace("%prefix%", Economy.getInstance().getMessages().getFormatted("Prefix"))
                ));
            }
        }
    }

}
