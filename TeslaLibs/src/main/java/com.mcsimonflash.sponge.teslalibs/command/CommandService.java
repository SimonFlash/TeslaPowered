package com.mcsimonflash.sponge.teslalibs.command;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

public class CommandService {

    private final Injector injector;
    private final PluginContainer container;

    /**
     * @see CommandService#of(PluginContainer)
     */
    private CommandService(PluginContainer container) {
        this.injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(CommandService.class).toInstance(CommandService.this);
            }
        });
        this.container = container;
    }

    /**
     * Creates a new {@link CommandService} for the given container. The command
     * service handles the creation and registration of commands by providing a
     * centralized cache of {@link Command}s through a Guice {@link Injector}.
     */
    public static CommandService of(PluginContainer container) {
        return new CommandService(container);
    }

    /**
     * @return the Command instance for this class retrieved from the injector.
     */
    public <T extends Command> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    /**
     * Registers a command of the given class created through the injector.
     */
    public void register(Class<? extends Command> clazz) {
        Command command = injector.getInstance(clazz);
        Sponge.getCommandManager().register(container, command.getSpec(), command.getAliases());
    }

    /**
     * Registers a command for all of the given classes.
     */
    @SafeVarargs
    public final void register(Class<? extends Command>... classes) {
        for (Class<? extends Command> clazz : classes) {
            register(clazz);
        }
    }

}