package br.com.gokan.mcommandbuilderv2.provider;

import br.com.gokan.mcommandbuilderv2.context.CommandContext;

public interface CommandExecutor {
    void execute(CommandContext ctx);
}
