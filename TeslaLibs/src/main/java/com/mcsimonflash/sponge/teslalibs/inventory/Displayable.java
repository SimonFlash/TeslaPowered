package com.mcsimonflash.sponge.teslalibs.inventory;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.function.Consumer;

public interface Displayable {

    /**
     * Opens this view for the given player. The opening of the inventory is
     * delayed using a task to ensure any event phases are properly exited.
     */
    void open(Player player);

    interface Builder {

        /**
         * Sets the archetype for the backing inventory.
         */
        Builder archetype(InventoryArchetype archetype);

        /**
         * Adds a property to the backing inventory.
         */
        Builder property(InventoryProperty property);

        /**
         * Sets the action that is accepted when this view is opened. The
         * {@link InteractInventoryEvent.Open} event is only fired when the
         * inventory is closed completely, not when changing views.
         */
        Builder onOpen(Consumer<Action<InteractInventoryEvent.Open>> action);

        /**
         * Sets the close action that is accepted when the inventory is closed.
         * The {@link InteractInventoryEvent.Close} event is only fired when the
         * inventory is closed completely, not when changing views.
         */
        Builder onClose(Consumer<Action<InteractInventoryEvent.Close>> action);

        /**
         * Builds and returns the representing {@link Displayable}. The given
         * {@link PluginContainer} is used to build the backing inventory and
         * submit tasks used when opening the inventory.
         */
        Displayable build(PluginContainer container);

    }

}