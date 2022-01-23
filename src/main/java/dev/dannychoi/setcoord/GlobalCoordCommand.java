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
import java.util.UUID;

public class GlobalCoordCommand implements CommandExecutor {
    private DecimalFormat df = new DecimalFormat("#.###");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final FileConfiguration coordsYML = SetCoord.getPlugin().getCoordsYML();

        if (args.length != 1) {
            return false; // Show syntax to sender.
        }

        final String coordName = args[0];
        final String path = "global."+coordName;

        if (!coordsYML.contains(path)) {
            sender.sendMessage(ChatColor.RED + "There are no global coordinates assigned to this name. Type /globalcoords to see all saved global coordinates.");
            return true;
        }

        final Location location = coordsYML.getLocation(path+".location");
        final String coordinates = "("+df.format(location.getX())+", "+df.format(location.getY())+", "+df.format(location.getZ())+")";
        final String creatorName = coordsYML.getString(path+".creator");

        sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.WHITE + coordName);
        sender.sendMessage(ChatColor.GOLD + "Coordinates: " + ChatColor.WHITE + coordinates);
        sender.sendMessage(ChatColor.GOLD + "World: " + ChatColor.WHITE + location.getWorld().getName());
        sender.sendMessage(ChatColor.GOLD + "Created by: " + ChatColor.WHITE + creatorName);

        return true;
    }
}
