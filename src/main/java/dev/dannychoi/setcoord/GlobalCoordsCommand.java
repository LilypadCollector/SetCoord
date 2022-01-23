package dev.dannychoi.setcoord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GlobalCoordsCommand implements CommandExecutor {
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
        if (args.length == 0) { // Argument is blank; show first page of global coords.
            // anything to add here?
        } else if (args.length == 1) { // One argument is specified; sender wants page args[0] of coords.
            try {
                currentPage = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return false;
            }

            if (currentPage < 1) {
                sender.sendMessage(ChatColor.RED + "Please enter a number greater than 0.");
                return true;
            }
        } else { // Too many arguments
            return false; // Show sender the syntax.
        }

        if (!coordsYML.contains("global")) {
            sender.sendMessage(ChatColor.RED + "There are no global coordinates saved.");
            return true;
        }

        int numLocations = coordsYML.getConfigurationSection("global").getKeys(false).size();
        maxPage = Math.ceil((double) numLocations / entriesPerPage);
        if (maxPage == 0)
            maxPage = 1;
        if (currentPage > maxPage) {
            sender.sendMessage(ChatColor.RED + "Page " + df.format(currentPage) + " does not exist. Max: " + df.format(maxPage));
            return true;
        }
        List<String> keys = new ArrayList<String>(coordsYML.getConfigurationSection("global").getKeys(false));
        sender.sendMessage(ChatColor.YELLOW + "---- Global Coordinates" + ChatColor.YELLOW + " -- " + ChatColor.GOLD + "Page " + ChatColor.RED + df.format(currentPage) + ChatColor.GOLD + "/" + ChatColor.RED + df.format(maxPage) + ChatColor.YELLOW + " ----");
        for (int i = (int) (currentPage-1) * entriesPerPage; i < (currentPage-1) * entriesPerPage + entriesPerPage && i < keys.size(); i++) {
            String key = keys.get(i);
            String path = "global." + key;
            Location pLocation = coordsYML.getLocation(path+".location");
            String creatorName = coordsYML.getString(path+".creator");
            String coordinates = "("+df.format(pLocation.getX())+", "+df.format(pLocation.getY())+", "+df.format(pLocation.getZ())+")";
            sender.sendMessage(ChatColor.GOLD + key + ": " + ChatColor.WHITE + coordinates + ChatColor.GOLD + " in " + ChatColor.WHITE + pLocation.getWorld().getName() + ChatColor.GOLD + " created by " + ChatColor.WHITE + creatorName);
        }

        if (currentPage == maxPage) // Don't print any ending line.
            return true;
        else
            sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/globalcoords " + df.format(currentPage + 1) + ChatColor.GOLD + " to see the next page.");

        return true;
    }
}
