package me.richdev.NameNotification.Listeners;

import me.clip.deluxechat.events.DeluxeChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Set;

public class Listener_DELUXE extends ListenerCaller implements Listener {


    @EventHandler
    public void chatListener(DeluxeChatEvent e) {
        new ListenerWorker(e.getChatMessage(), null, e.getPlayer(), e.getRecipients()) {
            @Override
            public void sendMessage(Set<Player> rec, String message, String format, Player target, Player player, String finalMessage) {
                rec.addAll(Arrays.asList(target, player));
                e.setChatMessage(finalMessage);
            }
        }.chatHandler();
    }

}
