package com.mcsimonflash.sponge.teslacore.command.element.functional;

import com.mcsimonflash.sponge.teslacore.command.element.core.DelegateElement;
import com.mcsimonflash.sponge.teslacore.command.element.core.ValueElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.*;
import org.spongepowered.api.text.Text;

import java.util.function.Predicate;

public class PredicateElement<T> extends DelegateElement<T, T> {

    private final Predicate<T> predicate;

    public PredicateElement(String key, Predicate<T> predicate, ValueElement<T> delegate) {
        super(key, delegate);
        this.predicate = predicate;
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        T val = parseDelegate(src, args);
        if (predicate.test(val)) {
            return val;
        }
        throw args.createError(Text.of("Value ", val, " did match the given predicate."));
    }

}