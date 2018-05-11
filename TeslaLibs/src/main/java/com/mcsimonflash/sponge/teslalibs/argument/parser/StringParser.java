package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;

import java.util.List;

public class StringParser extends StandardParser<String> {

    private final boolean remaining;

    public StringParser(boolean remaining, ImmutableMap<String, String> unused) {
        super(unused);
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
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        List<String> all = args.getAll();
        String arg = all.isEmpty() ? "" : all.get(all.size() - 1).toLowerCase();
        return Sponge.getServer().getOnlinePlayers().stream()
                .map(User::getName)
                .filter(n -> n.toLowerCase().startsWith(arg))
                .collect(ImmutableList.toImmutableList());
    }

}