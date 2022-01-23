package dev.dannychoi.setcoord;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class SetGlobalCoordCommand implements CommandExecutor {

    private DecimalFormat df = new DecimalFormat("#.###");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration coordsYML = SetCoord.getPlugin().getCoordsYML();

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }

        if (args.length <= 0) { // Syntax error, should be "/setglobalcoord [name]"
            return false; // This sends a message to player showing command syntax
        }

        String coordName = args[0];

        Player pSender = (Player) sender;
        Location pLocation = pSender.getLocation();
        String path = "global." + coordName;

        if (coordsYML.contains(path)) {
            pSender.sendMessage(ChatColor.RED + "There are already global coordinates assigned to this name. To delete it, use '/delglobalcoord [name]'.");
            return true;
        }

        coordsYML.set(path+".location", pLocation);
        coordsYML.set(path+".creator", pSender.getName());
        SetCoord.getPlugin().saveCoordsYML();
        pSender.sendMessage(ChatColor.GOLD + "Assigned" + ChatColor.WHITE + " (" + df.format(pLocation.getX()) + ", " + df.format(pLocation.getY()) + ", " + df.format(pLocation.getZ()) + ") " + ChatColor.GOLD + "to " + ChatColor.WHITE + coordName + ChatColor.GOLD + " as a global coordinate.");


        return true;
    }
}
