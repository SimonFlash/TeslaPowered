package com.mcsimonflash.sponge.teslacore.command.element;

import com.mcsimonflash.sponge.teslacore.command.element.core.StandardElement;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserElement extends StandardElement<User> {

    public UserElement(String key) {
        super(key);
    }

    @Override
    public User parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        return Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(arg)
                .orElseThrow(() -> args.createError(Text.of("Argument ", arg, " is not a user.")));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return Sponge.getServiceManager().provideUnchecked(UserStorageService.class).getAll().stream()
                .map(GameProfile::getName).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

}