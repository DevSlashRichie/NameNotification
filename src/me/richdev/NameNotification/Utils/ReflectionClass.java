package me.richdev.NameNotification.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ReflectionClass {
    public static void sendPacket(Player player, Object packet) {

        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static int getNumberVersion() {
        return Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].split("_")[1]);
    }

    public static void setPrivateField(Class clazz, Object objectToChange, String field, Object newValue) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            f.set(objectToChange, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

}
