package net.lumania.economy.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtils {

    @AllArgsConstructor
    public static class DatabaseCredentials {
        /* Variables*/
        @Getter private final String host, database, user, password;
        @Getter private final int port;
    }

    /* Methods */
    public static DataSource createDataSource(DatabaseCredentials credentials) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDatabase());
        config.setUsername(credentials.getUser());
        config.setPassword(credentials.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    public static void executeSQLFile(Plugin plugin, DataSource dataSource, String sqlFile) throws SQLException {
        String setup = null;

        try(InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream(sqlFile)) {
            setup = new String(inputStream.readAllBytes());
        } catch (Exception ignored) {}

        final String[] queries = setup.split(";");

        try(Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            for(String query : queries) {
                if(query.isBlank()) continue;

                try(PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.execute();
                }
            }
        }
    }

}
