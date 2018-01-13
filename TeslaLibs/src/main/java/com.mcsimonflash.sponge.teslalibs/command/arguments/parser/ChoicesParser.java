package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

import java.util.Map;

public class ChoicesParser<T> extends StandardParser<T> {

    protected final Map<String, T> choices;

    public ChoicesParser(Map<String, T> choices, ImmutableMap<String, String> messages) {
        super(messages);
        this.choices = choices;
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String key = args.next();
        T value = choices.get(key.toLowerCase());
        if (value != null) {
            return value;
        }
        throw args.createError(getMessage("no-choice", "No choice found for <key>.", "key", key));
    }

}