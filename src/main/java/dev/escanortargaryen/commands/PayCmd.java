package dev.escanortargaryen.commands;

import dev.escanortargaryen.SimpleEconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PayCmd implements CommandExecutor, TabCompleter {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cYou must be a player to do /pay");
            return false;
        }
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /pay <playerName> <amount>");
            return false;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(args[1] + " is not a valid number");
            return false;
        }

        if (amount == 0) {
            sender.sendMessage("§cCannot send 0 ");
            return false;
        }

        if (p.getName().equalsIgnoreCase(args[0])) {
            p.sendMessage("§cYou cannot send money to yourself!");
            return false;
        }
        OfflinePlayer pl = Bukkit.getOfflinePlayer(args[0]);
        if (pl == null) {
            sender.sendMessage("§cCouldn't find player " + args[0]);
            return false;
        }
        double d = 0;
        try {
            d = SimpleEconomy.pay(p, pl, amount);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return false;
        }
        p.sendMessage("§aSent §e" + SimpleEconomy.getEconomy().format(d) + "§a to §e" + args[0]);

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>(Bukkit.getOnlinePlayers().size());
            SimpleEconomy.getPlayers().forEach(p -> {

                if (!sender.getName().equalsIgnoreCase(p.getName()))
                    completions.add(p.getName());

            });
            return filterTabCompleteOptions(completions, args);
        }
        return new ArrayList<>();
    }

    public static List<String> filterTabCompleteOptions(Collection<String> options, String[] args) {
        String lastArg = "";
        if (args.length > 0) {
            lastArg = args[(args.length - 1)].toLowerCase();
        }
        List<String> Options = new ArrayList<>(options);
        for (int i = 0; i < Options.size(); i++) {
            if (!Options.get(i).toLowerCase().startsWith(lastArg)) {
                Options.remove(i--);
            }
        }
        return Options;
    }

}
