package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

import java.util.List;

public class LocationParser<T extends Extent> extends StandardParser<Location<T>> {

    private ValueParser<T> extent;
    private ValueParser<Vector3d> vector3d;

    public LocationParser(ValueParser<T> extent, ValueParser<Vector3d> vector3d, ImmutableMap<String, String> unused) {
        super(unused);
        this.extent = extent;
        this.vector3d = vector3d;
    }

    @Override
    public Location<T> parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return new Location<>(extent.parseValue(src, args), vector3d.parseValue(src, args));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        Object state = args.getState();
        try {
            extent.parseValue(src, args);
            return vector3d.complete(src, args, ctx);
        } catch (ArgumentParseException ignored) {
            args.setState(state);
            return extent.complete(src, args, ctx);
        }
    }

}