package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

import java.util.function.Function;

public class OrSourceParser<T> extends DelegateParser<T, T> {

    protected final Function<CommandSource, T> function;

    public OrSourceParser(Function<CommandSource, T> function, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        super(delegate, messages);
        this.function = function;
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        Object state = args.getState();
        try {
            return delegate.parseValue(src, args);
        } catch (ArgumentParseException e) {
            args.setState(state);
            try {
                return function.apply(src);
            } catch (Exception ex) {
                throw args.createError(getMessage("exception", "<exception>", "exception", ex.getMessage()));
            }
        }
    }

}