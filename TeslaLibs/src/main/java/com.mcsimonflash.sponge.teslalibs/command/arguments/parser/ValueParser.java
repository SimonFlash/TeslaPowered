package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.mcsimonflash.sponge.teslalibs.command.arguments.Arguments;
import com.mcsimonflash.sponge.teslalibs.command.arguments.CommandElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Identifiable;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@FunctionalInterface
public interface ValueParser<T> {

    /**
     * Parses a value from this parser. This is a {@link FunctionalInterface}
     * and may be defined in lambda expressions.
     */
    T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException;

    /**
     * Parses a value from this parser and adds it into the context under the
     * given key.
     */
    default void parse(Text key, CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        ctx.putArg(key, parseValue(src, args));
    }

    /**
     * Completes this parser for the given values.
     */
    default List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return ImmutableList.of();
    }

    /**
     * Creates a new {@link FunctionParser} with the given mapper.
     *
     * @see Arguments#function(Function, ValueParser, ImmutableMap)
     */
    default <R> FunctionParser<T, R> map(Function<T, R> mapper) {
        return Arguments.function(mapper, this, ImmutableMap.of());
    }

    /**
     * Creates a new {@link PredicateParser} with the given predicate.
     *
     * @see Arguments#predicate(Predicate, ValueParser, ImmutableMap)
     */
    default PredicateParser<T> filter(Predicate<T> predicate) {
        return Arguments.predicate(predicate, this, ImmutableMap.of());
    }

    /**
     * Creates a new {@link OptionalParser} with this parser.
     *
     * @see Arguments#optional(ValueParser, ImmutableMap)
     */
    default OptionalParser<T> optional() {
        return Arguments.optional(this, ImmutableMap.of());
    }

    /**
     * Creates a new {@link CommandElement} with the given key.
     */
    default CommandElement<T> toElement(String key) {
        return new CommandElement<>(Text.of(key), this);
    }

}