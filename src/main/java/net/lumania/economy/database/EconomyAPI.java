package net.lumania.economy.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EconomyAPI {

    /* Variables */
    private final DataSource dataSource;
    private final Logger logger;

    /* Constructor */
    public EconomyAPI(DataSource dataSource) {
        this.dataSource = dataSource;
        this.logger = LoggerFactory.getLogger(EconomyAPI.class);
    }

    /* Methods */
    public boolean playerExists(UUID uuid) {
        try(Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT coins FROM economy WHERE uuid=?")) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException exception) {
            return false;
        }
    }

    public void setupPlayer(UUID uuid) {
        if(playerExists(uuid)) return;
        try(Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO economy(uuid, coins, toggled_pay) VALUES(?,?,?)")) {
            pstmt.setString(1, uuid.toString());
            pstmt.setInt(2, 0);
            pstmt.setBoolean(3, false);
            pstmt.executeUpdate();
        } catch (SQLException exception) {
            logger.error("Error while setting up the Player", exception);
        }
    }

    public int getCoins(UUID uuid) {
        if(!playerExists(uuid)) setupPlayer(uuid);
        try(Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT coins FROM economy WHERE uuid=?")) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("coins");
            }
        } catch (SQLException exception) {
            logger.error("Error while getting player coins", exception);
        }
        return 0;
    }

    public boolean hasToggledPay(UUID uuid) {
        if(!playerExists(uuid)) setupPlayer(uuid);
        try(Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT toggled_pay FROM economy WHERE uuid=?")) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getBoolean("toggled_pay");
            }
        } catch (SQLException exception) {
            logger.error("Error while getting player toggled pay", exception);
            return false;
        }
        return false;
    }

    public void setCoins(UUID uuid, int coins) {
        if(!playerExists(uuid)) setupPlayer(uuid);
        try(Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement("UPDATE economy SET coins=? WHERE uuid=?")) {
            pstmt.setInt(1, coins);
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException exception) {
            logger.error("Error while setting player coins", exception);
        }
    }

    public void setToggledPay(UUID uuid, boolean toggledPay) {
        if(!playerExists(uuid)) setupPlayer(uuid);
        try(Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement("UPDATE economy SET toggled_pay=? WHERE uuid=?")) {
            pstmt.setBoolean(1, toggledPay);
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException exception) {
            logger.error("Error while setting player toggled pay", exception);
        }
    }

    public void addCoins(UUID uuid, int coins) {
        setCoins(uuid, getCoins(uuid) + coins);
    }

    public void removeCoins(UUID uuid, int coins) {
        setCoins(uuid, getCoins(uuid) - coins);
    }

}
