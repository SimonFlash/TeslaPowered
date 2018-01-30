package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;

import java.util.List;
import java.util.Map;

public class ChoicesParser<T> extends StandardParser<T> {

    private final Map<String, T> choices;

    public ChoicesParser(Map<String, T> choices, ImmutableMap<String, String> messages) {
        super(messages);
        this.choices = choices;
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        T value = choices.get(arg.toLowerCase());
        if (value != null) {
            return value;
        }
        throw args.createError(getMessage("no-choice", "No choice found for <arg>.", "arg", arg));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return complete(args, choices.keySet().stream());
    }

}