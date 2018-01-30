package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;

import java.util.List;
import java.util.stream.Collectors;

public class StringParser extends StandardParser<String> {

    private final boolean remaining;

    public StringParser(boolean remaining, ImmutableMap<String, String> messages) {
        super(messages);
        this.remaining = remaining;
    }

    @Override
    public String parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        if (remaining) {
            StringBuilder sb = new StringBuilder(args.next());
            while (args.hasNext()) {
                sb.append(" ").append(args.next());
            }
            return sb.toString();
        }
        return args.next();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        List<String> all = args.getAll();
        String arg = all.size() > 0 ? all.get(all.size() - 1) : "";
        return Sponge.getServer().getOnlinePlayers().stream().map(User::getName).filter(n -> n.toLowerCase().startsWith(arg)).collect(Collectors.toList());
    }

}