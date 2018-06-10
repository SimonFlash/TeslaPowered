package com.mcsimonflash.sponge.teslalibs.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Tuple;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public class Registry<T> {

    private final Map<String, Entry<T>> registry = Maps.newHashMap();
    private final Set<Entry<T>> distinct = Sets.newHashSet();

    private Registry() {}

    public static <T> Registry<T> of() {
        return new Registry<>();
    }

    public Optional<Entry<T>> get(String id) {
        return Optional.ofNullable(registry.get(id.toLowerCase()));
    }

    public Optional<T> getValue(String id) {
        return get(id).map(Tuple::getFirst);
    }

    public Optional<PluginContainer> getContainer(String id) {
        return get(id).map(Tuple::getSecond);
    }

    public Map<String, Entry<T>> getAll() {
        return registry;
    }

    public Set<Entry<T>> getDistinct() {
        return distinct;
    }

    public boolean register(String id, T value, PluginContainer container) {
        id = id.toLowerCase();
        Entry<T> entry = new Entry<>(value, container);
        checkArgument(registry.putIfAbsent(container.getId() + ":" + id, entry) == null, "The id " + container.getId() + ":" + id + " has already been registered");
        distinct.add(entry);
        return registry.putIfAbsent(id, entry) == null;
    }

    public boolean unregister(Entry<T> entry) {
        return registry.values().removeAll(ImmutableList.of(entry));
    }

    public void clear() {
        registry.clear();
        distinct.clear();
    }

    public static class Entry<T> extends Tuple<T, PluginContainer> {

        private Entry(T value, PluginContainer container) {
            super(value, container);
        }

    }

}