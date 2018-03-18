package com.mcsimonflash.sponge.teslalibs.configuration;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ConfigurationNodeException extends ObjectMappingException {

    private final ConfigurationNode node;

    /**
     * Creates a new ConfigurationNodeException for the given node and given
     * message.
     *
     * @param node the node
     * @param message the message
     */
    public ConfigurationNodeException(ConfigurationNode node, String message) {
        super(message);
        this.node = node;
    }

    /**
     * Creates a new ConfigurationNodeException for the given node using the
     * given format and arguments for the message.
     *
     * @param node the node
     * @param format the format
     * @param args the arguments
     */
    public ConfigurationNodeException(ConfigurationNode node, String format, Object... args) {
        this(node, String.format(format, args));
    }

    /**
     * Creates a new ConfigurationNodeException for the given node with the
     * given cause.
     *
     * @param node the node
     * @param cause the cause
     */
    public ConfigurationNodeException(ConfigurationNode node, Throwable cause) {
        super(cause);
        this.node = node;
    }

    /**
     * @return the node
     */
    public ConfigurationNode getNode() {
        return node;
    }

    /**
     * Creates an unchecked exception with this as the cause with proper typing.
     *
     * @return the new unchecked exception
     */
    public Unchecked asUnchecked() {
        return new Unchecked(this);
    }

    public static class Unchecked extends RuntimeException {

        /**
         * Creates a new unchecked exception with the cause being the given
         * exception. This class preserves typing.
         *
         * @param exception the exception
         */
        private Unchecked(ConfigurationNodeException exception) {
            super(exception);
        }

        /**
         * Returns the cause of this exception, which is defined to be a
         * {@link ConfigurationNodeException}.
         *
         * @return the original exception
         */
        @Override
        public synchronized ConfigurationNodeException getCause() {
            return (ConfigurationNodeException) super.getCause();
        }

    }

}