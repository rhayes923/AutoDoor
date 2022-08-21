package com.ryan.autodoor.listeners;

import com.ryan.autodoor.AutoDoor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Openable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoDoorListener implements Listener {

    FileConfiguration config = AutoDoor.getInstance().getConfig();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (config.getBoolean("enabled")) {
            Player player = e.getPlayer();
            Block block = player.getLocation().getBlock();
            if (config.getString("type").equals("unidirectional")) {
                String direction = player.getFacing().toString();

                switch (direction) {
                    case "NORTH":
                        block = block.getRelative(0, 0, -1);
                        break;
                    case "SOUTH":
                        block = block.getRelative(0, 0, 1);
                        break;
                    case "EAST":
                        block = block.getRelative(1, 0, 0);
                        break;
                    case "WEST":
                        block = block.getRelative(-1, 0, 0);
                        break;
                }

                if (block.getType().toString().contains("_DOOR")) {
                    openDoor(block, player.getWorld());
                }

            } else if (config.getString("type").equals("omnidirectional")) {
                if (block.getRelative(0, 0, -1).getType().toString().contains("_DOOR")) {
                    openDoor(block.getRelative(0, 0, -1), player.getWorld());
                }
                if (block.getRelative(0, 0, 1).getType().toString().contains("_DOOR")) {
                    openDoor(block.getRelative(0, 0, 1), player.getWorld());
                }
                if (block.getRelative(1, 0, 0).getType().toString().contains("_DOOR")) {
                    openDoor(block.getRelative(1, 0, 0), player.getWorld());
                }
                if (block.getRelative(-1, 0, 0).getType().toString().contains("_DOOR")) {
                    openDoor(block.getRelative(-1, 0, 0), player.getWorld());
                }
            }
        }
    }

    public void openDoor(Block block, World world) {
        BlockState blockState = block.getState();
        Openable openable = (Openable) blockState.getBlockData();
        if (!openable.isOpen()) {
            openable.setOpen(true);
            Sound open = Sound.BLOCK_WOODEN_DOOR_OPEN;
            Sound close = Sound.BLOCK_WOODEN_DOOR_CLOSE;
            if (block.getType().toString().equals("IRON_DOOR")) {
                open = Sound.BLOCK_IRON_DOOR_OPEN;
                close = Sound.BLOCK_IRON_DOOR_CLOSE;
            }
            world.playSound(block.getLocation(), open, 1.0f, 1.0f);
            blockState.setBlockData(openable);
            blockState.update();

            Sound finalClose = close;
            new BukkitRunnable() {
                @Override
                public void run() {
                    BlockState blockState = block.getState();
                    Openable openable = (Openable) blockState.getBlockData();
                    if (openable.isOpen()) {
                        openable.setOpen(false);
                        world.playSound(block.getLocation(), finalClose, 1.0f, 1.0f);
                        blockState.setBlockData(openable);
                        blockState.update();
                    }
                }
            }.runTaskLater(AutoDoor.getInstance(), config.getInt("delayInSeconds") * 20L);
        }
    }
}
