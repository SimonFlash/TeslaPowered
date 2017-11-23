package com.mcsimonflash.sponge.teslacore.command.element.functional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslacore.command.Arguments;
import com.mcsimonflash.sponge.teslacore.command.element.core.CommandElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlagsElement extends CommandElement {

    private final Map<List<String>, CommandElement> flags;
    private final Map<String, CommandElement> aliases = Maps.newHashMap();

    public FlagsElement(String key, Map<List<String>, CommandElement> flags) {
        super(key);
        this.flags = flags;
        flags.forEach((l, e) -> l.forEach(f -> aliases.put("-" + f.toLowerCase(), e)));
    }

    @Override
    public void parse(CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        System.out.println("Next: " + (args.hasNext() ? args.peek() : "none"));
        while (args.hasNext() && args.peek().startsWith("-")) {
            String[] split = args.next().split("=", 2);
            System.out.println("Split: " + split[0] + "/" + (split.length == 2 ? split[1] : "none"));
            if (split.length == 2) {
                System.out.println("Inserting: " + split[1]);
                args.insertArg(split[1]);
            }
            CommandElement element = aliases.get(split[0].toLowerCase());
            if (element == null) {
                throw args.createError(Text.of("Unknown flag ", split[0], "."));
            }
            System.out.println("Parsing: " + element.getUntranslatedKey());
            element.parse(src, args, ctx);
            System.out.println("Next: " + (args.hasNext() ? args.peek() : "none"));
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        System.out.println(String.join(", ", aliases.keySet()));
        String[] split = args.nextIfPresent().orElse("").split("=", 2);
        if (split.length == 2) {
            System.out.println("Try element");
            CommandElement element = aliases.get(split[0]);
            if (element != null) {
                args.insertArg(split[1]);
                return element.complete(src, args, ctx);
            }
        }
        System.out.println("Split: " + split[0]);
        return aliases.keySet().stream().filter(s -> s.startsWith(split[0])).collect(Collectors.toList());
    }

    @Override
    public Text getUsage(CommandSource src) {
        List<Object> args = Lists.newArrayList();
        for (Map.Entry<List<String>, CommandElement> flag : flags.entrySet()) {
            args.add("[" + String.join("|", flag.getKey()));
            Text usage = flag.getValue().getUsage(src);
            if (!usage.isEmpty()) {
                args.add(" ");
                args.add(usage);
            }
            args.add("]");
        }
        return Text.of(args.toArray());
    }

    public static class Builder {

        private String key;
        private Map<List<String>, CommandElement> flags = Maps.newHashMap();

        public Builder(String key) {
            this.key = key;
        }

        public Builder flag(CommandElement element, String... flags) {
            this.flags.put(Lists.newArrayList(flags), element);
            return this;
        }

        public Builder flag(String... flags) {
            return flag(Arguments.constant(flags.length > 0 ? flags[0] : null, Boolean.TRUE), flags);
        }

        public FlagsElement build() {
            return new FlagsElement(key, flags);
        }

    }

}