package dev.dannychoi.setcoord;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/* Used this tutorial for coords yml file https://www.spigotmc.org/wiki/config-files/#using-custom-configurations */

public final class SetCoord extends JavaPlugin {
    private static SetCoord plugin;

    private File customConfigFile;
    private FileConfiguration coordsYML;

    @Override
    public void onEnable(){
        plugin = this;
        createCoordsYML();

        this.getCommand("coords").setExecutor(new CoordsCommand());
        this.getCommand("coord").setExecutor(new CoordCommand());
        this.getCommand("setcoord").setExecutor(new SetCoordCommand());
        this.getCommand("delcoord").setExecutor(new DelCoordCommand());
        this.getCommand("globalcoords").setExecutor(new GlobalCoordsCommand());
        this.getCommand("globalcoord").setExecutor(new GlobalCoordCommand());
        this.getCommand("setglobalcoord").setExecutor(new SetGlobalCoordCommand());
        this.getCommand("delglobalcoord").setExecutor(new DelGlobalCoordCommand());
    }

    public static SetCoord getPlugin() {
        return plugin;
    }

    public FileConfiguration getCoordsYML() {
        return this.coordsYML;
    }

    public void saveCoordsYML() {
        try {
            coordsYML.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCoordsYML() {
        customConfigFile = new File(getDataFolder(), "coordinates.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("coordinates.yml", false);
        }

        coordsYML = new YamlConfiguration();
        try {
            coordsYML.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
