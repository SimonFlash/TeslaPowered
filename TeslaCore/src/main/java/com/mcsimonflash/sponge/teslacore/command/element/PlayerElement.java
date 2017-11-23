package com.mcsimonflash.sponge.teslacore.command.element;

import com.mcsimonflash.sponge.teslacore.command.element.core.StandardElement;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerElement extends StandardElement<Player> {

    public PlayerElement(String key) {
        super(key);
    }

    @Override
    public Player parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        return Sponge.getServer().getPlayer(arg)
                .orElseThrow(() -> args.createError(Text.of("Argument ", arg, " is not a player.")));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        String arg = args.nextIfPresent().orElse("").toLowerCase();
        return Sponge.getServer().getOnlinePlayers().stream()
                .map(User::getName)
                .filter(n -> n.toLowerCase().startsWith(arg))
                .collect(Collectors.toList());
    }

}