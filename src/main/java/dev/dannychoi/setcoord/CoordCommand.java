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

public class CoordCommand implements CommandExecutor {
    private DecimalFormat df = new DecimalFormat("#.###");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final FileConfiguration coordsYML = SetCoord.getPlugin().getCoordsYML();

        String creatorName;
        UUID creatorUUID;
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
                return true;
            }

            creatorName = ((Player) sender).getName();
            creatorUUID = ((Player) sender).getUniqueId();
        } else if (args.length == 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            if (!offlinePlayer.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + "This player has never logged on to this server. Maybe they changed their username?");
                return true;
            }

            creatorName = args[1];
            creatorUUID = offlinePlayer.getUniqueId();
        } else {
            return false;
        }


        final String coordName = args[0];
        final String path = creatorUUID.toString()+"."+coordName;
        final Location location = coordsYML.getLocation(path);
        final String coordinates = "("+df.format(location.getX())+", "+df.format(location.getY())+", "+df.format(location.getZ())+")";

        if (!coordsYML.contains(path)) {
            sender.sendMessage(ChatColor.RED + "There are no coordinates assigned to this name. Type /coords to see all saved coordinates.");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.WHITE + coordName);
        sender.sendMessage(ChatColor.GOLD + "Coordinates: " + ChatColor.WHITE + coordinates);
        sender.sendMessage(ChatColor.GOLD + "World: " + ChatColor.WHITE + location.getWorld().getName());
        sender.sendMessage(ChatColor.GOLD + "Created by: " + ChatColor.WHITE + creatorName);

        return true;
    }
}
