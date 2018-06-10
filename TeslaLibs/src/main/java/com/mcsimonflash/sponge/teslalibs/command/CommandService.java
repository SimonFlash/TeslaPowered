package com.mcsimonflash.sponge.teslalibs.command;

import com.google.common.collect.*;
import com.google.inject.ProvisionException;
import com.mcsimonflash.sponge.teslalibs.message.Message;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Optional;

public class CommandService {

    private final PluginContainer container;
    private final ClassToInstanceMap<Command> instances = MutableClassToInstanceMap.create();

    /**
     * @see CommandService#of(PluginContainer)
     */
    private CommandService(PluginContainer container) {
        this.container = container;
    }

    /**
     * Creates a new {@link CommandService} for the given container. The command
     * service handles the creation and registration of commands by maintaining
     * a {@link ClassToInstanceMap} using reflection to instantiate the command
     * instance. The class must have a constructor with a single
     * {@link Command.Settings} parameter that is annotated with
     * {@link com.google.inject.Inject} or {@link javax.inject.Inject}.
     */
    public static CommandService of(PluginContainer container) {
        return new CommandService(container);
    }

    /**
     * @return the Command instance for this class. If the class could not be
     * instantiated, an {@link ProvisionException} is thrown.
     */
    public <T extends Command> T getInstance(Class<T> clazz) throws ProvisionException {
        T instance = instances.getInstance(clazz);
        if (instance == null) {
            try {
                Constructor<T> constructor = getDeclaredConstructor(clazz, Command.Settings.class);
                if (constructor != null) {
                    if (!constructor.isAnnotationPresent(com.google.inject.Inject.class) && !constructor.isAnnotationPresent(javax.inject.Inject.class)) {
                        throw new ProvisionException("Command.Settings constructor for class " + clazz.getName() + " must be annotated with @Inject (google/javax).");
                    }
                    Command.Settings settings = Command.Settings.of(CommandService.this);
                    Optional.ofNullable(clazz.getAnnotation(Aliases.class)).ifPresent(a -> settings.aliases(a.value()));
                    Optional.ofNullable(clazz.getAnnotation(Children.class)).ifPresent(c -> settings.children(c.value()));
                    Optional.ofNullable(clazz.getAnnotation(Description.class)).ifPresent(d -> settings.description(Message.toText(d.value())));
                    Optional.ofNullable(clazz.getAnnotation(Permission.class)).ifPresent(p -> settings.permission(p.value()));
                    instance = constructor.newInstance(settings);
                    instances.put(clazz, instance);
                } else if ((constructor = getDeclaredConstructor(clazz, CommandService.class)) != null) {
                    System.out.println("Notice: Class " + clazz.getName() + " is using a deprecated constructor in TeslaLibs' CommandService.");
                    if (!constructor.isAnnotationPresent(com.google.inject.Inject.class) && !constructor.isAnnotationPresent(javax.inject.Inject.class)) {
                        throw new ProvisionException("CommandService constructor for class " + clazz.getName() + " must be annotated with @Inject (google/javax).");
                    }
                    instance = constructor.newInstance(this);
                } else if ((constructor = getDeclaredConstructor(clazz)) != null) {
                    if (!Modifier.isPublic(constructor.getModifiers()) && !constructor.isAnnotationPresent(com.google.inject.Inject.class) && !constructor.isAnnotationPresent(javax.inject.Inject.class)) {
                        throw new ProvisionException("Empty constructor for class " + clazz.getName() + " must be public or annotated with @Inject (google/javax).");
                    }
                    instance = constructor.newInstance();
                } else {
                    throw new ProvisionException("Unable to retrieve a suitable constructor for class " + clazz.getName() + ".");
                }
                instances.put(clazz, instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ProvisionException("Unable to instantiate class " + clazz.getName(), e);
            }
        }
        return instance;
    }

    @Nullable
    private static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class... params) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(params);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Registers the command instance for the given class to Sponge.
     */
    public void register(Class<? extends Command> clazz) {
        Command command = getInstance(clazz);
        Sponge.getCommandManager().register(container, command.getSpec(), command.getAliases());
    }

    /**
     * Registers the command instances for all of the given classes.
     */
    @SafeVarargs
    public final void register(Class<? extends Command>... classes) {
        for (Class<? extends Command> clazz : classes) {
            register(clazz);
        }
    }

}