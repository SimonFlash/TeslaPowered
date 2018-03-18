package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

import java.util.function.Function;

public class FunctionParser<T, R> extends DelegateParser<T, R> {

    private final Function<T, R> function;

    public FunctionParser(Function<T, R> function, ValueParser<T> delegate, ImmutableMap<String, String> unused) {
        super(delegate, unused);
        this.function = function;
    }

    @Override
    public R parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return function.apply(delegate.parseValue(src, args));
    }

}