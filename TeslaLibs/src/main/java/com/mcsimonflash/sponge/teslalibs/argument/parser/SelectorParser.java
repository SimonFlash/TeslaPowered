package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.selector.Selector;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectorParser<T> extends DelegateParser<T, Set<T>> {

    private Function<Stream<Entity>, Stream<T>> function;

    public SelectorParser(Function<Stream<Entity>, Stream<T>> function, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        super(delegate, messages);
        this.function = function;
    }

    @Override
    public Set<T> parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        if (args.hasNext() && args.peek().startsWith("@")) {
            try {
                return function.apply(Selector.parse(args.next()).resolve(src).stream()).collect(Collectors.toSet());
            } catch (IllegalArgumentException e) {
                throw args.createError(getMessage("invalid-selector", "The selector is not in the correct format: <arg>.", "arg", e.getMessage()));
            }
        }
        return Sets.newHashSet(delegate.parseValue(src, args));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        Object state = args.getState();
        List<String> completions = delegate.complete(src, args, ctx);
        args.setState(state);
        return ImmutableList.<String>builder().addAll(completions).addAll(Selector.complete(args.nextIfPresent().orElse(""))).build();
    }

}