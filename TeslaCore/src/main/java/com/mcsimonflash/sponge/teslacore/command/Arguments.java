package com.mcsimonflash.sponge.teslacore.command;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.mcsimonflash.sponge.teslacore.command.element.PlayerElement;
import com.mcsimonflash.sponge.teslacore.command.element.StringElement;
import com.mcsimonflash.sponge.teslacore.command.element.UserElement;
import com.mcsimonflash.sponge.teslacore.command.element.core.ValueElement;
import com.mcsimonflash.sponge.teslacore.command.element.functional.*;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.Tristate;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

//Allow for generic typed return values
//Avoid manipulating the argument state, catching elements, and known arguments
//Allow for with completing multiple arguments
//Send usage if an argument cannot be completed
@Beta
public final class Arguments {

////////////////////////////////////////////////////////////////////////////////

    public static <T> ConstantElement<T> constant(String key, T constant) {
        return new ConstantElement<>(key, constant);
    }

    public static <T> ValueElement<T> predicate(String key, Predicate<T> predicate, ValueElement<T> element) {
        return new PredicateElement<>(key, predicate, element);
    }

    public static <T, R> FunctionElement<T, R> function(String key, Function<T, R> function, ValueElement<T> element) {
        return new FunctionElement<>(key, function, element);
    }

    public static <T, R> MapElement<T, R> map(String key, Map<T, R> map, ValueElement<T> element) {
        return new MapElement<>(key, map, element);
    }

    public static FlagsElement.Builder flags(String key) {
        return new FlagsElement.Builder(key);
    }

////////////////////////////////////////////////////////////////////////////////

    public static FunctionElement<String, Integer> intt(String key) {
        return new FunctionElement<>(key, Integer::valueOf, ValueElement.next());
    }

    public static FunctionElement<String, Long> longg(String key) {
        return new FunctionElement<>(key, Long::valueOf, ValueElement.next());
    }

    public static FunctionElement<String, Float> floatt(String key) {
        return new FunctionElement<>(key, Float::valueOf, ValueElement.next());
    }

    public static FunctionElement<String, Double> doublee(String key) {
        return new FunctionElement<>(key, Double::valueOf, ValueElement.next());
    }

    private static final ImmutableMap<String, Boolean> BOOLEANS = ImmutableMap.<String, Boolean>builder()
            .put("true", Boolean.TRUE).put("t", Boolean.TRUE).put("1", Boolean.TRUE)
            .put("false", Boolean.FALSE).put("f", Boolean.FALSE).put("0", Boolean.FALSE)
            .build();

    public static MapElement<String, Boolean> booleann(String key) {
        return new MapElement<>(key, BOOLEANS, string("string"));
    }

    private static final ImmutableMap<String, Tristate> TRISTATES = ImmutableMap.<String, Tristate>builder()
            .put("true", Tristate.TRUE).put("t", Tristate.TRUE).put("1", Tristate.TRUE)
            .put("false", Tristate.FALSE).put("f", Tristate.FALSE).put("0", Tristate.FALSE)
            .put("undefined", Tristate.UNDEFINED).put("u", Tristate.UNDEFINED).put("1/2", Tristate.UNDEFINED)
            .build();

    public static MapElement<String, Tristate> tristate(String key) {
        return new MapElement<>(key, TRISTATES, string("string"));
    }

    public static StringElement string(String key) {
        return new StringElement(key, 1);
    }

    public static StringElement strings(String key, int quantity) {
        return new StringElement(key, quantity);
    }

    public static StringElement remainingStrings(String key) {
        return new StringElement(key, Integer.MAX_VALUE);
    }

    public static PlayerElement player(String key) {
        return new PlayerElement(key);
    }

    public static UserElement user(String key) {
        return new UserElement(key);
    }

    public static <T extends Comparable> PredicateElement<T> range(String key, Range<T> range, ValueElement<T> element) {
        return new PredicateElement<>(key, range::contains, element);
    }

    public static <T extends Identifiable> FunctionElement<T, UUID> uuid(String key, ValueElement<T> element) {
        return new FunctionElement<>(key, Identifiable::getUniqueId, element);
    }

}