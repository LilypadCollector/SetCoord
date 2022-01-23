package dev.dannychoi.setcoord;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class CoordsCommand implements CommandExecutor {

    private final SetCoord plugin = SetCoord.getPlugin();
    private final FileConfiguration coordsYML = plugin.getCoordsYML();

    private final int entriesPerPage = 8;
    private DecimalFormat df = new DecimalFormat("#.###");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        String pName;
        UUID pUUID;
        double currentPage = 1, maxPage; // currentPage is 1 by default. May be changed by upcoming if statement.
        if (args.length == 0) { // Argument is blank; sender wants their own coords.
            if (sender instanceof Player) {
                pUUID = ((Player) sender).getUniqueId();
                pName = sender.getName();
            } else {
                sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
                return true;
            }
        } else if (args.length == 1) { // One argument is specified; sender wants page [0] of their own coords.
            if (sender instanceof Player) {
                pUUID = ((Player) sender).getUniqueId();
                pName = sender.getName();
            } else {
                sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
                return true;
            }

            try {
                currentPage = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return false;
            }

            if (currentPage < 1) {
                sender.sendMessage(ChatColor.RED + "Please enter a number greater than 0.");
                return true;
            }
        } else if (args.length == 2) { // Two arguments are specified; sender wants page args[0] of args[1]'s coords.
            pName = args[1];
            try {
                currentPage = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return false;
            }

            if (currentPage < 1) {
                sender.sendMessage(ChatColor.RED + "Please enter a number greater than 0.");
                return true;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            UUID offlinePlayerUUID = offlinePlayer.getUniqueId();

            if (!offlinePlayer.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + "This player has never logged on to this server. Maybe they changed their username?");
                return true;
            }

            if (!coordsYML.contains(offlinePlayerUUID.toString()) || (coordsYML.getConfigurationSection(offlinePlayerUUID.toString()).getKeys(false).size() == 0)) {
                sender.sendMessage(ChatColor.WHITE + offlinePlayer.getName() + ChatColor.RED + " does not have any coordinates saved.");
                return true;
            }

            pUUID = offlinePlayer.getUniqueId();
        } else { // Too many arguments
            return false; // Show sender the syntax.
        }

        if (!coordsYML.contains(pUUID.toString())) {
            sender.sendMessage(ChatColor.WHITE + pName + ChatColor.RED + " does not have any coordinates saved.");
            return true;
        }

        int numLocations = coordsYML.getConfigurationSection(pUUID.toString()).getKeys(false).size();
        maxPage = Math.ceil((double) numLocations / entriesPerPage);
        if (maxPage == 0)
            maxPage = 1;
        if (currentPage > maxPage) {
            sender.sendMessage(ChatColor.RED + "Page " + df.format(currentPage) + " does not exist. Max: " + df.format(maxPage));
            return true;
        }
        List<String> keys = new ArrayList<String>(coordsYML.getConfigurationSection(pUUID.toString()).getKeys(false));
        sender.sendMessage(ChatColor.YELLOW + "---- " + ChatColor.GOLD + pName + "'s coordinates" + ChatColor.YELLOW + " -- " + ChatColor.GOLD + "Page " + ChatColor.RED + df.format(currentPage) + ChatColor.GOLD + "/" + ChatColor.RED + df.format(maxPage) + ChatColor.YELLOW + " ----");
        for (int i = (int) (currentPage-1) * entriesPerPage; i < (currentPage-1) * entriesPerPage + entriesPerPage && i < keys.size(); i++) {
            String key = keys.get(i);
            String path = pUUID.toString() + "." + key;
            Location pLocation = coordsYML.getLocation(path);
            String coordinates = "("+df.format(pLocation.getX())+", "+df.format(pLocation.getY())+", "+df.format(pLocation.getZ())+")";
            sender.sendMessage(ChatColor.GOLD + key + ": " + ChatColor.WHITE + coordinates + ChatColor.GOLD + " in " + ChatColor.WHITE + pLocation.getWorld().getName());
        }

        if (currentPage == maxPage) // Don't print any ending line.
            return true;
        else {
            if (args.length == 2)
                sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/coords " + df.format(currentPage + 1) + " " + args[1] + ChatColor.GOLD + " to see the next page.");
            else
                sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/coords " + df.format(currentPage + 1) + ChatColor.GOLD + " to see the next page.");
        }

        return true;
    }
}
