package com.mcsimonflash.sponge.teslacore.tesla;

import com.google.inject.Inject;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = "teslalibs", name = "TeslaLibs", version = "1.1.6", description = "A library for plugin developers", authors = "Simon_Flash")
public class TeslaLibs extends Tesla {

    @Inject
    private TeslaLibs(PluginContainer container) {
        super(container);
    }

}