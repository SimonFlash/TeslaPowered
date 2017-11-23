package com.mcsimonflash.sponge.teslalibs.configuration;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.function.Consumer;

public class NodeUtils {

    /**
     * Accepts the given consumer if the given node is virtual.
     *
     * @param node the node
     * @param consumer the consumer
     */
    public static <T extends ConfigurationNode> void ifVirtual(T node, Consumer<T> consumer) {
        if (node.isVirtual()) {
            consumer.accept(node);
        }
    }

    /**
     * Accepts the given consumer if the given node is attached (not virtual).
     *
     * @param node the node
     * @param consumer the consumer
     */
    public static <T extends ConfigurationNode> void ifAttached(T node, Consumer<T> consumer) {
        if (!node.isVirtual()) {
            consumer.accept(node);
        }
    }

    /**
     * Returns true if the given node has a parent. If the node is virtual, this
     * result may not be accurate.
     *
     * @param node the node
     * @return true if the node's parent is not null
     */
    public static boolean hasParent(ConfigurationNode node) {
        return node.getParent() != null;
    }

    /**
     * Returns the root node of the given node, which is the last parent node.
     *
     * @param node the node
     * @return the root
     */
    public static <T extends ConfigurationNode> T getRoot(T node) {
        T root = node;
        while (hasParent(root)) {
            root = (T) root.getParent();
        }
        return root;
    }

    /**
     * Attempts to get a value from the given node using the given type token.
     * If the deserialization throws an {@link ObjectMappingException}, the
     * default is returned instead.
     *
     * @param node the node
     * @param token the type token
     * @param def the default
     * @return the value obtained
     */
    public static <T> T getOrDefault(ConfigurationNode node, TypeToken<T> token, T def) {
        T value = def;
        try {
            value = node.getValue(token, def);
        } catch (ObjectMappingException ignored) {}
        return value;
    }

    /**
     * Moves a node from one location to another deleting the previous location.
     *
     * @param from the node to move from
     * @param to the node to move to
     */
    public static void move(ConfigurationNode from, ConfigurationNode to) {
        to.setValue(from);
        from.setValue(null);
    }

    /**
     * Sets the comment of the given node to the given comment if the node is an
     * instance of {@link CommentedConfigurationNode}, else does nothing.
     *
     * @param node the node
     * @param comment the comment
     */
    public static void tryComment(ConfigurationNode node, String comment) {
        if (node instanceof CommentedConfigurationNode) {
            ((CommentedConfigurationNode) node).setComment(comment);
        }
    }

}