package br.com.gokan.mcommandbuilderv2.provider;

import br.com.gokan.mcommandbuilderv2.context.CommandContext;

import java.util.List;

public interface AutoCompleteProvider {
    List<String> complete(CommandContext ctx);

}
