package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser extends StandardParser<Long> {

    public static final Pattern PATTERN = Pattern.compile("(?:([0-9]+)w)?(?:([0-9]+)d)?(?:([0-9]+)h)?(?:([0-9]+)m)?(?:([0-9]+)s)?(?:([0-9]+)ms)?");
    private static final long[] CONVERSIONS = {604800000, 86400000, 3600000, 60000, 1000, 1};

    public DurationParser(ImmutableMap<String, String> messages) {
        super(messages);
    }

    @Override
    public Long parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        try {
            return Long.parseLong(arg);
        } catch (NumberFormatException ignored) {
            Matcher matcher = PATTERN.matcher(arg);
            if (matcher.matches()) {
                long time = 0;
                for (int i = 1; i <= 6; i++) {
                    time += matcher.group(i) != null ? Integer.parseInt(matcher.group(i)) * CONVERSIONS[i - 1] : 0;
                }
                return time;
            }
            throw args.createError(getMessage("invalid-format", "Input <arg> does not match the format of a duration.", "arg", arg));
        }
    }

}