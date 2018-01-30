package com.mcsimonflash.sponge.teslalibs.command.arguments;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.*;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class SequenceElement extends CommandElement {

    private final ImmutableList<CommandElement> elements;

    public SequenceElement(ImmutableList<CommandElement> elements) {
        super(null);
        this.elements = elements;
    }

    @Override
    public void parse(CommandSource src, CommandArgs args, CommandContext ctx) throws ArgumentParseException {
        for (CommandElement element : elements) {
            element.parse(src, args, ctx);
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        for (CommandElement element : elements) {
            Object state = args.getState();
            try {
                element.parse(src, args, ctx);
                if (args.hasNext()) {
                    continue;
                }
            } catch (ArgumentParseException ignored) {}
            args.setState(state);
            return element.complete(src, args, ctx);
        }
        return ImmutableList.of();
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.joinWith(Text.of(" "), elements.stream().map(e -> e.getUsage(src)).collect(Collectors.toList()));
    }

    @Override
    @Deprecated
    protected Object parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        throw new UnsupportedOperationException("Attempted to parse a value from flags.");
    }

}