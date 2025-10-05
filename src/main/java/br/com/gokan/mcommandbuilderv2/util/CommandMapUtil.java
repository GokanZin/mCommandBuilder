package br.com.gokan.mcommandbuilderv2.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandMapUtil {

    public static CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            throw new RuntimeException("Could not get CommandMap", e);
        }
    }
}
