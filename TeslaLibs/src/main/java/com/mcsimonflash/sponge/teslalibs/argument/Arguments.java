package com.mcsimonflash.sponge.teslalibs.argument;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.argument.parser.*;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    private static final StringParser STRING = string(false, ImmutableMap.of());
    private static final StringParser REMAINING_STRINGS = string(true, ImmutableMap.of());
    private static final NumberParser<Integer> INTEGER = number(Integer::decode, ImmutableMap.of("invalid-format", "Expected <arg> to be an integer."));
    private static final NumberParser<Long> LONG = number(Long::decode, ImmutableMap.of("invalid-format", "Expected <arg> to be a long (integer)."));
    private static final NumberParser<Float> FLOAT = number(Float::valueOf, ImmutableMap.of("invalid-format", "Expected <arg> to be a float (decimal)."));
    private static final NumberParser<Double> DOUBLE = number(Double::valueOf, ImmutableMap.of("invalid-format", "Expected <arg> to be a double (decimal)."));
    private static final ChoicesParser<Boolean> BOOLEAN = choices(BOOLEANS, ImmutableMap.of("no-choice", "Expected <key> to be a boolean (true/false)."));
    private static final ChoicesParser<Tristate> TRISTATE = choices(TRISTATES, ImmutableMap.of("no-choice", "Expected <key> to be a tristate (true/false/undefined)."));
    private static final PlayerParser PLAYER = player(ImmutableMap.of());
    private static final SelectorParser<Player> PLAYER_SELECTOR = PLAYER.selector();
    private static final UserParser USER = user(ImmutableMap.of());
    private static final SelectorParser<User> USER_SELECTOR = USER.selector();
    private static final WorldParser WORLD = world(ImmutableMap.of());
    private static final PositionParser POSITION = position(ImmutableMap.of());
    private static final LocationParser LOCATION = location(WORLD.orSource(), POSITION, ImmutableMap.of());
    private static final CommandParser COMMAND = command(ImmutableMap.of());
    private static final DateParser DATE = date(ImmutableMap.of());
    private static final DurationParser DURATION = duration(ImmutableMap.of());

    /**
     * Parses a {@link String}.
     */
    public static ValueParser<String> string() {
        return STRING;
    }

    /**
     * Parses a {@link String} from all remaining arguments, joined by spaces.
     */
    public static ValueParser<String> remainingStrings() {
        return REMAINING_STRINGS;
    }

    /**
     * Parses an {@link Integer}. This supports decimal, hexadecimal, and octal
     * numbers as defined by {@link Integer#decode(String)}
     */
    public static NumberParser<Integer> intObj() {
        return INTEGER;
    }

    /**
     * Parses an {@link Long}.
     */
    public static NumberParser<Long> longObj() {
        return LONG;
    }

    /**
     * Parses an {@link Double}.
     */
    public static NumberParser<Double> doubleObj() {
        return DOUBLE;
    }

    /**
     * Parses an {@link Float}.
     */
    public static NumberParser<Float> floatObj() {
        return FLOAT;
    }

    /**
     * Parses an {@link Boolean}. Acceptable inputs are:
     *
     *  true: "true", "t", "1"
     *  false: "false", "f", "0"
     */
    public static ChoicesParser<Boolean> booleanObj() {
        return BOOLEAN;
    }

    /**
     * Parses an {@link Tristate}. Acceptable inputs are:
     *
     *  true: "true", "t", "1"
     *  false: "false", "f", "0"
     *  undefined: "undefined", "u", "1/2"
     */
    public static ChoicesParser<Tristate> tristate() {
        return TRISTATE;
    }

    /**
     * Parses an {@link Player} from the name of an online player.
     */
    public static PlayerParser player() {
        return PLAYER;
    }

    /**
     * Parses {@link Player} entities with a selector. If the input is not a
     * selector, this delegates to a {@link PlayerParser}.
     */
    public static SelectorParser<Player> playerSelector() {
        return PLAYER_SELECTOR;
    }

    /**
     * Parses an {@link User} from the name of a stored user.
     */
    public static UserParser user() {
        return USER;
    }

    /**
     * Parses {@link User} entities (which are {@link Player}s) with a selector.
     * If the input is not a selector, this delegates to a {@link UserParser}.
     */
    public static SelectorParser<User> userSelector() {
        return USER_SELECTOR;
    }

    /**
     * Parses a {@link World} from the name of a loaded world.
     */
    public static WorldParser world() {
        return WORLD;
    }

    /**
     * Parses a {@link Vector3d}. This parser supports the use of relative
     * positions as well as the modifiers #me, #self, #first, and #target.
     */
    public static PositionParser position() {
        return POSITION;
    }

    /**
     * Parses a {@link Location<World>}. This parser requires a position to be
     * defined, but the world may be assumed if the source has a location.
     */
    public static LocationParser location() {
        return LOCATION;
    }

    /**
     * Parses a {@link String} intended to represent a command. This parser only
     * provides suggestions for the command; it does not verify input.
     */
    public static CommandParser command() {
        return COMMAND;
    }

    /**
     * Parses a {@link java.time.LocalDate}. This parser uses the same format
     * as defined by {@link java.time.LocalDate#parse(CharSequence)}.
     */
    public static DateParser date() {
        return DATE;
    }

    /**
     * Parses a {@link Long} representing a duration in milliseconds. The input
     * may be either a long (representing milliseconds) or in the pattern:
     *
     *  [N]w[N]d[N]h[N]m[N]s[N]ms
     */
    public static DurationParser duration() {
        return DURATION;
    }

    /**
     * Creates a new {@link StringParser}. If remaining is true, all remaining
     * arguments will be joined with spaces, else only the next argument is
     * returned. There are no messages used.
     */
    public static StringParser string(boolean remaining, ImmutableMap<String, String> unused) {
        return new StringParser(remaining, unused);
    }

    /**
     * Creates a new {@link NumberParser}. Available messages are:
     *
     *  invalid-number: If the function throws a {@link NumberFormatException}.
     */
    public static <T extends Number & Comparable<T>> NumberParser<T> number(Function<String, T> function, ImmutableMap<String, String> messages) {
        return new NumberParser<>(function, messages);
    }

    /**
     * Creates a new {@link ChoicesParser}. Available messages are:
     *
     *  no-choice: If the argument is not a key in the choices map
     */
    public static <T> ChoicesParser<T> choices(Map<String, T> choices, ImmutableMap<String, String> messages) {
        return new ChoicesParser<>(choices, messages);
    }

    /**
     * Creates a new {@link PlayerParser}. Available message are:
     *
     *  no-player: If the argument is not the name of a player
     */
    public static PlayerParser player(ImmutableMap<String, String> messages) {
        return new PlayerParser(messages);
    }

    /**
     * Creates a new {@link UserParser}. Available message are:
     *
     *  no-user: If the argument is not the name of a user
     *  invalid-name: If the argument is not in the format of a username
     */
    public static UserParser user(ImmutableMap<String, String> messages) {
        return new UserParser(messages);
    }

    /**
     * Creates a new {@link OrSourceParser}. If a value could not be parsed from
     * the given delegate, it applies the function to the source and returns the
     * value. Available messages are:
     *
     *  exception: If an exception is thrown when applying the function
     */
    public static <T> OrSourceParser<T> orSource(Function<CommandSource, T> function, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        return new OrSourceParser<>(function, delegate, messages);
    }

    /**
     * Creates a new {@link WorldParser}. Available messages are:
     *
     *  no-world: If the argument is not the name of a world
     */
    public static WorldParser world(ImmutableMap<String, String> messages) {
        return new WorldParser(messages);
    }

    /**
     * Creates a new {@link PositionParser}. Available messages are:
     *
     *  not-entity: If #first or #target is used but the source is not an Entity
     *  not-locatable: If a modifier (#arg) or relative coordinates are used but
     *          the source is not a Locatable
     *  invalid-format: If the number of vector components given is incorrect
     *  invalid-modifier: If a modifier (#arg) that is unknown is used
     *  invalid-number: If a given coordinate is not a double
     */
    public static PositionParser position(ImmutableMap<String, String> messages) {
        return new PositionParser(messages);
    }

    /**
     * Creates a new {@link LocationParser} using the World and Vector3d values
     * from the given parsers. There are no messages used.
     */
    public static LocationParser location(ValueParser<World> world, ValueParser<Vector3d> position, ImmutableMap<String, String> unused) {
        return new LocationParser(world, position, unused);
    }

    /**
     * Creates a new {@link CommandParser}. There are no messages used.
     */
    public static CommandParser command(ImmutableMap<String, String> messages) {
        return new CommandParser(messages);
    }

    /**
     * Creates a new {@link DateParser}. Available messages are:
     *
     *  invalid-format: If the argument does not match the expected format
     */
    public static DateParser date(ImmutableMap<String, String> messages) {
        return new DateParser(messages);
    }

    /**
     * Creates a new {@link DurationParser}. Available messages are:
     *
     *  invalid-format: If the argument does not match the expected format
     */
    public static DurationParser duration(ImmutableMap<String, String> messages) {
        return new DurationParser(messages);
    }

    /**
     * Creates a new {@link CatalogTypeParser}. Available messages are:
     *
     *  no-type: If the argument is not the id of a registered catalog type.
     */
    public static <T extends CatalogType> CatalogTypeParser<T> catalogType(Class<T> type, ImmutableMap<String, String> messages) {
        return new CatalogTypeParser<>(type, messages);
    }

    /**
     * Creates a new {@link PredicateParser} that tests the value returned by
     * the delegate. Available messages are:
     *
     *  exception: If an exception is thrown when applying the predicate
     *  invalid-value: If the value does not match the predicate
     */
    public static <T> PredicateParser<T> predicate(Predicate<T> predicate, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        return new PredicateParser<>(predicate, delegate, messages);
    }

    /**
     * Creates a new {@link FunctionParser} that converts the value returned by
     * the delegate. Available messages are:
     *
     *  exception: If an exception is thrown when applying the function
     */
    public static <T, R> FunctionParser<T, R> function(Function<T, R> function, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        return new FunctionParser<>(function, delegate, messages);
    }

    /**
     * Creates a new {@link OptionalParser} that returns an optional of the
     * value returned by the delegate or empty if an exception is thrown. This
     * parser will add the value to the context if the optional is present.
     * There are no messages used.
     *
     * This parser cannot distinguish between exceptions caused by parsing a
     * value (such as an argument that is not a number) and exceptions from
     * modifying a value (such as checking if that number is in a range).
     */
    public static <T> OptionalParser<T> optional(ValueParser<T> delegate, ImmutableMap<String, String> unused) {
        return new OptionalParser<>(delegate, unused);
    }

    /**
     * Creates a new {@link SelectorParser} that accepts a selector for entities
     * of a given type. Entities that are not instances of the given class will
     * be filtered out. The returned set may be empty. Available messages are:
     *
     *  exception: If an exception is thrown when applying the function
     *  invalid-selector: If the selector could not be parsed
     */
    public static <T> SelectorParser<T> selector(Function<Stream<Entity>, Stream<T>> function, ValueParser<T> delegate, ImmutableMap<String, String> messages) {
        return new SelectorParser<>(function, delegate, messages);
    }

    /**
     * Creates a new {@link SequenceElement} that parses multiple elements in
     * order. This element is designed with improvements for tab completions,
     * particular with optional-like elements.
     */
    public static SequenceElement sequence(CommandElement... elements) {
        return new SequenceElement(ImmutableList.copyOf(elements));
    }

    /**
     * Creates a new builder for a {@link FlagsElement}. Flags must occur in a
     * defined spot in the command, and all flags begin with the character "-".
     * A value for a flag can be set with either "-flag value" or "-flag=value".
     */
    public static FlagsElement.Builder flags() {
        return new FlagsElement.Builder();
    }

}