package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;

import java.util.List;

public abstract class DelegateParser<T, R> extends StandardParser<R> {

    protected final ValueParser<T> delegate;

    public DelegateParser(ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        super(messages);
        this.delegate = delegate;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return delegate.complete(src, args, ctx);
    }

}