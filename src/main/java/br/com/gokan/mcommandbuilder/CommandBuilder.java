package br.com.gokan.mcommandbuilder;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * CommandBuilder allows for the construction of commands with customizable execution logic,
 * permission checks, tab completion, and more.
 */
public class CommandBuilder implements CommandExecutor {
    private static final Map<Plugin, List<String>> registeredCommands = new HashMap<>();

    private final String name;
    private String permission;
    private String messagePermission;
    private List<String> aliases;
    private BiConsumer<CommandSender, String[]> executor;
    private BiFunction<CommandSender, String[], List<String>> tabCompleter;
    private BiConsumer<Player, String[]> executorPlayer;
    private BiConsumer<ConsoleCommandSender, String[]> executorConsole;

    /**
     * Initializes a new CommandBuilder with the specified command name.
     *
     * @param name the command name
     */
    public CommandBuilder(String name) {
        this.name = name;
        this.aliases = new ArrayList<>();
        this.executor = (sender, args) -> {};
        this.tabCompleter = (sender, args) -> Collections.emptyList();
        this.executorPlayer = null;
        this.executorConsole = null;
    }

    /**
     * Sets the required permission to execute the command.
     *
     * @param permission the permission string
     * @return the current CommandBuilder instance
     */
    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Sets the message to send when the sender lacks permission.
     *
     * @param messagePermission the permission message
     * @return the current CommandBuilder instance
     */
    public CommandBuilder setMessagePermission(String messagePermission) {
        this.messagePermission = messagePermission;
        return this;
    }

    /**
     * Sets the command aliases.
     *
     * @param aliases the command aliases
     * @return the current CommandBuilder instance
     */
    public CommandBuilder setAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    /**
     * Sets the executor to handle command execution.
     *
     * @param executor the command executor
     * @return the current CommandBuilder instance
     */
    public CommandBuilder executor(BiConsumer<CommandSender, String[]> executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Sets the executor specifically for players.
     *
     * @param executorPlayer the player-specific executor
     * @return the current CommandBuilder instance
     */
    public CommandBuilder executorPlayer(BiConsumer<Player, String[]> executorPlayer) {
        this.executorPlayer = executorPlayer;
        return this;
    }

    /**
     * Sets the executor specifically for console senders.
     *
     * @param executorConsole the console-specific executor
     * @return the current CommandBuilder instance
     */
    public CommandBuilder executorConsole(BiConsumer<ConsoleCommandSender, String[]> executorConsole) {
        this.executorConsole = executorConsole;
        return this;
    }

    /**
     * Sets the tab completer for the command.
     *
     * @param tabCompleter the tab completer
     * @return the current CommandBuilder instance
     */
    public CommandBuilder setTabCompleter(BiFunction<CommandSender, String[], List<String>> tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    /**
     * Executes the command with permission checks and appropriate executor delegation.
     *
     * @param sender  the source of the command
     * @param command the command which was executed
     * @param label   the alias of the command which was used
     * @param args    the arguments passed to the command
     * @return true if the command was executed successfully
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            if (messagePermission != null && !messagePermission.isEmpty()) {
                sender.sendMessage(messagePermission);
            }
            return true;
        }

        if (sender instanceof Player && executorPlayer != null) {
            executorPlayer.accept((Player) sender, args);
            return true;
        }

        if (sender instanceof ConsoleCommandSender && executorConsole != null) {
            executorConsole.accept((ConsoleCommandSender) sender, args);
            return true;
        }

        executor.accept(sender, args);
        return true;
    }

    /**
     * Registers the command with the specified plugin.
     *
     * @param plugin the plugin instance
     */
    public void registerCommand(Plugin plugin) {
        CommandMap commandMap = CommandMapUtil.getCommandMap();
        CommandBuilder builder = this;
        CommandWrapper command = new CommandWrapper(name, builder, aliases, permission, tabCompleter);
        commandMap.register(name, command);

        registeredCommands.computeIfAbsent(plugin, k -> new ArrayList<>()).add(name);
    }

    /**
     * Deregisters all commands registered by the specified plugin.
     *
     * @param plugin the plugin instance
     * @return the number of commands deregistered
     */
    public static int deRegister(Plugin plugin) {
        CommandMap commandMap = CommandMapUtil.getCommandMap();
        List<String> commands = registeredCommands.remove(plugin);
        if (commands == null) return 0;

        int count = 0;
        for (String commandName : commands) {
            try {
                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
                if (knownCommands.remove(commandName) != null) count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }
}