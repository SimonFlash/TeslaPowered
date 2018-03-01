package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
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
            throw args.createError(getMessage("invalid-format", "Unable to parse ", arg, " into a date: " + e.getMessage()));
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return complete(args, Stream.of(LocalDate.now().toString()));
    }


}
