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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chat(AsyncPlayerChatEvent e) {
        e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
    }

   @EventHandler(priority = EventPriority.MONITOR)
   public void chatListener(AsyncPlayerChatEvent e) {

       String message = e.getMessage();
       Player player = e.getPlayer();

       //player.sendMessage("Message: " + message);
       //player.sendMessage("Format: " + format);

       if(!message.contains(player.getName()))
            return;

       message = message.replace(player.getName(), ChatColor.YELLOW + player.getName());

       StringBuilder stringBuilder = new StringBuilder();

       String lastCode = ChatColor.RESET + "";
       boolean isName = false;
       for (String s : message.split(" ")) {
           if(!s.contains(player.getName())) {
               if (isName) {
                   s = lastCode + s;
               }
               Matcher matcher = pattern.matcher(s);
               if(matcher.find()) {
                   lastCode = matcher.group();
               }
               isName = false;
           } else {
               isName = true;
               s = s.replaceAll("(RichTheLord)", "$1" + lastCode);
           }

           stringBuilder.append(s).append(" ");
       }

       e.setMessage(stringBuilder.toString());

   }
}
