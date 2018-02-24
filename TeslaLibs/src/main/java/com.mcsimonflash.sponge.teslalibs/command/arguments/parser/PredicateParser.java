package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

public class PredicateParser<T> extends DelegateParser<T, T> {

    private final Predicate<T> predicate;

    public PredicateParser(Predicate<T> predicate, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        super(delegate, messages);
        this.predicate = predicate;
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        T value = delegate.parseValue(src, args);
        if (predicate.apply(value)) {
            return value;
        }
        throw args.createError(getMessage("invalid-value", "The value <value> does not meet the requirements for this argument.", "value", value));
    }

}