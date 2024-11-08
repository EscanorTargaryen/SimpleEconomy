package dev.escanortargaryen;

import dev.escanortargaryen.commands.BalanceCmd;
import dev.escanortargaryen.commands.EconomyCmd;
import dev.escanortargaryen.commands.PayCmd;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SimpleEconomy extends JavaPlugin implements Listener {

    static SimpleEconomy instance;

    static DatabaseManager database;

    private static VaultEconomy economy;

    @Override
    public void onEnable() {

        instance = this;

        try {
            new File(getDataFolder(), "").mkdirs();

            database = new DatabaseManager(new File(getDataFolder(), "database.db"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        economy = new VaultEconomy(database);

        PluginCommand command = Bukkit.getPluginCommand("economy");
        if (command != null) {
            command.setExecutor(new EconomyCmd());
            command.setTabCompleter(new EconomyCmd());
        }

        command = Bukkit.getPluginCommand("balance");
        if (command != null) {
            command.setExecutor(new BalanceCmd());
            command.setTabCompleter(new BalanceCmd());
        }

        command = Bukkit.getPluginCommand("pay");
        if (command != null) {
            command.setExecutor(new PayCmd());
            command.setTabCompleter(new PayCmd());
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            getServer().getServicesManager().register(Economy.class, new VaultInjector(economy), this, ServicePriority.Highest);
            getLogger().info("Hooked into Vault");
        }

        Bukkit.getPluginManager().registerEvents(this, this);

    }

    public static VaultEconomy getEconomy() {
        return economy;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {

        database.addPlayer(e.getPlayer());

    }

    @Override
    public void onDisable() {

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            getServer().getServicesManager().unregister(Economy.class, this);
            getLogger().info("Unhooked from Vault");
        }

    }

    public static void add(OfflinePlayer player, double amount) {
        database.setBalance(player, database.getBalance(player) + amount);

    }

    public static void remove(OfflinePlayer player, double amount) {
        double am = database.getBalance(player) - amount;

        database.setBalance(player, am >= 0 ? am : 0);
    }

    public static double get(OfflinePlayer player) {

        return database.getBalance(player);

    }

    public static String getWithFormat(OfflinePlayer player) {

        return getEconomy().format(get(player));

    }

    public static boolean has(OfflinePlayer player, double amount) {
        verify(amount);
        return database.getBalance(player) >= amount;
    }

    public static void set(OfflinePlayer player, double amount) {
        database.setBalance(player, amount);

    }

    public static double pay(OfflinePlayer sender, OfflinePlayer reciver, double amount) {
        verify(amount);

        boolean modified = false;
        if (has(sender, amount)) {
            modified = true;
            remove(sender, amount);
            add(reciver, amount);
        }
        return modified ? amount : 0;

    }

    public static boolean contains(OfflinePlayer player) {
        return database.containsPlayer(player);
    }

    public static void register(OfflinePlayer player) {
        database.addPlayer(player);
    }

    private static void verify(double d) {
        Validate.isTrue(d >= 0, "Amount must be a positive number");
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<OfflinePlayer> getPlayers() {
        return database.getPlayerNames();
    }

}
