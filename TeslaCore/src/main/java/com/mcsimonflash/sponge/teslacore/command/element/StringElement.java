package com.mcsimonflash.sponge.teslacore.command.element;

import com.mcsimonflash.sponge.teslacore.command.element.core.StandardElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;

public class StringElement extends StandardElement<String> {

    private int quantity;

    public StringElement(String key, int quantity) {
        super(key);
        this.quantity = quantity;
    }

    @Override
    public String parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        if (quantity == 1) {
            return args.next();
        }
        StringBuilder builder = new StringBuilder(args.next());
        for (int i = 1; i < quantity && args.hasNext(); i++) {
            builder.append(" ").append(args.next());
        }
        return builder.toString();
    }

}