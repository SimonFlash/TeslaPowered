package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.Locatable;

public class CommandParser extends StandardParser<String> {

    public CommandParser(ImmutableMap<String, String> unused) {
        super(unused);
    }

    @Override
    public String parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return Arguments.remainingStrings().parseValue(src, args);
    }

    @Override
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        try {
            return ImmutableList.copyOf(Sponge.getCommandManager().getSuggestions(src, Arguments.remainingStrings().parseValue(src, args), src instanceof Locatable ? ((Locatable) src).getLocation() : null));
        } catch (ArgumentParseException ignored) {}
        return ImmutableList.of();
    }

}