package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.mcsimonflash.sponge.teslalibs.command.arguments.Arguments;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

import java.util.function.Function;

public class NumberParser<T extends Number & Comparable<T>> extends StandardParser<T> {

    private final Function<String, T> function;

    public NumberParser(Function<String, T> function, ImmutableMap<String, String> messages) {
        super(messages);
        this.function = function;
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        try {
            return function.apply(arg);
        } catch (NumberFormatException e) {
            throw args.createError(getMessage("invalid-number", "Unable to parse <arg> into a number.", "arg", arg));
        }
    }

    /**
     * Creates a new {@link PredicateParser} that verifies this number is within
     * the provided range.
     */
    public PredicateParser<T> inRange(Range<T> range) {
        return Arguments.predicate(range, this, ImmutableMap.of("invalid-value", "Value <value> must be within range " + range + "."));
    }

}