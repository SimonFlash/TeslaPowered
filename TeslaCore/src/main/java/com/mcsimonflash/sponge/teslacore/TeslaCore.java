package com.mcsimonflash.sponge.teslacore;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.mcsimonflash.sponge.teslacore.tesla.Tesla;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = "teslacore", name = "TeslaCore", version = "1.1.0", dependencies = @Dependency(id = "teslalibs"), description = "Conductor of all things Tesla", authors = "Simon_Flash")
public class TeslaCore extends Tesla {

    @Inject
    public TeslaCore(PluginContainer container) {
        super(container);
    }

    @Listener(order = Order.FIRST)
    public void onInit(GameInitializationEvent event) {
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getLogger().info("|     TeslaPowered -- Simon_Flash     |");
        getLogger().info("|                                     |");
        Tesla.getRegistry().values().forEach(t -> {
            String message = t.getContainer().getId() + ": v" + t.getContainer().getVersion().orElse("unknown");
            getLogger().info("|      - " + message + Strings.repeat(" ", 29 - message.length()) + "|");
        });
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
    }

}