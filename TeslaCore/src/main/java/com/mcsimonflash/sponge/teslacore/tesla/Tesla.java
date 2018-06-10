package com.mcsimonflash.sponge.teslacore.tesla;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslacore.logger.LoggerService;
import com.mcsimonflash.sponge.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.teslalibs.message.Message;
import com.mcsimonflash.sponge.teslalibs.message.MessageService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public abstract class Tesla {

    public static final Optional<URL> DISCORD = TeslaUtils.parseURL("https://discord.gg/4wayq37");
    private static final Map<String, Tesla> REGISTRY = Maps.newHashMap();

    private final PluginContainer container;
    private final Path directory;
    private final LoggerService logger;
    private final CommandService commands;
    private final MessageService messages;
    private final Text prefix;

    public Tesla(PluginContainer container) {
        this.container = container;
        directory = Sponge.getConfigManager().getPluginConfig(container).getDirectory();
        logger = LoggerService.of(container);
        messages = TeslaUtils.getMessageService(this);
        commands = CommandService.of(container);
        prefix = Message.toText("&8[&eTesla&6" + container.getName().substring(5) + "&8]&f: &7");
        REGISTRY.put(container.getId(), this);
    }

    public PluginContainer getContainer() {
        return container;
    }

    public Path getDirectory() {
        return directory;
    }

    public LoggerService getLogger() {
        return logger;
    }

    public MessageService getMessages() {
        return messages;
    }

    public CommandService getCommands() {
        return commands;
    }

    public Text getPrefix() {
        return prefix;
    }

    public static ImmutableMap<String, Tesla> getRegistry() {
        return ImmutableMap.copyOf(REGISTRY);
    }

}