package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import java.util.List;

public class PlayerParser extends StandardParser<Player> implements ValueParser.OrSource<Player>, ValueParser.ToUuid<Player> {

    public PlayerParser(ImmutableMap<String, String> messages) {
        super(messages);
    }

    @Override
    public Player parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        return Sponge.getServer().getPlayer(arg).orElseThrow(() ->
                args.createError(getMessage("no-player","No player found with name <arg>.", "arg", arg)));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return complete(args, Sponge.getServer().getOnlinePlayers().stream().map(User::getName));
    }

}