package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.message.Message;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.text.Text;

import java.util.stream.Stream;

public abstract class StandardParser<T> implements ValueParser<T> {

    protected final ImmutableMap<String, String> messages;

    public StandardParser(ImmutableMap<String, String> messages) {
        this.messages = messages;
    }

    public Text getMessage(String key, String def, Object... args) {
        return Message.of(messages.getOrDefault(key, def)).args(args).toText();
    }

    public final ImmutableList<String> complete(CommandArgs args, Stream<String> stream) {
        return args.nextIfPresent().map(String::toLowerCase).map(a -> stream.filter(s -> s.toLowerCase().startsWith(a))).orElse(stream).collect(ImmutableList.toImmutableList());
    }

}