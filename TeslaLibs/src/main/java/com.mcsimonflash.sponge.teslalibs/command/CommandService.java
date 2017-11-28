package com.mcsimonflash.sponge.teslalibs.command;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

public class CommandService {

    private final Injector injector;
    private final PluginContainer container;

    private CommandService(PluginContainer container) {
        this.injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(CommandService.class).toInstance(CommandService.this);
            }
        });
        this.container = container;
    }

    public static CommandService of(PluginContainer container) {
        return new CommandService(container);
    }

    public <T extends Command> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    public void register(Class<? extends Command> clazz) {
        Command command = injector.getInstance(clazz);
        Sponge.getCommandManager().register(container, command.getSpec(), command.getAliases());
    }

    @SafeVarargs
    public final void register(Class<? extends Command>... classes) {
        for (Class<? extends Command> clazz : classes) {
            register(clazz);
        }
    }

}