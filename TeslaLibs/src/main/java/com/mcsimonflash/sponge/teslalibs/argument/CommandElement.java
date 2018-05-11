package com.mcsimonflash.sponge.teslalibs.argument;

import com.google.common.collect.ImmutableList;
import com.mcsimonflash.sponge.teslalibs.argument.parser.ValueParser;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

public class CommandElement<T> extends org.spongepowered.api.command.args.CommandElement implements ValueParser<T> {

    protected final ValueParser<T> parser;

    public CommandElement(Text key, ValueParser<T> parser) {
        super(key);
        this.parser = parser;
    }

    public ValueParser<T> getParser() {
        return parser;
    }

    @Override
    public void parse(CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        parser.parse(getKey(), src, args, ctx);
    }

    @Override
    public T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return parser.parseValue(src, args);
    }

    @Override
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return parser.complete(src, args, ctx);
    }

}