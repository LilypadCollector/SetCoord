package dev.dannychoi.setcoord;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DelGlobalCoordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration coordsYML = SetCoord.getPlugin().getCoordsYML();
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }

        if (args.length <= 0) { // Syntax error, should be "/delglobalcoord [name]"
            return false; // This sends a message to player showing command syntax
        }

        String coordName = args[0];

        Player pSender = (Player) sender;
        UUID pUUID = pSender.getUniqueId();

        String path = "global." + coordName;

        if (!coordsYML.contains(path)) {
            sender.sendMessage(ChatColor.RED + "There are no coordinates assigned to this name. Type /globalcoords to see all saved global coordinates.");
            return true; 
        }

        coordsYML.set(path, null);
        SetCoord.getPlugin().saveCoordsYML();
        sender.sendMessage(ChatColor.GOLD + "Removed " + ChatColor.WHITE + coordName + ChatColor.GOLD + " from the global coordinates list.");

        return true;
    }
}
