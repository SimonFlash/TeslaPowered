package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslalibs.animation.Animatable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.type.OrderedInventory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.Map;

public class View implements Animatable<Layout> {

    private final Inventory inventory;
    private final Map<Integer, Element> slots = Maps.newHashMap();
    private final PluginContainer container;
    private boolean closeable = true;

    /**
     * @see #of(InventoryArchetype, PluginContainer)
     */
    private View(InventoryArchetype archetype, PluginContainer container) {
        this.inventory = Inventory.builder()
                .of(archetype)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.EMPTY))
                .listener(InteractInventoryEvent.Close.class, this::processClose)
                .listener(ClickInventoryEvent.class, this::processClick)
                .build(container);
        this.container = container;
    }

    /**
     * Creates a new View with the given archetype for the given container.
     *
     * @param archetype the archetype of this inventory
     * @param container the container creating this view
     * @return the new view
     */
    public static View of(InventoryArchetype archetype, PluginContainer container) {
        return new View(archetype, container);
    }

    /**
     * Defines this view as encoded by the given layout. This method sets every
     * slot in the inventory with a registered element or {@link Element#EMPTY}.
     *
     * @param layout the layout representing this view
     * @return this view
     */
    public View define(Layout layout) {
        slots.clear();
        for (int i = 0; i < inventory.capacity(); i++) {
            setSlot(i, layout.getElement(i));
        }
        return this;
    }

    /**
     * Updates the existing view with the given layout. Indexes not present in
     * the layout will not have their associated slot changed.
     *
     * @param layout the layout to update this view
     * @return this view
     */
    public View update(Layout layout) {
        layout.getElements().forEach(this::setSlot);
        return this;
    }

    /**
     * Sets the slot of the given index to use the given element.
     *
     * @param index the slot index
     * @param element the element
     */
    public void setSlot(int index, Element element) {
        inventory.<OrderedInventory>query(OrderedInventory.class).getSlot(SlotIndex.of(index)).ifPresent(s -> {
            slots.put(index, element);
            s.set(element.getItem().createStack());
            slots.put(index, element);
        });
    }

    /**
     * Sets whether this inventory is closeable by the player. This view can
     * always be closed using {@link #close(Player)}.
     *
     * @param closeable true if the view can be closed, else false
     */
    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    /**
     * Opens this view for the given player.
     *
     * @param player the player
     */
    public void open(Player player) {
        Task.builder().execute(t -> player.openInventory(inventory, Cause.source(container).build())).submit(container);
    }

    /**
     * Closes this view for the given player, regardless of whether this view
     * is closeable or not.
     *
     * @param player the player
     */
    public void close(Player player) {
        boolean closeable = this.closeable;
        this.closeable = true;
        player.closeInventory(Cause.source(container).build());
        this.closeable = closeable;
    }

    /**
     * Processes a ClickInventoryEvent for the inventory of this view. If a
     * player is present, the slot is within this inventory, and an element is
     * registered for that slot, the element will be processed.
     *
     * @param event the event
     */
    private void processClick(ClickInventoryEvent event) {
        event.setCancelled(true);
        event.getCause().first(Player.class).ifPresent(p -> event.getTransactions().forEach(t -> t.getSlot().getProperty(SlotIndex.class, "slotindex").ifPresent(i -> {
            Element element = slots.get(i.getValue());
            if (element != null) {
                Task.builder().execute(task -> element.process(p)).submit(container);
            }
        })));
    }

    /**
     * Processes an InteractInventoryEvent.Close for the inventory of this view.
     * If this inventory is not closeable, the event will be canceled.
     *
     * @param event the event
     */
    private void processClose(InteractInventoryEvent.Close event) {
        event.setCancelled(!closeable);
    }

    /**
     * Updates this view with the given layout. Used in animations.
     *
     * @param frame the layout of the frame
     */
    @Override
    public void nextFrame(Layout frame) {
        update(frame);
    }

}