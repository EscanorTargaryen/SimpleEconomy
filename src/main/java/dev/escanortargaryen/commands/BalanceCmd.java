package dev.escanortargaryen.commands;

import dev.escanortargaryen.SimpleEconomy;
import org.bukkit.Bukkit;
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

public class BalanceCmd implements CommandExecutor, TabCompleter {

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player p) {

            if (args.length > 1) {
                sender.sendMessage(
                        p.hasPermission("eco.balance.others") ? "§cUsage: /balance [playername]" : "§cUsage /balance");
                return false;
            }

            if (args.length == 1) {
                if (p.hasPermission("eco.balance.others")) {
                    OfflinePlayer pl = Bukkit.getOfflinePlayer(args[0]);
                    if (pl == null) {
                        sender.sendMessage("§cCouldn't find player " + args[0]);
                        return false;
                    }
                    sender.sendMessage("§aUser §e" + args[0] + "§a has §e" + SimpleEconomy.getWithFormat(pl));
                    return true;
                } else {
                    sender.sendMessage("§cInsufficient permissions to know other player's balances");
                    return false;
                }
            }

            sender.sendMessage("§eYou§a have §e" + SimpleEconomy.getWithFormat(p) + " ");

        } else {

            if (args.length != 1) {
                sender.sendMessage("§cusage: /balance <playerName>");
                return false;
            }

            OfflinePlayer pl = Bukkit.getOfflinePlayer(args[0]);
            if (pl == null) {
                sender.sendMessage("§cCouldn't find player " + args[0]);
                return false;
            }
            sender.sendMessage("§aUser §e" + args[0] + "§a has §e" + SimpleEconomy.getWithFormat(pl) + " ");

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("eco.balance.others")) {
            List<String> completions = new ArrayList<>(Bukkit.getOnlinePlayers().size());
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
