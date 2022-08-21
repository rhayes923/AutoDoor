package com.ryan.autodoor.commands;

import com.ryan.autodoor.AutoDoor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AutoDoorCommand implements CommandExecutor {

    FileConfiguration config = AutoDoor.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "[AutoDoor] Invalid arguments!");
                return false;
            } else if (args[0].toLowerCase().equals("unidirectional")) {
                config.set("type", "unidirectional");
                AutoDoor.getInstance().saveConfig();
                player.sendMessage(ChatColor.RED + "[AutoDoor]" + ChatColor.LIGHT_PURPLE + " Type set to unidirectional");
                return true;
            } else if (args[0].toLowerCase().equals("omnidirectional")) {
                config.set("type", "omnidirectional");
                AutoDoor.getInstance().saveConfig();
                player.sendMessage(ChatColor.RED + "[AutoDoor]" + ChatColor.LIGHT_PURPLE + " Type set to omnidirectional");
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
