package com.mcsimonflash.sponge.teslacore.tesla;

import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslacore.utils.TeslaUtils;
import com.mcsimonflash.sponge.teslacore.logger.LoggerService;
import com.mcsimonflash.sponge.teslalibs.message.MessageService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.common.SpongeImpl;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public abstract class Tesla {

    private static final Map<String, Tesla> REGISTRY = Maps.newHashMap();
    public static final Optional<URL> Discord = TeslaUtils.parseURL("https://discord.gg/4wayq37");

    public final PluginContainer Container;
    public final String Id;
    public final String Name;
    public final String Version;
    public final String Description;
    public final Optional<URL> URL;
    public final Path Directory;
    public final LoggerService Logger;
    public final MessageService Messages;
    public final Text Prefix;

    public Tesla(PluginContainer container) {
        Container = container;
        Id = Container.getId();
        Name = Container.getName();
        Version = Container.getVersion().orElse("undefined");
        Description = Container.getDescription().orElse("undefined");
        URL = TeslaUtils.parseURL(Container.getUrl().orElse("https://en.wikipedia.org/wiki/Nikola_Tesla"));
        Directory = SpongeImpl.getPluginConfigDir().resolve(Id);
        Logger = new LoggerService(Container.getLogger());
        MessageService ms;
        try {
            Path translations = Directory.resolve("translations");
            Files.createDirectories(translations);
            Path messages = translations.resolve("messages.properties");
            if(Files.notExists(messages)) {
                Container.getAsset("messages.properties").get().copyToFile(messages);
            }
            ms = MessageService.of(translations, "messages");
        } catch (IOException e) {
            Logger.error("An error occurred initializing message translations. Using internal copies.");
            ms = MessageService.of(getClass().getClassLoader(), "assets/" + Id + "/messages");
        }
        Messages = ms;
        Prefix = Text.of(TextColors.DARK_GRAY, "[", TextColors.YELLOW, "Tesla", TextColors.GOLD, Name.substring(5), TextColors.DARK_GRAY, "]", TextColors.GRAY, " ");
        REGISTRY.put(Id, this);
    }

}