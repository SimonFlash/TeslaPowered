package com.mcsimonflash.sponge.teslacore.command.element.core;

import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.List;

@FunctionalInterface
public interface ValueElement<T> {

    T parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException;

    default List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return Lists.newArrayList();
    }

    default Text getUsage(CommandSource src) {
        return Text.of();
    }

    static ValueElement<String> next() {
        return next;
    }
    ValueElement<String> next = (src, args) -> args.next();

}