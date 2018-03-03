package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.inventory.property.AbstractInventoryProperty;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.List;

public class Page {

    public static final Element FIRST = Element.builder().build();
    public static final Element LAST = Element.builder().build();
    public static final Element NEXT = Element.builder().build();
    public static final Element PREVIOUS = Element.builder().build();
    public static final Element CURRENT = Element.builder().build();

    private final List<View> views = Lists.newArrayList();
    private final InventoryArchetype archetype;
    private final ImmutableList<InventoryProperty> properties;
    private final Layout layout;
    private final PluginContainer container;

    /**
     * @see Page#of(Layout, InventoryArchetype, PluginContainer)
     */
    private Page(InventoryArchetype archetype, ImmutableList<InventoryProperty> properties, Layout layout, PluginContainer container) {
        this.archetype = archetype;
        this.properties = properties;
        this.layout = layout;
        this.container = container;
    }

    /**
     * Creates a new {@link Page} using the given layout as the template and
     * archetype for creating the backing {@link View}s.
     *
     * @see Page.Builder methods
     */
    public Page of(Layout layout, InventoryArchetype archetype, PluginContainer container) {
        return Page.builder().layout(layout).archetype(archetype).build(container);
    }

    /**
     * Defines this page to contain the given elements. The number of pages is
     * expanded as need be to fill the size of the elements.
     */
    public Page define(List<Element> elements) {
        views.clear();
        int capacity = archetype.getProperty(InventoryCapacity.class)
                .map(AbstractInventoryProperty::getValue)
                .orElse(54) - layout.getElements().size();
        int pages = elements.isEmpty() ? 1 : elements.size() / capacity + 1;
        for (int i = 1; i <= pages; i++) {
            View.Builder builder = View.builder().archetype(archetype);
            properties.forEach(builder::property);
            views.add(builder.build(container).define(Layout.builder()
                    .dimension(layout.getDimension())
                    .setAll(layout.getElements())
                    .replace(FIRST, createElement("First Page", i, 1))
                    .replace(LAST, createElement("Last Page", i, pages))
                    .replace(NEXT, createElement("Next Page", i, i == pages ? i : i + 1))
                    .replace(PREVIOUS, createElement("Previous Page", i, i == 1 ? i : i - 1))
                    .replace(CURRENT, createElement("Current Page", i, i))
                    .page(elements.subList((i - 1) * capacity, i == pages ? elements.size() : i * capacity))
                    .build()));
        }
        return this;
    }

    /**
     * Creates an element for a certain page number and target page.
     */
    private Element createElement(String name, int page, int target) {
        ItemStack item = ItemStack.builder()
                .itemType(page == target ? ItemTypes.MAP : ItemTypes.PAPER)
                .add(Keys.DISPLAY_NAME, Text.of(name, " (", target, ")"))
                .quantity(page)
                .build();
        return page == target ? Element.of(item) : Element.of(item, a -> views.get(page - 1).open(a.getPlayer()));
    }

    /**
     * Opens the given page for the player. Pages are indexed starting at 1. If
     * the index is out of bounds, the closed valid page will be opened.
     */
    public void open(Player player, int page) {
        views.get((page > 1 ? Math.min(page, views.size()) - 1 : 0)).open(player);
    }

    /**
     * Creates a new builder for creating {@link Page}s.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private InventoryArchetype archetype = InventoryArchetypes.DOUBLE_CHEST;
        private List<InventoryProperty> properties = Lists.newArrayList();
        private Layout layout;

        /**
         * Sets the archetype used for the backing {@link View}s.
         */
        public Builder archetype(InventoryArchetype archetype) {
            this.archetype = archetype;
            return this;
        }

        /**
         * Adds a property used for the backing {@link View}s.
         */
        public Builder property(InventoryProperty property) {
            properties.add(property);
            return this;
        }

        /**
         * Sets the layout used for the template of this view. It is expected
         * that the layout contains empty slots.
         */
        public Builder layout(Layout layout) {
            this.layout = layout;
            return this;
        }

        /**
         * @return the created page
         */
        public Page build(PluginContainer container) {
            Preconditions.checkState(layout != null, "layout");
            return new Page(archetype, ImmutableList.copyOf(properties), layout, container);
        }

    }

}