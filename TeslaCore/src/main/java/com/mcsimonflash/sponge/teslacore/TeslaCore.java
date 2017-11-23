package com.mcsimonflash.sponge.teslacore;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.teslacore.tesla.Tesla;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(id = "teslacore",
        name = "TeslaCore",
        version = "1.0.0",
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