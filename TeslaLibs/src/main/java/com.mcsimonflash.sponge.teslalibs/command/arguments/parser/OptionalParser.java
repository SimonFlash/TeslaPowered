package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class OptionalParser<T> extends DelegateParser<T, Optional<T>> {

    public OptionalParser(ValueParser<T> delegate, ImmutableMap<String, String> unused) {
        super(delegate, unused);
    }

    @Override
    public void parse(Text key, CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        parseValue(src, args).ifPresent(v -> ctx.putArg(key, v));
    }

    @Override
    public Optional<T> parseValue(CommandSource src, CommandArgs args) {
        Object state = args.getState();
        try {
            return Optional.of(delegate.parseValue(src, args));
        } catch (ArgumentParseException e) {
            args.setState(state);
            return Optional.empty();
        }
    }

}