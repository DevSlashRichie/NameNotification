package me.richdev.NameNotification.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class Listener_NONE extends ListenerCaller implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void chatListener(AsyncPlayerChatEvent e) {

        new ListenerWorker(e.getMessage(), e.getFormat(), e.getPlayer(), e.getRecipients()) {
            @Override
            public void sendMessage(Set<Player> nil, String message, String format, Player target, Player player, String finalMessage) {
                String toSend = String.format(e.getFormat(), player.getName(), finalMessage);
                target.sendMessage(toSend);
                player.sendMessage(toSend);

            }
        }.chatHandler();

    }

}
