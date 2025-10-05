package br.com.gokan.mcommandbuilderv2.util;

import br.com.gokan.mcommandbuilderv2.provider.CommandExecutor;
import br.com.gokan.mcommandbuilderv2.api.CommandBuilder;
import br.com.gokan.mcommandbuilderv2.context.CommandContext;
import br.com.gokan.mcommandbuilderv2.provider.AutoCompleteProvider;
import org.bukkit.command.*;

import java.util.Collections;
import java.util.List;

public class CommandWrapper extends Command  {

    private final CommandBuilder builder;
    private final String permission;
    private final AutoCompleteProvider tabCompleter;
    private final CommandExecutor executor;
    public CommandWrapper(String name, CommandBuilder builder, List<String> aliases, String permission, AutoCompleteProvider tabCompleter, CommandExecutor executor) {
        super(name);
        this.builder = builder;
        this.permission = permission;
        this.tabCompleter = tabCompleter;
        this.executor = executor;
        if (aliases != null && !aliases.isEmpty()) setAliases(aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage(builder.getMessagePermission());
            return true;
        }

        if (executor != null) {
            executor.execute(new CommandContext(sender, args, label));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (tabCompleter != null) {
            List<String> result = tabCompleter.complete(new CommandContext(sender, args, alias));
            return result != null ? result : Collections.emptyList();
        }
        return Collections.emptyList();
    }


}
