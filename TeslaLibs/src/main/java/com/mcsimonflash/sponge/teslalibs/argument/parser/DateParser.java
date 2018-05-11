package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

public class DateParser extends StandardParser<LocalDate> {

    public DateParser(ImmutableMap<String, String> messages) {
        super(messages);
    }

    @Override
    public LocalDate parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        try {
            return LocalDate.parse(arg);
        } catch (DateTimeParseException e) {
            throw args.createError(getMessage("invalid-format", "Input <arg> could not be parsed as a date: <exception>.", "arg", arg, "exception", e.getMessage()));
        }
    }

    @Override
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return complete(args, Stream.of(LocalDate.now().toString()));
    }


}
