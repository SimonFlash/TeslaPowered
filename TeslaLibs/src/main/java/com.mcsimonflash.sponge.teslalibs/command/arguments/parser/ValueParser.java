package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

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
import java.util.function.Predicate;

@FunctionalInterface
public interface ValueParser<T> {

    T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException;

    default void parse(Text key, CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        ctx.putArg(key, parseValue(src, args));
    }

    default List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return ImmutableList.of();
    }

    default <R> FunctionParser<T, R> map(Function<T, R> mapper) {
        return Arguments.function(mapper, this, ImmutableMap.of());
    }

    default PredicateParser<T> filter(Predicate<T> predicate) {
        return Arguments.predicate(predicate, this, ImmutableMap.of());
    }

    default OptionalParser<T> optional() {
        return Arguments.optional(this, ImmutableMap.of());
    }

    interface InRange<T extends Comparable> extends ValueParser<T> {

        default PredicateParser<T> inRange(Range<T> range) {
            return Arguments.predicate(range, this, ImmutableMap.of("fail-test", "Value <value> must be within range " + range + "."));
        }

    }

    interface OrSource<T extends User> extends ValueParser<T> {

        default OrSourceParser<T> orSource() {
            return Arguments.orSource(this, ImmutableMap.of());
        }

    }

    interface ToUuid<T extends Identifiable> extends ValueParser<T> {

        default FunctionParser<T, UUID> toUuid() {
            return map(Identifiable::getUniqueId);
        }

    }

    default CommandElement<T> toElement(String key) {
        return toElement(Text.of(key));
    }

    default CommandElement<T> toElement(Text key) {
        return new CommandElement<>(key, this);
    }

}