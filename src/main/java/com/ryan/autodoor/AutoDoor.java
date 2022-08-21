package com.ryan.autodoor;

import com.ryan.autodoor.commands.AutoDoorCommand;
import com.ryan.autodoor.listeners.AutoDoorListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoDoor extends JavaPlugin {

	static AutoDoor instance;
	FileConfiguration config = getConfig();

	@Override
	public void onEnable() {
		instance = this;
		config.addDefault("enabled", true);
		config.addDefault("type", "unidirectional");
		config.addDefault("delayInSeconds", 2);
		config.options().copyDefaults(true);
		saveConfig();
		this.getServer().getPluginManager().registerEvents(new AutoDoorListener(), this);
		this.getCommand("autodoor").setExecutor(new AutoDoorCommand());
	}
	
	@Override
	public void onDisable() {}

	public static AutoDoor getInstance() {
		return instance;
	}
}