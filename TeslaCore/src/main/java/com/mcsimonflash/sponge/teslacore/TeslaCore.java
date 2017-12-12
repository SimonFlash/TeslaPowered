package com.mcsimonflash.sponge.teslacore;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.teslacore.tesla.Tesla;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = "teslacore",
        name = "TeslaCore",
        version = "1.0.1",
        dependencies = @Dependency(id = "teslalibs"),
        description = "Conductor of all things Tesla",
        authors = "Simon_Flash")
public class TeslaCore extends Tesla {

    private static TeslaCore tesla;
    public static TeslaCore getTesla() {
        return tesla;
    }

    @Inject
    public TeslaCore(PluginContainer container) {
        super(container);
        tesla = this;
    }

    //  _________           __
    // /___  ___/          / /
    //    / /______ _____ / /____ _
    //   / // __  // ___// // __ `/
    //  / // ____//__  // // /_/ /
    //  \/ \____/ \___/ \/ \__,_/

}