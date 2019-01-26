package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslalibs.animation.Animatable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class View implements Animatable<Layout>, Displayable {

    private final Inventory inventory;
    private final Map<Integer, Element> slots = Maps.newHashMap();
    private final Consumer<Action<InteractInventoryEvent.Open>> onOpen;
    private final Consumer<Action<InteractInventoryEvent.Close>> onClose;
    private final PluginContainer container;

    /**
     * @see #of(InventoryArchetype, PluginContainer)
     */
    private View(Builder builder, PluginContainer container) {
        Inventory.Builder b = Inventory.builder();
        builder.properties.forEach(b::property);
        inventory = b.of(builder.archetype)
                .listener(ClickInventoryEvent.class, this::processClick)
                .listener(InteractInventoryEvent.Open.class, this::processOpen)
                .listener(InteractInventoryEvent.Close.class, this::processClose)
                .build(container);
        onOpen = builder.onOpen;
        onClose = builder.onClose;
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
     * delayed to ensure any event phases are properly exited, and thus can be
     * called safely in actions.
     *
     * By closing the inventory first, the {@link InteractInventoryEvent.Close}
     * is fired when changing inventories through this method.
     */
    @Override
    public void open(Player player) {
        Task.builder().execute(t -> {
            player.closeInventory();
            player.openInventory(inventory);
        }).submit(container);
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
     * Processes a {@link ClickInventoryEvent} for this view. If there are
     * transactions that modify this inventory, the event is canceled and then
     * elements are processed if a {@link Player} is in the cause.
     */
    private void processClick(ClickInventoryEvent event) {
        List<Slot> slots = event.getTransactions().stream()
                .map(SlotTransaction::getSlot)
                .filter(s -> s.getInventoryProperty(SlotIndex.class).filter(i -> i.getValue() < inventory.capacity()).isPresent())
                .collect(Collectors.toList());
        if (!slots.isEmpty()) {
            event.setCancelled(true);
            event.getCause().first(Player.class).ifPresent(p -> slots.forEach(s -> s.getInventoryProperty(SlotIndex.class)
                    .map(i -> this.slots.get(i.getValue()))
                    .ifPresent(e -> e.process(new Action.Click<>(event, p, e, s)))));
        }
    }

    /**
     * Processes a {@link InteractInventoryEvent.Open} event for this view,
     * accepting an onOpen action if defined.
     */
    private void processOpen(InteractInventoryEvent.Open event) {
        if (onOpen != null) {
            event.getCause().first(Player.class).ifPresent(p -> onOpen.accept(new Action<>(event, p)));
        }
    }

    /**
     * Processes {@link InteractInventoryEvent.Close} event for this view,
     * accepting an onOpen action if defined.
     */
    private void processClose(InteractInventoryEvent.Close event) {
        if (onClose != null) {
            event.getCause().first(Player.class).ifPresent(p -> onClose.accept(new Action<>(event, p)));
        }
    }

    /**
     * Updates this view with the given layout frame.
     */
    @Override
    @Deprecated
    public void nextFrame(Layout frame) {
        update(frame);
    }

    /**
     * Creates a new builder for creating a {@link View}.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Displayable.Builder {

        private InventoryArchetype archetype = InventoryArchetypes.DOUBLE_CHEST;
        private List<InventoryProperty> properties = Lists.newArrayList();
        private Consumer<Action<InteractInventoryEvent.Open>> onOpen;
        private Consumer<Action<InteractInventoryEvent.Close>> onClose;

        /**
         * Sets the archetype for the backing inventory.
         */
        @Override
        public Builder archetype(InventoryArchetype archetype) {
            this.archetype = archetype;
            return this;
        }

        /**
         * Adds a property to the backing inventory
         */
        @Override
        public Builder property(InventoryProperty property) {
            properties.add(property);
            return this;
        }

        /**
         * Sets the action for when this view is opened.
         */
        @Override
        public Builder onOpen(Consumer<Action<InteractInventoryEvent.Open>> action) {
            onOpen = action;
            return this;
        }

        /**
         * Sets the action for when this view is closed.
         */
        @Override
        public Builder onClose(Consumer<Action<InteractInventoryEvent.Close>> action) {
            onClose = action;
            return this;
        }

        /**
         * @return the created view
         */
        @Override
        public View build(PluginContainer container) {
            return new View(this, container);
        }

    }

}