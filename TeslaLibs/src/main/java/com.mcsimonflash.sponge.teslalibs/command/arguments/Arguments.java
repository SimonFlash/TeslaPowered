package com.mcsimonflash.sponge.teslalibs.command.arguments;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.mcsimonflash.sponge.teslalibs.command.arguments.parser.*;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.Tristate;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class Arguments {

    public static final ImmutableMap<String, Boolean> BOOLEANS = ImmutableMap.<String, Boolean>builder()
            .put("true", Boolean.TRUE).put("t", Boolean.TRUE).put("1", Boolean.TRUE)
            .put("false", Boolean.FALSE).put("f", Boolean.FALSE).put("0", Boolean.FALSE)
            .build();

    public static final ImmutableMap<String, Tristate> TRISTATES = ImmutableMap.<String, Tristate>builder()
            .put("true", Tristate.TRUE).put("t", Tristate.TRUE).put("1", Tristate.TRUE)
            .put("false", Tristate.FALSE).put("f", Tristate.FALSE).put("0", Tristate.FALSE)
            .put("undefined", Tristate.UNDEFINED).put("u", Tristate.UNDEFINED).put("1/2", Tristate.UNDEFINED)
            .build();

    private static final ValueParser<String> STRING = (src, args) -> args.next();

    private static final ValueParser<String> REMAINING_STRINGS = (src, args) -> args.getRaw().substring(args.getRawPosition());

    private static final NumberParser<Integer> INTEGER = number(Integer::decode, ImmutableMap.of("invalid-format", "Expected <arg> to be an integer."));

    private static final NumberParser<Long> LONG = number(Long::decode, ImmutableMap.of("invalid-format", "Expected <arg> to be a long (integer)."));

    private static final NumberParser<Float> FLOAT = number(Float::valueOf, ImmutableMap.of("invalid-format", "Expected <arg> to be a float (decimal)."));

    private static final NumberParser<Double> DOUBLE = number(Double::valueOf, ImmutableMap.of("invalid-format", "Expected <arg> to be a double (decimal)."));

    private static final PlayerParser PLAYER = player(ImmutableMap.of());

    private static final UserParser USER = user(ImmutableMap.of());

    private static final ChoicesParser<Boolean> BOOLEAN = choices(BOOLEANS, ImmutableMap.of("no-choice", "Expected <key> to be a boolean (true/false)."));

    private static final ChoicesParser<Tristate> TRISTATE = choices(TRISTATES, ImmutableMap.of("no-choice", "Expected <key> to be a tristate (true/false/undefined)."));

    private static final WorldParser WORLD = world(ImmutableMap.of());

    private static final Vector3DParser VECTOR_3D = vector3d(ImmutableMap.of());

    public static ValueParser<String> string() {
        return STRING;
    }

    public static ValueParser<String> remainingStrings() {
        return REMAINING_STRINGS;
    }

    public static NumberParser<Integer> integer() {
        return INTEGER;
    }

    public static NumberParser<Long> longg() {
        return LONG;
    }

    public static NumberParser<Double> doublee() {
        return DOUBLE;
    }

    public static NumberParser<Float> floatt() {
        return FLOAT;
    }

    public static ChoicesParser<Boolean> booleann() {
        return BOOLEAN;
    }

    public static ChoicesParser<Tristate> tristate() {
        return TRISTATE;
    }

    public static PlayerParser player() {
        return PLAYER;
    }

    public static UserParser user() {
        return USER;
    }

    public static WorldParser world() {
        return WORLD;
    }

    public static Vector3DParser vector3d() {
        return VECTOR_3D;
    }

    public static <T extends Number & Comparable<T>> NumberParser<T> number(Function<String, T> function, ImmutableMap<String, String> messages) {
        return new NumberParser<>(function, messages);
    }

    public static <T> ChoicesParser<T> choices(Map<String, T> choices, ImmutableMap<String, String> messages) {
        return new ChoicesParser<>(choices, messages);
    }

    public static PlayerParser player(ImmutableMap<String, String> messages) {
        return new PlayerParser(messages);
    }

    public static UserParser user(ImmutableMap<String, String> messages) {
        return new UserParser(messages);
    }

    public static <T extends User> OrSourceParser<T> orSource(ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        return new OrSourceParser<>(delegate, messages);
    }

    public static WorldParser world(ImmutableMap<String, String> messages) {
        return new WorldParser(messages);
    }

    public static Vector3DParser vector3d(ImmutableMap<String, String> messages) {
        return new Vector3DParser(messages);
    }

    public static <T> PredicateParser<T> predicate(Predicate<T> predicate, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        return new PredicateParser<>(predicate, delegate, messages);
    }

    public static <T, R> FunctionParser<T, R> function(Function<T, R> function, ValueParser<T> delegate, ImmutableMap<String, String> unused) {
        return new FunctionParser<>(function, delegate, unused);
    }

    public static <T> OptionalParser<T> optional(ValueParser<T> delegate, ImmutableMap<String, String> unused) {
        return new OptionalParser<>(delegate, unused);
    }

    public static FlagsElement.Builder flags() {
        return new FlagsElement.Builder();
    }

}