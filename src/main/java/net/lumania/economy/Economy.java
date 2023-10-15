package net.lumania.economy;

import lombok.Getter;
import net.lumania.economy.commands.PayCommand;
import net.lumania.economy.commands.ReloadCommand;
import net.lumania.economy.commands.money.MoneyCommandManager;
import net.lumania.economy.commands.PayallCommand;
import net.lumania.economy.database.DatabaseUtils;
import net.lumania.economy.database.EconomyAPI;
import net.lumania.economy.expansions.CoinsExpansion;
import net.lumania.economy.listeners.PlayerHandler;
import net.lumania.economy.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.sql.SQLException;


public final class Economy extends JavaPlugin {

    /* Variables */
    @Getter private static Economy instance;

    @Getter private ConfigManager database;
    @Getter private ConfigManager messages;
    @Getter private ConfigManager permissions;

    @Getter private DataSource dataSource;
    @Getter private EconomyAPI economy;

    /* Methods */
    @Override
    public void onEnable() {
        instance = this;

        setupExpansions();
        setupConfigs();
        setupDatabase();
        register();
    }

    private void setupExpansions() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CoinsExpansion().register();
        }
    }

    private void setupConfigs() {
        database = new ConfigManager(this, "database.yml");
        messages = new ConfigManager(this, "messages.yml");
        permissions = new ConfigManager(this, "permissions.yml");
    }

    private void setupDatabase() {
        DatabaseUtils.DatabaseCredentials credentials = new DatabaseUtils.DatabaseCredentials(
                database.getString("Host"),
                database.getString("Database"),
                database.getString("Username"),
                database.getString("Password"),
                database.getInt("Port")
        );

        dataSource = DatabaseUtils.createDataSource(credentials);

        try {
            DatabaseUtils.executeSQLFile(this, dataSource, "setup.sql");
        } catch (SQLException exception) {
            getSLF4JLogger().error("Error while executing SQL File", exception);
        }

        economy = new EconomyAPI(dataSource);
    }

    private void register() {
        new PlayerHandler(this);
        new PayallCommand(this);
        new PayCommand(this);
        new MoneyCommandManager(this);
        new ReloadCommand(this);
    }

}
