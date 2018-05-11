package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Locatable;

import java.util.Optional;

public class PositionParser extends StandardParser<Vector3d> {

    public static final ImmutableList<String> MODIFIERS = ImmutableList.of("#me", "#self", "#first", "#target");

    public PositionParser(ImmutableMap<String, String> messages) {
        super(messages);
    }

    @Override
    public Vector3d parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        if (arg.startsWith("#")) {
            switch (arg.toLowerCase()) {
                case "#me": case "#self":
                    if (src instanceof Locatable) {
                        return ((Locatable) src).getLocation().getPosition();
                    }
                    throw args.createError(getMessage("not-locatable","The use of <arg> requires the source to have a location.", "arg", arg));
                case "#first": case "#target":
                    if (src instanceof Entity) {
                        return BlockRay.from((Entity) src)
                                .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                                .build()
                                .end()
                                .map(BlockRayHit::getPosition)
                                .orElseThrow(() -> args.createError(getMessage("no-target","No block target found.")));
                    }
                    throw args.createError(getMessage("not-entity","The use of ", arg, " requires the source to be an entity."));
            }
            throw args.createError(getMessage("invalid-modifier","No known modifier with the name <arg>.", "arg", arg));
        }
        Optional<Vector3d> source = src instanceof Locatable ? Optional.of(((Locatable) src).getLocation().getPosition()) : Optional.empty();
        if (arg.contains(",")) {
            String[] split = arg.split(",");
            if (split.length == 3) {
                return parseVector(args, source, split[0], split[1], split[2]);
            }
            throw args.createError(getMessage("invalid-format","Expected three position components in <arg>, received <size>.", "arg", arg, "size", split.length));
        }
        return parseVector(args, source, arg, args.next(), args.next());
    }

    private Vector3d parseVector(CommandArgs args, Optional<Vector3d> source, String xArg, String yArg, String zArg) throws ArgumentParseException {
        return new Vector3d(parseComponent(args, xArg, source.map(Vector3d::getX)), parseComponent(args, yArg, source.map(Vector3d::getY)), parseComponent(args, zArg, source.map(Vector3d::getZ)));
    }

    private double parseComponent(CommandArgs args, String arg, Optional<Double> optPos) throws ArgumentParseException {
        try {
            if (arg.startsWith("~")) {
                double relative = optPos.orElseThrow(() -> args.createError(getMessage("not-locatable","The use of <arg> requires the source to have a location.", "arg", arg)));
                return arg.length() > 1 ? Double.parseDouble(arg.substring(1)) + relative : relative;
            }
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            throw args.createError(getMessage("invalid-number","The argument <arg> is not a number: <exception>", "arg", arg, "exception", e.getMessage()));
        }
    }

    @Override
    public ImmutableList<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return complete(args, MODIFIERS.stream());
    }

    /**
     * Creates a new {@link OrSourceParser} that returns the position of their
     * location if the source is {@link Locatable}.
     */
    public OrSourceParser<Vector3d> orSource() {
        return Arguments.orSource(s -> ((Locatable) s).getLocation().getPosition(), this, ImmutableMap.of("exception", "Unable to parse position and source does not have a location."));
    }

}