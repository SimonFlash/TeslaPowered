package com.mcsimonflash.sponge.teslacore.command.element.functional;

import com.mcsimonflash.sponge.teslacore.command.element.core.DelegateElement;
import com.mcsimonflash.sponge.teslacore.command.element.core.ValueElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

import java.util.function.Function;

public class FunctionElement<T, R> extends DelegateElement<T, R> {

    private Function<T, R> function;

    public FunctionElement(String key, Function<T, R> function, ValueElement<T> delegate) {
        super(key, delegate);
        this.function = function;
    }

    @Override
    public R parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return function.apply(parseDelegate(src, args));
    }

}