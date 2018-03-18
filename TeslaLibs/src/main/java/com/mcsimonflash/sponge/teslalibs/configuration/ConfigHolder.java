package com.mcsimonflash.sponge.teslalibs.configuration;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;

/**
 * Contains a {@link ConfigurationLoader} and it's root node.
 */
public class ConfigHolder<T extends ConfigurationNode> {

    private final ConfigurationLoader loader;
    private final T node;

    /**
     * @see #of(ConfigurationLoader)
     */
    public ConfigHolder(ConfigurationLoader<T> loader) throws IOException {
        this.loader = loader;
        this.node = loader.load();
    }

    /**
     * Creates a new ConfigHolder for the given configuration loader.
     *
     * @param loader the configuration loader
     * @return the new config
     * @throws IOException if the config could not be parsed
     */
    public static <T extends ConfigurationNode> ConfigHolder<T> of(ConfigurationLoader<T> loader) throws IOException {
        return new ConfigHolder<>(loader);
    }

    /**
     * Returns the node represented by the given path from the root node.
     *
     * @param path the child nodes, if any
     * @return the node at the provided path
     */
    public T getNode(Object... path) {
        return (T) node.getNode(path);
    }

    /**
     * Attempts to save this loader with this node.
     *
     * @return true if the save was successful, else false.
     */
    public boolean save() {
        try {
            loader.save(node);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}