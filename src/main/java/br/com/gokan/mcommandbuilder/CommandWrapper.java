package br.com.gokan.mcommandbuilder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class CommandWrapper extends Command {

    private final br.com.gokan.mcommandbuilder.CommandBuilder commandBuilder;
    private final String permission;
    private final BiFunction<CommandSender, String[], List<String>> tabCompleter;

    public CommandWrapper(String name, CommandBuilder commandBuilder, List<String> aliases, String permission,
                          BiFunction<CommandSender, String[], List<String>> tabCompleter) {
        super(name);
        this.commandBuilder = commandBuilder;
        this.permission = permission;
        this.tabCompleter = tabCompleter;
        setAliases(aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return commandBuilder.onCommand(sender, this, label, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (permission != null && !sender.hasPermission(permission))
            return new ArrayList<>();

        return tabCompleter.apply(sender, args);
    }
}
