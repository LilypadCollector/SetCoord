package dev.dannychoi.setcoord;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class SetCoordCommand implements CommandExecutor {

    private DecimalFormat df = new DecimalFormat("#.###");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration coordsYML = SetCoord.getPlugin().getCoordsYML();
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }

        if (args.length <= 0) { // Syntax error, should be "/setcoord [name]"
            return false; // This sends a message to player showing command syntax
        }

        String coordName = args[0];

        Player pSender = (Player) sender;
        UUID pUUID = pSender.getUniqueId();
        Location pLocation = pSender.getLocation();
        String path = pUUID.toString() + "." + coordName;

        if (coordsYML.contains(path)) {
            pSender.sendMessage(ChatColor.RED + "You have already assigned coordinates to this name. To delete it, use '/delcoord [name]'.");
            return true;
        }

        coordsYML.set(path, pLocation);
        SetCoord.getPlugin().saveCoordsYML();
        pSender.sendMessage(ChatColor.GOLD + "Assigned" + ChatColor.WHITE + " (" + df.format(pLocation.getX()) + ", " + df.format(pLocation.getY()) + ", " + df.format(pLocation.getZ()) + ") " + ChatColor.GOLD + "to " + ChatColor.WHITE + coordName + ChatColor.GOLD + ".");

        return true;
    }
}
