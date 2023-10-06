package net.lumania.economy.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoinsExpansion extends PlaceholderExpansion {


    /* Methods */
    @Override
    public @NotNull String getIdentifier() {
        return "lumania-coins";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lumania";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return super.onPlaceholderRequest(player, params);
    }

}
