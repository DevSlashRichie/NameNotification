package me.richdev.NameNotification.Listeners;

import me.richdev.NameNotification.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Overall implements Listener {

    @EventHandler()
    public void join(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("NameNotification.admin") || e.getPlayer().isOp()) {
            if (Main.getInstance().message_to_op != null)
                e.getPlayer().sendMessage(ChatColor.RED + Main.getInstance().message_to_op);
        }
    }

}
