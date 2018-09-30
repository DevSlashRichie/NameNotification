package me.richdev.NameNotification;

import me.richdev.NameNotification.Configuration.ConfigurationVariables;
import me.richdev.NameNotification.Configuration.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.spiget.SpigetUpdate;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        getLogger().info("Loading NameNotification...");
        // INIT INSTANCE
        instance = this;
        getLogger().info("Loading instance...");

        // REGISTER LISTENERS
        getLogger().info("Loading listener...");
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);

        getLogger().info("Initializing updater...");
        new SpigetUpdate(this, 61202);
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
