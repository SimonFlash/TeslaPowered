package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.entity.living.player.User;

public class OrSourceParser<T extends User> extends DelegateParser<T, T> {

    public OrSourceParser(ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        super(delegate, messages);
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        Object state = args.getState();
        try {
            return delegate.parseValue(src, args);
        } catch (ArgumentParseException e) {
            args.setState(state);
            try {
                return (T) src;
            } catch (ClassCastException e1) {
                String name = e1.getMessage().substring(e1.getMessage().lastIndexOf(46)+1, e1.getMessage().length() - 1).toLowerCase();
                throw args.createError(getMessage("not-instance", "Expected an argument or the source to be a " + name + "."));
            }
        }
    }

}