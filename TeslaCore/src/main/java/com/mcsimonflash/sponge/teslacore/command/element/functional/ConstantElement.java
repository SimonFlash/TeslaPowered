package com.mcsimonflash.sponge.teslacore.command.element.functional;

import com.mcsimonflash.sponge.teslacore.command.element.core.StandardElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

public class ConstantElement<T> extends StandardElement<T> {

    private final T constant;

    public ConstantElement(String key, T constant) {
        super(key);
        this.constant = constant;
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return constant;
    }

}