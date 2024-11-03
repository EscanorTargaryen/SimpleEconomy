package dev.escanortargaryen;

import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class VaultEconomy {

    DatabaseManager table;

    private VaultEconomy() {
    }

    @SuppressWarnings("deprecation")
    private double apply(String player, double amount) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(player);
        return apply(p, amount);
    }

    private double apply(OfflinePlayer player, double amount) {

        return amount;
    }

    public VaultEconomy(DatabaseManager table) {
        this.table = table;
    }

    public boolean isEnabled() {
        return SimpleEconomy.instance.isEnabled();
    }

    public String getName() {
        return "SimpleEconomy";
    }

    public boolean hasBankSupport() {
        return false;
    }

    public int fractionalDigits() {
        return 2;
    }

    public String format(double d) {
        return SimpleEconomy.round(d, 2) + " $";
    }

    public String currencyNamePlural() {
        return "Dollars";
    }

    public String currencyNameSingular() {
        return "Dollar";
    }

    @Deprecated
    public boolean hasAccount(String player) {
        return table.containsPlayer(Bukkit.getOfflinePlayer(player));
    }

    public boolean hasAccount(OfflinePlayer player) {
        return table.containsPlayer(player);
    }

    @Deprecated
    public boolean hasAccount(String player, String world) {
        return table.containsPlayer(Bukkit.getOfflinePlayer(player));
    }

    public boolean hasAccount(OfflinePlayer player, String world) {
        return table.containsPlayer(player);
    }

    @Deprecated
    public double getBalance(String player) {
        return table.getBalance(Bukkit.getOfflinePlayer(player));
    }

    public double getBalance(OfflinePlayer player) {
        return table.getBalance(player);
    }

    @Deprecated
    public double getBalance(String player, String world) {
        return table.getBalance(Bukkit.getOfflinePlayer(player));
    }

    public double getBalance(OfflinePlayer player, String world) {
        return table.getBalance(player);
    }

    @Deprecated
    public boolean has(String player, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Illegal amount value " + amount);
        } else if (amount == 0) {
            return true;
        } else {
            return table.getBalance(Bukkit.getOfflinePlayer(player)) >= amount;
        }
    }

    public boolean has(OfflinePlayer player, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Illegal amount value " + amount);
        } else if (amount == 0) {
            return true;
        } else {
            return table.getBalance(player) >= amount;
        }
    }

    @Deprecated
    public boolean has(String player, String world, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Illegal amount value " + amount);
        } else if (amount == 0) {
            return true;
        } else {
            return table.getBalance(Bukkit.getOfflinePlayer(player)) >= amount;
        }
    }

    public boolean has(OfflinePlayer player, String world, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Illegal amount value " + amount);
        } else if (amount == 0) {
            return true;
        } else {
            return table.getBalance(player) >= amount;
        }
    }

    @Deprecated
    public EconomyResponse withdrawPlayer(String player, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot withdraw a negative amount of ");
        }
        double d = table.getBalance(Bukkit.getOfflinePlayer(player));

        double d1 = d <= amount ? 0 : d - amount;

        table.setBalance(Bukkit.getOfflinePlayer(player), d1);

        return new EconomyResponse(d1, d, ResponseType.SUCCESS, "");
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot withdraw a negative amount of ");
        }
        double d = table.getBalance(player);

        double d1 = d <= amount ? 0 : d - amount;

        table.setBalance(player, d1);

        return new EconomyResponse(d1, d, ResponseType.SUCCESS, "");
    }

    @Deprecated
    public EconomyResponse withdrawPlayer(String player, String world, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot withdraw a negative amount of ");
        }
        double d = table.getBalance(Bukkit.getOfflinePlayer(player));

        double d1 = d <= amount ? 0 : d - amount;

        table.setBalance(Bukkit.getOfflinePlayer(player), d1);

        return new EconomyResponse(d1, d, ResponseType.SUCCESS, "");
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot withdraw a negative amount of ");
        }
        double d = table.getBalance(player);

        double d1 = d <= amount ? 0 : d - amount;

        table.setBalance(player, d1);

        return new EconomyResponse(d1, d, ResponseType.SUCCESS, "");
    }

    @Deprecated
    public EconomyResponse depositPlayer(String player, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot deposit a negative amount of ");
        }
        double balance = table.getBalance(Bukkit.getOfflinePlayer(player));

        table.setBalance(Bukkit.getOfflinePlayer(player), balance + amount);

        return new EconomyResponse(amount, balance + amount, ResponseType.SUCCESS, "");
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot deposit a negative amount of ");
        }
        double balance = table.getBalance(player);

        table.setBalance(player, balance + amount);

        return new EconomyResponse(amount, balance + amount, ResponseType.SUCCESS, "");
    }

    @Deprecated
    public EconomyResponse depositPlayer(String player, String world, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot deposit a negative amount of ");
        }
        OfflinePlayer p = Bukkit.getOfflinePlayer(player);

        double balance = table.getBalance(p);

        table.setBalance(p, balance + amount);

        return new EconomyResponse(amount, balance + amount, ResponseType.SUCCESS, "");
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        if (amount <= 0) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "Cannot deposit a negative amount of ");
        }
        double balance = table.getBalance(player);

        table.setBalance(player, balance + amount);

        return new EconomyResponse(amount, balance + amount, ResponseType.SUCCESS, "");
    }

    @Deprecated
    public EconomyResponse createBank(String var1, String var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse createBank(String var1, OfflinePlayer var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse deleteBank(String var1) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse bankBalance(String var1) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse bankHas(String var1, double var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse bankWithdraw(String var1, double var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse bankDeposit(String var1, double var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse isBankOwner(String var1, String var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse isBankOwner(String var1, OfflinePlayer var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse isBankMember(String var1, String var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public EconomyResponse isBankMember(String var1, OfflinePlayer var2) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "");
    }

    @Deprecated
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Deprecated
    public boolean createPlayerAccount(String player) {
        table.addPlayer(Bukkit.getOfflinePlayer(player));
        return true;
    }

    public boolean createPlayerAccount(OfflinePlayer player) {
        table.addPlayer(player);
        return true;
    }

    @Deprecated
    public boolean createPlayerAccount(String player, String world) {
        table.addPlayer(Bukkit.getOfflinePlayer(player));
        return true;
    }

    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        table.addPlayer(player);
        return true;
    }

}
