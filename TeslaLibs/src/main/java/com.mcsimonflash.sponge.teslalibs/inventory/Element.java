package com.mcsimonflash.sponge.teslalibs.inventory;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.function.Consumer;

/* TODO:
 * Is there a need to have simplified methods to handle common consumers such
 * as process commands, open new views, etc.
 */
/* TODO:
 * Some GUI cases, such as a trade window, would need to allow players to edit
 * certain slots. Is this something feasible for this project, and if so how
 * should we go about determining who can edit a slot?
 *
 * Because this would need to be player specific, we'd likely have to have a
 * Function<Player, Boolean> to return whether the given Player can edit the
 * slot and cancel (or not) the event accordingly.
 *
 * An alternative is to consider having two independent views, and requiring a
 * simultaneous update of each.
 */
public class Element {

    public static final Element EMPTY = Element.of(ItemStack.of(ItemTypes.NONE, 1), player -> {});

    private final ItemStackSnapshot item;
    private final Consumer<Player> consumer;

    /**
     * @see #of(ItemStack, Consumer)
     */
    private Element(ItemStackSnapshot item, Consumer<Player> consumer) {
        this.item = item;
        this.consumer = consumer;
    }

    /**
     * Creates a new Element with the given item and consumer.
     *
     * @param item the item displayed for this element
     * @param consumer the consumer when this element is clicked
     * @return the newly created element
     */
    public static Element of(ItemStack item, Consumer<Player> consumer) {
        return new Element(item.createSnapshot(), consumer);
    }

    /**
     * Creates a new Element with the given item and an empty consumer
     *
     * @param item the item displayed for this element
     * @return the newly created element
     */
    public static Element of(ItemStack item) {
        return new Element(item.createSnapshot(), player -> {});
    }

    /**
     * @return the item
     */
    public ItemStackSnapshot getItem() {
        return item;
    }

    /**
     * Accepts the consumer for a given player. This method is called when an
     * {@link org.spongepowered.api.event.item.inventory.ClickInventoryEvent}
     * is registered for a slot with this element by the player.
     *
     * @param player the player who clicked on the slot
     */
    public void process(Player player) {
        consumer.accept(player);
    }

}