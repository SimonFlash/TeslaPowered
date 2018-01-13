package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
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

}