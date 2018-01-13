package com.mcsimonflash.sponge.teslalibs.command.arguments;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslalibs.command.arguments.parser.ValueParser;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Beta
public class FlagsElement extends CommandElement {

    public static final ValueParser<Boolean> TRUE = (src, args) -> Boolean.TRUE;

    private final ImmutableMap<List<String>, CommandElement> flags;
    private final ImmutableMap<String, CommandElement> aliases;

    public FlagsElement(@Nullable Text key, Map<List<String>, CommandElement> flags) {
        super(key);
        this.flags = ImmutableMap.copyOf(flags);
        ImmutableMap.Builder<String, CommandElement> builder = ImmutableMap.builder();
        flags.forEach((k, v) -> k.forEach(f -> builder.put("-" + f.toLowerCase(), v)));
        aliases = builder.build();
    }

    @Override
    public void parse(CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        while (args.hasNext() && args.peek().startsWith("-")) {
            String[] split = args.next().split("=", 2);
            CommandElement element = aliases.get(split[0].substring(1));
            if (element != null) {
                if (split.length == 2) {
                    args.insertArg(split[1]);
                }
                element.parse(src, args, ctx);
            }
            throw args.createError(Text.of("Unknown flag ", split[0], "."));
        }
    }

    @Override
    @Deprecated
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        throw new UnsupportedOperationException("Attempted to parse a value from flags.");
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        String[] split = args.nextIfPresent().orElse("").split("=", 2);
        if (split.length == 2) {
            CommandElement element = aliases.get(split[0]);
            if (element != null) {
                args.insertArg(split[1]);
                return element.complete(src, args, ctx);
            }
        }
        return aliases.keySet().stream().filter(s -> s.startsWith(split[0])).collect(Collectors.toList());
    }

    @Override
    public Text getUsage(CommandSource src) {
        List<Object> args = Lists.newArrayList();
        for (Map.Entry<List<String>, CommandElement> entry : flags.entrySet()) {
            args.add("[" + String.join("|", entry.getKey()) + ":");
            Text usage = entry.getValue().getUsage(src);
            if (!usage.isEmpty()) {
                args.add(" ");
                args.add(usage);
            }
            args.add("]");
        }
        return Text.of(args.toArray());
    }

    public static class Builder {

        private Map<List<String>, CommandElement> flags = Maps.newHashMap();

        public Builder flag(CommandElement element, String... flags) {
            this.flags.put(Lists.newArrayList(flags), element);
            return this;
        }

        public Builder flag(String... flags) {
            return flags.length != 0 ? flag(TRUE.toElement(flags[0])) : this;
        }

        public FlagsElement build(@Nullable Text key) {
            return new FlagsElement(key, flags);
        }

    }

}