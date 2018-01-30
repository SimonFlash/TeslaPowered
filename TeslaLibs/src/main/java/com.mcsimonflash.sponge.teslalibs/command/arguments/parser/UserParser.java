package com.mcsimonflash.sponge.teslalibs.command.arguments.parser;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.List;
import java.util.Optional;

public class UserParser extends StandardParser<User> implements ValueParser.OrSource<User>, ValueParser.ToUuid<User> {

    public UserParser(ImmutableMap<String, String> messages) {
        super(messages);
    }

    @Override
    public User parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        Player player = Sponge.getServer().getPlayer(arg).orElse(null);
        return player != null ? player : Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(arg).orElseThrow(() ->
                args.createError(getMessage("no-user","No user found with name <arg>.", "arg", arg)));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return complete(args, Sponge.getServiceManager().provideUnchecked(UserStorageService.class).getAll().stream().map(GameProfile::getName).filter(Optional::isPresent).map(Optional::get));
    }

}