package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class LocationParser extends StandardParser<Location<World>> {

    private ValueParser<World> world;
    private ValueParser<Vector3d> position;

    public LocationParser(ValueParser<World> world, ValueParser<Vector3d> position, ImmutableMap<String, String> unused) {
        super(unused);
        this.world = world;
        this.position = position;
    }

    @Override
    public Location<World> parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return new Location<>(world.parseValue(src, args), position.parseValue(src, args));
    }

    @Override
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        Object state = args.getState();
        try {
            world.parseValue(src, args);
            return position.complete(src, args, ctx);
        } catch (ArgumentParseException ignored) {
            args.setState(state);
            return world.complete(src, args, ctx);
        }
    }

    /**
     * Creates a new {@link OrSourceParser} that returns their location if the
     * source is {@link Locatable}.
     */
    public OrSourceParser<Location<World>> orSource() {
        return Arguments.orSource(s -> ((Locatable) s).getLocation(), this, ImmutableMap.of("exception", "Unable to parse location and source does not have a location."));
    }

}