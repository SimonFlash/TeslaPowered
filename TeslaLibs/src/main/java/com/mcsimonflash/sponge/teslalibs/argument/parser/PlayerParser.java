package com.mcsimonflash.sponge.teslalibs.argument.parser;

import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.util.Identifiable;

import java.util.List;
import java.util.UUID;

public class PlayerParser extends StandardParser<Player> {

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

    /**
     * Creates a new {@link OrSourceParser} that returns the source if they are
     * a {@link Player}.
     */
    public OrSourceParser<Player> orSource() {
        return Arguments.orSource(Player.class::cast, this, ImmutableMap.of("exception", "Unable to parse player and source is not a Player."));
    }

    /**
     * Creates a new {@link ValueParser} that maps this player to their uuid.
     */
    public ValueParser<UUID> toUuid() {
        return map(Identifiable::getUniqueId);
    }

}