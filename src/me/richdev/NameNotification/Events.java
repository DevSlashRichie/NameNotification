package me.richdev.NameNotification;

import me.richdev.NameNotification.Configuration.ConfigurationVariables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Events implements Listener {

    private final static Pattern pattern = Pattern.compile("(§.)");

    @EventHandler(priority = EventPriority.LOW)
   public void chatListener(AsyncPlayerChatEvent e) {

       String message = e.getMessage();
       Player player = e.getPlayer();

        Player target = null;

        if (ConfigurationVariables.getInstance().SEARCH_MODE == ConfigurationVariables.SearchMode.OPTIMIZED) {
            for (String s : message.split(" ")) {
                target = Bukkit.getPlayer(s.replaceAll("[^\\w\\d]+", ""));
            }
        } else if (ConfigurationVariables.getInstance().SEARCH_MODE == ConfigurationVariables.SearchMode.PRECISE) {
            for (Player recipient : e.getRecipients()) {
                if (message.contains(recipient.getName())) {
                    target = recipient;
                    break;
                }
            }
        }

        if (target == null)
            return;

        if (target == player)
            return;

        e.getRecipients().remove(player);
        e.getRecipients().remove(target);
        e.setCancelled(true);

        message = message.replace(target.getName(), ConfigurationVariables.getInstance().NOTIFICATION_COLOR + target.getName());
       StringBuilder stringBuilder = new StringBuilder();

        String lastCode = ConfigurationVariables.getInstance().DEFAULT_COLOR + "";
       for (String s : message.split(" ")) {
           if (!s.contains(target.getName())) {
               Matcher matcher = pattern.matcher(s);
               if(matcher.find()) {
                   lastCode = matcher.group();
               }
           } else {
               s = s.replaceAll("(" + target.getName() + ")", "$1" + lastCode);
           }

           stringBuilder.append(s).append(" ");
       }

        // SEND MESSAGES AND SOUND

        String toSend = String.format(e.getFormat(), player.getName(), stringBuilder.toString());

        target.sendMessage(toSend);
        player.sendMessage(toSend);

        target.playSound(target.getLocation(), ConfigurationVariables.getInstance().SOUND,
                ConfigurationVariables.getInstance().VOLUME, ConfigurationVariables.getInstance().PITCH);

   }
}
