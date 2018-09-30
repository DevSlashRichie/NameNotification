package me.richdev.NameNotification;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        log("&aLoading NameNotification...");
        // INIT INSTANCE
        instance = this;
        log("&aLoading instance...");

        // REGISTER LISTENERS
        log("&aaLoading listener...");
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        log("&aDone loading NameNotification");
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));
    }

    public static Main getInstance() {
        return instance;
    }

}
