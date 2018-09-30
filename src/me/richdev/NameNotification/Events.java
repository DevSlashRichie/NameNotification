package me.richdev.NameNotification;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Events implements Listener {

    private final static Pattern pattern = Pattern.compile("(§.)");

   @EventHandler(priority = EventPriority.MONITOR)
   public void chatListener(AsyncPlayerChatEvent e) {

       String message = e.getMessage();
       Player player = e.getPlayer();

       if(!message.contains(player.getName()))
            return;

       message = message.replace(player.getName(), ChatColor.YELLOW + player.getName());

       StringBuilder stringBuilder = new StringBuilder();

       String lastCode = ChatColor.RESET + "";
       for (String s : message.split(" ")) {
           if(!s.contains(player.getName())) {
               Matcher matcher = pattern.matcher(s);
               if(matcher.find()) {
                   lastCode = matcher.group();
               }
           } else {
               s = s.replaceAll("(RichTheLord)", "$1" + lastCode);
           }

           stringBuilder.append(s).append(" ");
       }

       e.setMessage(stringBuilder.toString());

   }
}
