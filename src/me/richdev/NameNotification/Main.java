package me.richdev.NameNotification;

import me.richdev.NameNotification.Configuration.ConfigurationVariables;
import me.richdev.NameNotification.Configuration.SettingsManager;
import me.richdev.NameNotification.Listeners.Listener_DELUXE;
import me.richdev.NameNotification.Listeners.Listener_NONE;
import me.richdev.NameNotification.Listeners.Overall;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;
import org.inventivetalent.update.spiget.spiget.SpigetUpdate;

public class Main extends JavaPlugin {

    private static Main instance;
    public String message_to_op = null;

    private Listener listenerCaller = null;

    @Override
    public void onEnable() {
        getLogger().info("Loading NameNotification...");
        // INIT INSTANCE
        instance = this;
        getLogger().info("Loading instance...");

        // REGISTER LISTENERS
        getLogger().info("Loading listener...");

        Bukkit.getServer().getPluginManager().registerEvents(new Overall(), this);

        if (Bukkit.getServer().getPluginManager().isPluginEnabled("DeluxeChat")) {
            getLogger().info("We found you are using DeluxeChat, hooking with DeluxeChat...");
            listenerCaller = new Listener_DELUXE();
        } else {
            listenerCaller = new Listener_NONE();
        }

        Bukkit.getServer().getPluginManager().registerEvents(listenerCaller, this);

        getLogger().info("Initializing updater...");

        SpigetUpdate spigetUpdate = new SpigetUpdate(this, 61202);


        spigetUpdate.setVersionComparator(VersionComparator.SEM_VER);
        spigetUpdate.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl, boolean canAutoDownload) {
                if (canAutoDownload) {
                    if (spigetUpdate.downloadUpdate()) {
                        getLogger().warning("The new version (" + newVersion + ") is now downloaded, you should reload the plugin.");
                        message_to_op = "The new version (" + newVersion + ") is now downloaded, you should reload the plugin.";
                    } else {
                        getLogger().warning("Update download failed, reason is " + spigetUpdate.getFailReason());
                        message_to_op = "Update download failed, reason is " + spigetUpdate.getFailReason() + ". Here is the download link: " + downloadUrl;
                    }
                }
            }

            @Override
            public void upToDate() {
                getLogger().fine("--------------------------------");
                getLogger().info("");
                getLogger().info("You have the latest version :)");
                getLogger().info("");
                getLogger().fine("--------------------------------");
            }
        });


        loadMetrics();

        getLogger().info("Done loading NameNotification");
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("namenotification")) {

            if (!sender.hasPermission("NameNotification.admin")) {
                sender.sendMessage(ChatColor.RED + "Forbidden.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_AQUA + "Commands for NameNotification:");
                sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " reload " + ChatColor.YELLOW + " - Reloads the configuration and plugin.");
            } else if (args[0].equalsIgnoreCase("reload")) {
                long start = System.currentTimeMillis();
                sender.sendMessage(ChatColor.YELLOW + "Trying to reload the plugin.");

                ConfigurationVariables.getInstance().load();

                SettingsManager.getConfig().reload();
                if (SettingsManager.getConfig().<Boolean>get("DoNotTouchMeUnlessAChangeLogOrDeveloperTellsYou.resetConfiguration")) {
                    SettingsManager.getConfig().reset();
                }

                sender.sendMessage(ChatColor.GREEN + "Plugin reloaded in: " + (System.currentTimeMillis() - start));
            }
        }

        return true;
    }

    private void loadMetrics() {
        Metrics metrics = new Metrics(this);

        metrics.addCustomChart(new Metrics.SimplePie("SearchMode", ()
                -> ConfigurationVariables.getInstance().SEARCH_MODE.toString()));

        metrics.addCustomChart(new Metrics.SimplePie("Sound", ()
                -> ConfigurationVariables.getInstance().SOUND.toString()));

        metrics.addCustomChart(new Metrics.SimplePie("Color", ()
                -> ConfigurationVariables.getInstance().NOTIFICATION_COLOR.toString()));
    }

}
