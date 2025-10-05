package br.com.gokan.mcommandbuilderv2.api;

import br.com.gokan.mcommandbuilderv2.context.CommandContext;
import br.com.gokan.mcommandbuilderv2.provider.AutoCompleteProvider;
import br.com.gokan.mcommandbuilderv2.provider.CommandExecutor;
import br.com.gokan.mcommandbuilderv2.util.CommandMapUtil;
import br.com.gokan.mcommandbuilderv2.util.CommandWrapper;
import org.bukkit.command.CommandMap;

import java.util.*;
import java.util.function.Function;

public class CommandBuilder {

    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private String permission;
    private String messagePermission = "§cVocê não tem permissão para isso!";
    private CommandExecutor executor;
    private AutoCompleteProvider tabCompleter;
    private final Map<Integer, Function<CommandContext, List<String>>> autoCompletes = new HashMap<>();



    public CommandBuilder(String name) {
        this.name = name;
    }

    public String getMessagePermission(){
        return messagePermission;
    }

    public CommandBuilder setAliases(String... aliases) {
        this.aliases.clear();
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder setExecutor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public CommandBuilder setAutoComplete(AutoCompleteProvider provider) {
        this.tabCompleter = provider;
        return this;
    }

    public CommandBuilder setMessagePermission(String messagePermission) {
        this.messagePermission = messagePermission;
        return this;
    }

    private AutoCompleteProvider mergeAutoCompletes() {
        return ctx -> {
            int index = ctx.getArgs().length - 1;
            Function<CommandContext, List<String>> provider = autoCompletes.get(index);

            if (provider != null) {
                try {
                    return provider.apply(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (tabCompleter != null) {
                List<String> result = tabCompleter.complete(ctx);
                if (result != null) return result;
            }

            return Collections.emptyList();
        };
    }

    public CommandBuilder addAuto(int index, Function<CommandContext, List<String>> provider) {
        autoCompletes.put(index, provider);
        return this;
    }


    public void build() {
        CommandMap commandMap = CommandMapUtil.getCommandMap();
        AutoCompleteProvider finalComplleter = mergeAutoCompletes();
        CommandWrapper command = new CommandWrapper(name, this, aliases, permission, finalComplleter, executor);
        commandMap.register(name, command);
    }



}
