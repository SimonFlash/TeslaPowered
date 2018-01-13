package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.message.MessageService;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StandardParser<T> implements ValueParser<T> {

    protected final ImmutableMap<String, String> messages;

    public StandardParser(ImmutableMap<String, String> messages) {
        this.messages = messages;
    }

    public Text getMessage(String key, String def, Object... args) {
        return Text.of(new MessageService.Formatter(messages.getOrDefault(key, def)).args(args).toString());
    }

    public final List<String> complete(CommandArgs args, Stream<String> stream) {
        return args.nextIfPresent().map(String::toLowerCase).map(a -> stream.filter(s -> s.toLowerCase().startsWith(a))).orElse(stream).collect(Collectors.toList());
    }

}