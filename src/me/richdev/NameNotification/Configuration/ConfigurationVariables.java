package me.richdev.NameNotification.Configuration;

import me.richdev.NameNotification.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class ConfigurationVariables {

    private static ConfigurationVariables instance = new ConfigurationVariables();
    public SearchMode SEARCH_MODE = SearchMode.PRECISE;
    public boolean MULTIPLE_SEARCH;
    public Sound SOUND = Sound.ORB_PICKUP;
    public int VOLUME = 10;
    public int PITCH = 1;
    public ChatColor DEFAULT_COLOR = ChatColor.RESET;
    public ChatColor NOTIFICATION_COLOR = ChatColor.YELLOW;

    public ConfigurationVariables() {
        load();
    }

    public static ConfigurationVariables getInstance() {
        return instance;
    }

    public void load() {
        SearchMode sm = SearchMode.getMode(SettingsManager.getConfig().get("SearchMode"));
        if (sm == null)
            Main.getInstance().getLogger().severe("This search_mode " + SettingsManager.getConfig().get("SearchMode") + " does not exists. Setting a default one.");
        else SEARCH_MODE = sm;

        Sound s = getSound(SettingsManager.getConfig().get("NotificationDetails.Sound.ID"));
        if (s == null)
            Main.getInstance().getLogger().severe("The sound " + SettingsManager.getConfig().get("NotificationDetails.Sound.ID") + " does not exists. Setting a default one.");
        else SOUND = s;

        VOLUME = SettingsManager.getConfig().get("NotificationDetails.Sound.volume");
        PITCH = SettingsManager.getConfig().get("NotificationDetails.Sound.pitch");

        DEFAULT_COLOR = ChatColor.getByChar(SettingsManager.getConfig().get("DefaultChatColor"));
        NOTIFICATION_COLOR = ChatColor.getByChar(SettingsManager.getConfig().get("NotificationDetails.NotificationColor"));

    }

    private Sound getSound(String name) {
        for (Sound value : Sound.values()) {
            if (value.toString().equals(name.toUpperCase())) {
                return value;
            }
        }
        return null;
    }

    public enum SearchMode {
        OPTIMIZED, PRECISE;

        public static SearchMode getMode(String name) {
            for (SearchMode value : SearchMode.values()) {
                if (value.toString().equals(name.toUpperCase())) {
                    return value;
                }
            }
            return null;
        }
    }
}
