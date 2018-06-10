package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslalibs.animation.Animatable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.Map;
import java.util.function.Consumer;

public class View implements Animatable<Layout>, Displayable {

    private final Inventory inventory;
    private final Map<Integer, Element> slots = Maps.newHashMap();
    private final Consumer<Action<InteractInventoryEvent.Close>> closeAction;
    private final PluginContainer container;

    /**
     * @see #of(InventoryArchetype, PluginContainer)
     */
    private View(Inventory.Builder builder, Consumer<Action<InteractInventoryEvent.Close>> closeAction, PluginContainer container) {
        this.inventory = builder
                .listener(ClickInventoryEvent.class, this::processClick)
                .listener(InteractInventoryEvent.Close.class, this::processClose)
                .build(container);
        this.closeAction = closeAction;
        this.container = container;
    }

    /**
     * Creates a new {@link View} with the given archetype.
     *
     * @see View.Builder methods
     */
    public static View of(InventoryArchetype archetype, PluginContainer container) {
        return builder().archetype(archetype).build(container);
    }

    /**
     * Opens this view for the given player. The opening of the inventory is
     * delayed by a tick to ensure any events are properly canceled.
     */
    @Override
    public void open(Player player) {
        Task.builder().execute(t -> player.openInventory(inventory)).delayTicks(1).submit(container);
    }

    /**
     * Defines this view as encoded by the given layout. This method sets every
     * slot in the inventory with a registered element or {@link Element#EMPTY}.
     */
    public View define(Layout layout) {
        slots.clear();
        for (int i = 0; i < inventory.capacity(); i++) {
            setElement(i, layout.getElement(i));
        }
        return this;
    }

    /**
     * Updates the existing view with the given layout. Indexes not present in
     * the layout will not have their associated slot changed.
     */
    public View update(Layout layout) {
        layout.getElements().forEach(this::setElement);
        return this;
    }

    /**
     * Sets the element at the given index and adds it to the element registry.
     */
    public void setElement(int index, Element element) {
        inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(index))).first().set(element.getItem().createStack());
        slots.put(index, element);
    }

    /**
     * Processes a ClickInventoryEvent for the inventory of this view. If a
     * player is present, the slot is within this inventory, and an element is
     * registered for that slot, the element will be processed.
     */
    private void processClick(ClickInventoryEvent event) {
        event.setCancelled(true);
        event.getCause().first(Player.class).ifPresent(p -> event.getTransactions().forEach(t -> t.getSlot().getProperty(SlotIndex.class, "slotindex").ifPresent(i -> {
            Element element = slots.get(i.getValue());
            if (element != null) {
                element.process(new Action.Click<>(event, p, element, t.getSlot()));
            }
        })));
    }

    /**
     * Processes an InteractInventoryEvent.Close for the inventory of this view.
     * If this inventory is not closeable, the event will be canceled.
     */
    private void processClose(InteractInventoryEvent.Close event) {
        event.getCause().first(Player.class).ifPresent(p -> closeAction.accept(new Action<>(event, p)));
    }

    /**
     * Updates this view with the given layout frame.
     */
    @Override
    public void nextFrame(Layout frame) {
        update(frame);
    }

    /**
     * Creates a new builder for creating a {@link View}.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final Consumer<Action<InteractInventoryEvent.Close>> NONE = a -> {};

        private Inventory.Builder builder = Inventory.builder();
        private Consumer<Action<InteractInventoryEvent.Close>> closeAction = NONE;

        /**
         * Sets the archetype for the backing inventory.
         */
        public Builder archetype(InventoryArchetype archetype) {
            builder.of(archetype);
            return this;
        }

        /**
         * Adds a property to the backing inventory
         */
        public Builder property(InventoryProperty property) {
            builder.property(property);
            return this;
        }

        /**
         * Sets the close action that is accepted when this view is closed.
         */
        public Builder onClose(Consumer<Action<InteractInventoryEvent.Close>> action) {
            closeAction = action;
            return this;
        }

        /**
         * @return the created view
         */
        public View build(PluginContainer container) {
            return new View(builder, closeAction, container);
        }

    }

}