package com.mcsimonflash.sponge.teslacore.command.element.core;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.List;

/**
 * A {@link CommandElement} containing a {@link ValueElement} of type {@param T}
 * and returns an object of type {@param R}. Implementations are expected to
 * call {@link #parseDelegate} within {@link #parseValue}.
 *
 * @param <T> the return type of the delegate
 */
public abstract class DelegateElement<T, R> extends StandardElement<R> {

    private ValueElement<T> delegate;

    protected DelegateElement(String key, ValueElement<T> delegate) {
        super(key);
        this.delegate = delegate;
    }

    public T parseDelegate(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return delegate.parseValue(src, args);
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return delegate.complete(src, args, ctx);
    }

    @Override
    public Text getUsage(CommandSource src) {
        return delegate.getUsage(src);
    }

}