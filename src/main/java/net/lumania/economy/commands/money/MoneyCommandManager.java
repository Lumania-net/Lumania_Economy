package net.lumania.economy.commands.money;

import net.kyori.adventure.text.Component;
import net.lumania.economy.Economy;
import net.lumania.economy.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MoneyCommandManager implements CommandExecutor {

    /* Variables */
    private List<SubCommand> subCommands;

    /* Constructor */
    public MoneyCommandManager() {
        subCommands = new ArrayList<>();

        //impl subCommands by adding to list
    }

    /* Methods */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(
                    Economy.getInstance().getMessages().getFormatted("Errors.ConsoleSender")
                            .replace("%prefix%", "Prefix")
            ));
            return false;
        }

        if(args.length == 0) {
            // show command
        } else {
            for(SubCommand command : subCommands) {
                if(args[0].equalsIgnoreCase(command.getName())) {
                    command.perform(player, args);
                    return true;
                }
            }

            // perform show
        }

        return true;
    }
}
