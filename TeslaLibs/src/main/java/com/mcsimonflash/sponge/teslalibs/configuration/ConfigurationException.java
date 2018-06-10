package com.mcsimonflash.sponge.teslalibs.configuration;

import ninja.leaping.configurate.ConfigurationNode;

public class ConfigurationException extends RuntimeException {

    private final ConfigurationNode node;

    /**
     * Creates a new ConfigurationNodeException for the given node and given
     * message.
     */
    public ConfigurationException(ConfigurationNode node, String message) {
        super(message);
        this.node = node;
    }

    /**
     * Creates a new ConfigurationNodeException for the given node using the
     * given format and arguments for the message.
     */
    public ConfigurationException(ConfigurationNode node, String format, Object... args) {
        this(node, String.format(format, args));
    }

    /**
     * Creates a new ConfigurationNodeException for the given node with the
     * given cause.
     */
    public ConfigurationException(ConfigurationNode node, Throwable cause) {
        super(cause);
        this.node = node;
    }

    /**
     * @return the node
     */
    public ConfigurationNode getNode() {
        return node;
    }

}