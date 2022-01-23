package dev.dannychoi.setcoord;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class DelCoordCommand implements CommandExecutor {
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

        if (!coordsYML.contains(pUUID.toString())) {
            sender.sendMessage(ChatColor.RED + "This player does not have any coordinates saved. Maybe they changed their username?");
            return true;
        }

        String path = pUUID.toString() + "." + coordName;

        if (!coordsYML.contains(path)) {
            sender.sendMessage(ChatColor.RED + "There are no coordinates assigned to this name. Type /coords to see all saved coordinates.");
            return true;
        }

        coordsYML.set(path, null);
        SetCoord.getPlugin().saveCoordsYML();
        sender.sendMessage(ChatColor.GOLD + "Removed " + ChatColor.WHITE + coordName + ChatColor.GOLD + " from your coordinates list.");

        return true;
    }
}
