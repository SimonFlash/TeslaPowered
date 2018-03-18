package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class WorldParser extends StandardParser<World> {

    public WorldParser(ImmutableMap<String, String> messages) {
        super(messages);
    }

    @Override
    public World parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        return Sponge.getServer().getWorld(arg).orElseThrow(() -> args.createError(getMessage("no-world","No world found with name <arg>.", "arg", arg)));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return complete(args, Sponge.getServer().getWorlds().stream().map(World::getName));
    }

    /**
     * Creates a new {@link OrSourceParser} that returns the world of the source
     * if they are a player.
     */
    public OrSourceParser<World> orSource() {
        return Arguments.orSource(s -> ((Player) s).getWorld(), this, ImmutableMap.of("exception", "Unable to parse world and source is not a Player."));
    }

}