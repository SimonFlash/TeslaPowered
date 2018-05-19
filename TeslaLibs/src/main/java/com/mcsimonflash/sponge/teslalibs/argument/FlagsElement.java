package com.mcsimonflash.sponge.teslalibs.argument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslalibs.argument.parser.ValueParser;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FlagsElement extends CommandElement {

    private final ImmutableMap<ImmutableList<String>, CommandElement> flags;
    private final ImmutableMap<String, CommandElement> aliases;

    public FlagsElement(ImmutableMap<ImmutableList<String>, CommandElement> flags) {
        super(null);
        this.flags = flags;
        ImmutableMap.Builder<String, CommandElement> builder = ImmutableMap.builder();
        flags.forEach((k, v) -> k.forEach(f -> builder.put("-" + f.toLowerCase(), v)));
        aliases = builder.build();
    }

    @Override
    public void parse(CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        while (args.hasNext() && args.peek().startsWith("-")) {
            String[] split = args.next().split("=", 2);
            CommandElement element = aliases.get(split[0]);
            if (element == null) {
                throw args.createError(Text.of("Unknown flag ", split[0], "."));
            } else if (split.length == 2) {
                args.insertArg(split[1]);
            }
            try {
                element.parse(src, args, ctx);
            } catch (ArgumentParseException e) {
                throw args.createError(Text.of("Error parsing value for flag ", split[0], ": ", e.getText()));
            }
        }
    }

    @Override
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        List<ImmutableList<String>> unused = Lists.newArrayList(flags.keySet());
        try {
            while (args.hasNext() && args.peek().startsWith("-")) {
                String[] split = args.next().split("=", 2);
                CommandElement element = aliases.get(split[0].toLowerCase());
                if (element == null || split.length == 1 && !args.hasNext()) {
                    return unused.stream().flatMap(Collection::stream).map(s -> "-" + s).filter(s -> s.toLowerCase().startsWith(split[0].toLowerCase())).collect(ImmutableList.toImmutableList());
                } else if (split.length == 2) {
                    args.insertArg(split[1]);
                }
                Object state = args.getState();
                try {
                    element.parse(src, args, ctx);
                    unused.removeIf(l -> l.contains(split[0].substring(1).toLowerCase()));
                } catch (ArgumentParseException e) {
                    args.setState(state);
                    String start = split.length == 2 ? split[0] + "=" : "";
                    return element.complete(src, args, ctx).stream().map(s -> start + s).collect(ImmutableList.toImmutableList());
                }
            }
        } catch (ArgumentParseException ignored) {}
        return args.nextIfPresent().map(String::toLowerCase).map(a -> unused.stream()
                .flatMap(Collection::stream).map(s -> "-" + s).filter(s -> s.toLowerCase().startsWith(a)).collect(ImmutableList.toImmutableList()))
                .orElse(ImmutableList.of());
    }

    @Override
    public Text getUsage(CommandSource src) {
        List<Object> args = Lists.newArrayList();
        for (Map.Entry<ImmutableList<String>, CommandElement> entry : flags.entrySet()) {
            args.add("[" + String.join("|", entry.getKey()));
            Text usage = entry.getValue().getUsage(src);
            if (!usage.isEmpty()) {
                args.add(": ");
                args.add(usage);
            }
            args.add("] ");
        }
        return Text.of(args.toArray());
    }

    @Override
    @Deprecated
    protected Object parseValue(CommandSource src, CommandArgs args) {
        throw new UnsupportedOperationException("Attempted to parse a value from flags.");
    }

    public static class Builder {

        private static final ValueParser<Boolean> TRUE = (src, args) -> Boolean.TRUE;

        private Map<ImmutableList<String>, CommandElement> flags = Maps.newHashMap();

        /**
         * Adds a new flag with the given element and list of flags. The value
         * parsed is added into the context based on the key of the element, not
         * any of the flag aliases.
         */
        public Builder flag(CommandElement element, String... flags) {
            this.flags.put(Arrays.stream(flags).map(String::toLowerCase).collect(ImmutableList.toImmutableList()), element);
            return this;
        }

        /**
         * Adds a new flag with an element that returns true and list of flags.
         * If defined, 'true' is added to the context under the first flag.
         */
        public Builder flag(String... flags) {
            return flags.length != 0 ? flag(TRUE.toElement(flags[0]), flags) : this;
        }

        /**
         * Creates a new {@link FlagsElement} from this builder.
         */
        public FlagsElement build() {
            return new FlagsElement(ImmutableMap.copyOf(flags));
        }

    }

}