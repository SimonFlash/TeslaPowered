package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.function.Supplier;

public class OptionalParser<T> extends DelegateParser<T, Optional<T>> {

    public OptionalParser(ValueParser<T> delegate, ImmutableMap<String, String> unused) {
        super(delegate, unused);
    }

    @Override
    public void parse(Text key, CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        parseValue(src, args).ifPresent(v -> ctx.putArg(key, v));
    }

    @Override
    public Optional<T> parseValue(CommandSource src, CommandArgs args) {
        if (args.hasNext()) {
            Object state = args.getState();
            try {
                return Optional.of(delegate.parseValue(src, args));
            } catch (ArgumentParseException e) {
                args.setState(state);
            }
        }
        return Optional.empty();
    }

    /**
     * Creates a new {@link ValueParser} that returns the optional value if
     * present or else the given value.
     */
    public ValueParser<T> orElse(T value) {
        return map(p -> p.orElse(value));
    }

    /**
     * Creates a new {@link ValueParser} that returns the optional value if
     * present or else gets the value from the given {@link Supplier<T>}.
     */
    public ValueParser<T> orElse(Supplier<T> supplier) {
        return map(p -> p.orElseGet(supplier));
    }

}