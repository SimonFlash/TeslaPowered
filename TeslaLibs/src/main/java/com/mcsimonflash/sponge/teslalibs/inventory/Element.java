package com.mcsimonflash.sponge.teslalibs.inventory;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.function.Consumer;
import java.util.function.Function;

public class Element {

    public static final Element EMPTY = builder().build();

    private final ItemStackSnapshot item;
    private final Consumer<Action.Click> clickAction;

    /**
     * @see Element#of(ItemStack, Consumer)
     */
    private Element(ItemStackSnapshot item, Consumer<Action.Click> clickAction) {
        this.item = item;
        this.clickAction = clickAction;
    }

    /**
     * Creates a new {@link Element} with the given item and click action.
     *
     * @see Element.Builder methods
     */
    public static Element of(ItemStack item, Consumer<Action.Click> clickAction) {
        return builder().item(item).onClick(clickAction).build();
    }

    /**
     * Creates a new {@link Element} with the given item and empty click action.
     */
    public static Element of(ItemStack item) {
        return builder().item(item).build();
    }

    /**
     * Creates a new {@link Element} with the given item and click action.
     */
    public static Element of(ItemStackSnapshot item, Consumer<Action.Click> clickAction) {
        return builder().item(item).onClick(clickAction).build();
    }

    /**
     * Creates a new {@link Element} with the given item and empty click action.
     */
    public static Element of(ItemStackSnapshot item) {
        return builder().item(item).build();
    }

    /**
     * @return the item
     */
    public ItemStackSnapshot getItem() {
        return item;
    }

    /**
     * Processes this elements click action with the given {@link Action.Click}.
     */
    public void process(Action.Click action) {
        clickAction.accept(action);
    }

    /**
     * Creates a new builder for creating an {@link Element}.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final Consumer<Action.Click> NONE = a -> {};

        private ItemStackSnapshot item = ItemStackSnapshot.NONE;
        private Consumer<Action.Click> clickAction = NONE;

        /**
         * Sets the item to be the given {@link ItemStackSnapshot}.
         */
        public Builder item(ItemStackSnapshot item) {
            this.item = item;
            return this;
        }

        /**
         * Sets the item to be a snapshot of the given {@link ItemStack}.
         *
         * @see Element.Builder#item(ItemStackSnapshot)
         */
        public Builder item(ItemStack item) {
            return item(item.createSnapshot());
        }

        /**
         * Sets the click action that is accepted when this element is clicked.
         */
        public Builder onClick(Consumer<Action.Click> action) {
            clickAction = action;
            return this;
        }

        /**
         * @return the created element
         */
        public Element build() {
            return new Element(item, clickAction);
        }

    }

}