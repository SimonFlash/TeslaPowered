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
    public void parse(Text key, CommandSource src, CommandArgs args, CommandContext ctx) {
        parseValue(src, args).ifPresent(value -> {
            if (value instanceof Iterable) {
                ((Iterable<?>) value).forEach(v -> ctx.putArg(key, v));
            } else {
                ctx.putArg(key, value);
            }
        });
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

    @Override
    public boolean isOptional() {
        return true;
    }

    /**
     * Creates a new {@link FunctionParser} that returns the optional value if
     * present or else the given value.
     */
    public FunctionParser<Optional<T>, T> orElse(T value) {
        return map(p -> p.orElse(value));
    }

    /**
     * Creates a new {@link FunctionParser} that returns the optional value if
     * present or else gets the value from the given {@link Supplier<T>}.
     */
    public FunctionParser<Optional<T>, T> orElseGet(Supplier<T> supplier) {
        return map(p -> p.orElseGet(supplier));
    }

}