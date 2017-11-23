package com.mcsimonflash.sponge.teslacore.command.element.core;

import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.List;

/**
 * Represents an element within command arguments.
 */
public abstract class CommandElement extends org.spongepowered.api.command.args.CommandElement {

    protected CommandElement(String key) {
        super(Text.of(key));
    }

    @Override
    public abstract void parse(CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException;

    @Override
    public Object parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return null;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return Lists.newArrayList();
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.of();
    }

}