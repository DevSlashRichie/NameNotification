package me.richdev.NameNotification.Configuration;

import me.richdev.NameNotification.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Random;

public class ConfigurationVariables {

    public SearchMode SEARCH_MODE = SearchMode.PRECISE;
    public boolean MULTIPLE_SEARCH;
    public Sound SOUND = defaultOne();
    public int VOLUME = 10;
    public int PITCH = 1;
    public ChatColor DEFAULT_COLOR = ChatColor.RESET;
    public ChatColor NOTIFICATION_COLOR = ChatColor.YELLOW;

    public ConfigurationVariables() {
        load();
    }

    public static ConfigurationVariables getInstance() {
        return Main.getInstance().getConfigurationVariables();
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

        DEFAULT_COLOR = ChatColor.getByChar(SettingsManager.getConfig().get("DefaultChatColor", String.class));
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

    private Sound defaultOne() {
        for (Sound sound : Sound.values()) {
            if(sound.toString().contains("XP"))
                return sound;
        }
        return Sound.values()[new Random().nextInt(Sound.values().length)];
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
