package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;

public abstract class DelegateParser<T, R> extends StandardParser<R> {

    protected final ValueParser<T> delegate;

    public DelegateParser(ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        super(messages);
        this.delegate = delegate;
    }

    @Override
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return delegate.complete(src, args, ctx);
    }

}