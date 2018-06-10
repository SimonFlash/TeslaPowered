package com.mcsimonflash.sponge.teslalibs.registry;

import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Tuple;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RegistryService<T> {

    private final Registry<T> registry;

    private RegistryService(Registry<T> registry) {
        this.registry = registry;
    }

    public static <T> RegistryService<T> of(Registry<T> registry) {
        return new RegistryService<>(registry);
    }

    public Optional<Registry.Entry<T>> get(String id) {
        return registry.get(id);
    }

    public Optional<T> getValue(String id) {
        return registry.getValue(id);
    }

    public Optional<PluginContainer> getContainer(String id) {
        return registry.getContainer(id);
    }

    public Map<String, Tuple<T, PluginContainer>> getAll() {
        return Collections.unmodifiableMap(registry.getAll());
    }

    public Set<Tuple<T, PluginContainer>> getDistinct() {
        return Collections.unmodifiableSet(registry.getDistinct());
    }

    public boolean register(String id, T value, PluginContainer container) {
        return registry.register(id, value, container);
    }

}