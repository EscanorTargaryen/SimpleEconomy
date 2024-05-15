package dev.escanortargaryen.commands;

import dev.escanortargaryen.SimpleEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EconomyCmd implements CommandExecutor, TabCompleter {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /eco <give|take|set> ...");
            return false;
        }
        double amount = 0;
        if (args.length == 3)
            try {
                amount = Double.parseDouble(args[2]);
                if (amount < 0) {
                    sender.sendMessage("§cCannot send less then 0 ");
                    return false;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("§c" + args[2] + " is not a valid number");
                return false;
            }

        if (args[0].equalsIgnoreCase("give")) {

            if (args.length != 3) {
                sender.sendMessage("§cUsage: /eco give <playerName> <amount>");
                return false;
            }

            OfflinePlayer pl = Bukkit.getOfflinePlayer(args[1]);
            if (pl == null) {
                sender.sendMessage("§cCouldn't find player " + args[0]);
                return false;
            }

            try {
                SimpleEconomy.add(pl, amount);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c" + args[2] + " is not a valid number");
                return false;
            }

            sender.sendMessage("§aGave §e" + SimpleEconomy.getEconomy().format(amount) + "§a to §e" + args[1]);

            return true;
        } else if (args[0].equalsIgnoreCase("take")) {

            if (args.length != 3) {
                sender.sendMessage("§cUsage: /eco take <playerName> <amount>");
                return false;
            }

            OfflinePlayer pl = Bukkit.getOfflinePlayer(args[1]);
            if (pl == null) {
                sender.sendMessage("§cCouldn't find player " + args[0]);
                return false;
            }

            try {
                SimpleEconomy.remove(pl, amount);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c" + args[2] + " is not a valid number");
                return false;
            }

            sender.sendMessage("§aTook §e" + SimpleEconomy.getEconomy().format(amount) + "§a from §e" + args[1]);


            return true;
        } else if (args[0].equalsIgnoreCase("set")) {

            if (args.length != 3) {
                sender.sendMessage("§cUsage: /eco set <playerName> <amount>");
                return false;
            }

            OfflinePlayer pl = Bukkit.getOfflinePlayer(args[1]);
            if (pl == null) {
                sender.sendMessage("§cCouldn't find player " + args[0]);
                return false;
            }

            try {
                SimpleEconomy.set(pl, amount);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c" + args[2] + " is not a valid number");
                return false;
            }

            sender.sendMessage("§aSetted §e" + SimpleEconomy.getEconomy().format(amount) + "§a to §e" + args[1]);


            return true;
        } else {
            sender.sendMessage("§cUsage: /eco <give|take|set> ...");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return filterTabCompleteOptions(Arrays.asList("give", "take", "set"), args);
        } else if (args.length == 2) {
            List<String> completions = new LinkedList<>();
            SimpleEconomy.getPlayers().forEach(p -> {

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
