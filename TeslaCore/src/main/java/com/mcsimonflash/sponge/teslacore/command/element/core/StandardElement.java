package com.mcsimonflash.sponge.teslacore.command.element.core;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;

/**
 * An {@link CommandElement} that parses an object of type {@param T}.
 *
 * @param <T> the return type of this element
 */
public abstract class StandardElement<T> extends CommandElement implements ValueElement<T> {

    protected StandardElement(String key) {
        super(key);
    }

    @Override
    public void parse(CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        T val = parseValue(src, args);
        ctx.putArg(getUntranslatedKey(), val);
    }

    @Override
    public abstract T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException;

}