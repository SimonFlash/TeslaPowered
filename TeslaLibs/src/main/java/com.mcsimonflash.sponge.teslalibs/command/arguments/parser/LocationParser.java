package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;

public class LocationParser extends StandardParser<Location<World>> {

    private ValueParser<World> world;
    private ValueParser<Vector3d> vector3d;

    public LocationParser(ValueParser<World> world, ValueParser<Vector3d> vector3d, ImmutableMap<String, String> unused) {
        super(unused);
        this.world = world;
        this.vector3d = vector3d;
    }

    @Override
    public Location<World> parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return new Location<>(world.parseValue(src, args), vector3d.parseValue(src, args));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        Object state = args.getState();
        try {
            world.parseValue(src, args);
            return vector3d.complete(src, args, ctx);
        } catch (ArgumentParseException ignored) {
            args.setState(state);
            return world.complete(src, args, ctx);
        }
    }

}