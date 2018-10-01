package me.richdev.NameNotification.Listeners;

import me.richdev.NameNotification.Configuration.ConfigurationVariables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ListenerWorker {

    private final static Pattern pattern = Pattern.compile("(§.)");

    private String message;
    private String format;
    private Player player;
    private Set<Player> recipients;

    ListenerWorker(String message, String format, Player player, Set<Player> recipients) {
        this.message = message;
        this.format = format;
        this.player = player;
        this.recipients = recipients;
    }

    void chatHandler() {
        Player target = null;

        if (ConfigurationVariables.getInstance().SEARCH_MODE == ConfigurationVariables.SearchMode.OPTIMIZED) {
            for (String s : message.split(" ")) {
                target = Bukkit.getPlayer(s.replaceAll("[^\\w\\d]+", ""));
            }
        } else if (ConfigurationVariables.getInstance().SEARCH_MODE == ConfigurationVariables.SearchMode.PRECISE) {
            for (Player recipient : recipients) {
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

        recipients.remove(player);
        recipients.remove(target);

        message = message.replace(target.getName(), ConfigurationVariables.getInstance().NOTIFICATION_COLOR + target.getName());
        StringBuilder stringBuilder = new StringBuilder();

        String lastCode = ConfigurationVariables.getInstance().DEFAULT_COLOR + "";
        for (String s : message.split(" ")) {
            if (!s.contains(target.getName())) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    lastCode = matcher.group();
                }
            } else {
                s = s.replaceAll("(" + target.getName() + ")", "$1" + lastCode);
            }

            stringBuilder.append(s).append(" ");
        }

        // SEND MESSAGES AND SOUND
        target.playSound(target.getLocation(), ConfigurationVariables.getInstance().SOUND,
                ConfigurationVariables.getInstance().VOLUME, ConfigurationVariables.getInstance().PITCH);
        sendMessage(recipients, message, format, target, player, stringBuilder.toString());
    }

    public abstract void sendMessage(Set<Player> recipients, String message, String format, Player target, Player player, String finalMessage);


}
