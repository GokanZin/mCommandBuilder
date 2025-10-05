package br.com.gokan.mcommandbuilderv2.context;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandContext {
    private final CommandSender sender;
    private final String[] args;
    private final String label;

    public CommandContext(CommandSender sender, String[] args, String label) {
        this.sender = sender;
        this.args = args;
        this.label = label;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Player getPlayer() {
        return sender instanceof Player ? (Player) sender : null;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    public boolean hasArg(int args) {
        return this.args.length >= args;
    }

    public String getArg(int args) {
        return (this.args.length > args) ? this.args[args] : null;
    }
    public String getArgOrEmpty(int index) {
        return index >= 0 && index < args.length ? args[index] : "";
    }

    public String getLabel() {
        return label;
    }

    public void send(String msg) {
        sender.sendMessage(msg);
    }
}