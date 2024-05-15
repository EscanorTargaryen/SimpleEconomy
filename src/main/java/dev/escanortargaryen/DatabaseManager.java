package dev.escanortargaryen;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private Connection connection;


    public DatabaseManager(@NotNull File dbFile) throws Exception {
        Preconditions.checkNotNull(dbFile, "Database file is null.");

        if (!dbFile.exists() && !dbFile.createNewFile()) {
            throw new IOException("Cannot create the database file.");
        }
        Class.forName("org.sqlite.JDBC");
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setEncoding(SQLiteConfig.Encoding.UTF8);
        config.setSynchronous(SQLiteConfig.SynchronousMode.FULL);
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile, config.toProperties());

        setUp();
    }

    private void setUp() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.addBatch("CREATE TABLE IF NOT EXISTS `Players` ( id INTEGER PRIMARY KEY AUTOINCREMENT, uuid TEXT NOT NULL UNIQUE, player_name TEXT NOT NULL, balance REAL NOT NULL DEFAULT 0.0);");
            statement.executeBatch();
        }
    }


    public boolean containsPlayer(OfflinePlayer offlinePlayer) {

        String playerName = offlinePlayer.getName();
        String query = "SELECT COUNT(*) FROM `Players` WHERE player_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public double getBalance(OfflinePlayer offlinePlayer) {
        String playerName = offlinePlayer.getName();
        double balance = 0.0;

        String query = "SELECT balance FROM `Players` WHERE player_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    balance = rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;

    }

    public void setBalance(OfflinePlayer player, double balance) {

        String playerName = player.getName();

        String query = "UPDATE `Players` SET balance = ? WHERE player_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, balance);
            stmt.setString(2, playerName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(OfflinePlayer offlinePlayer) {

        String playerName = offlinePlayer.getName();
        String uuid = offlinePlayer.getUniqueId().toString();
        double initialBalance = 0.0;

        if (!containsPlayer(offlinePlayer)) {
            String query = "INSERT INTO `Players` (uuid, player_name, balance) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, uuid);
                stmt.setString(2, playerName);
                stmt.setDouble(3, initialBalance);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public List<OfflinePlayer> getPlayerNames() {

        List<OfflinePlayer> players = new ArrayList<>();

        String query = "SELECT player_name FROM `Players`";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                players.add(offlinePlayer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }
}
