package com.mcsimonflash.sponge.teslacore.command.element.functional;

import com.mcsimonflash.sponge.teslacore.command.element.core.DelegateElement;
import com.mcsimonflash.sponge.teslacore.command.element.core.ValueElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapElement<T, R> extends DelegateElement<T, R> {

    private Map<T, R> map;

    public MapElement(String key, Map<T, R> map, ValueElement<T> delegate) {
        super(key, delegate);
        this.map = map;
    }

    @Override
    public R parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        T key = parseDelegate(src, args);
        R value = map.get(key);
        if (value != null) {
            return value;
        }
        throw args.createError(Text.of("Unknown key ", key, "."));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return map.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

}