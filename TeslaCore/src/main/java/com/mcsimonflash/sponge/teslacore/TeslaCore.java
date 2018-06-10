package com.mcsimonflash.sponge.teslacore;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.mcsimonflash.sponge.teslacore.tesla.Tesla;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = "teslacore", name = "TeslaCore", version = "1.1.3", dependencies = @Dependency(id = "teslalibs"), description = "Conductor of all things Tesla", authors = "Simon_Flash")
public class TeslaCore extends Tesla {

    @Inject
    public TeslaCore(PluginContainer container) {
        super(container);
    }

    @Listener(order = Order.FIRST)
    public void onInit(GameInitializationEvent event) {
        Logger logger = getContainer().getLogger();
        logger.info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        logger.info("|     TeslaPowered -- Simon_Flash     |");
        logger.info("|                                     |");
        Tesla.getRegistry().values().forEach(t -> {
            String message = t.getContainer().getId() + ": v" + t.getContainer().getVersion().orElse("unknown");
            logger.info("|      - " + message + Strings.repeat(" ", 29 - message.length()) + "|");
        });
        logger.info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
    }

}