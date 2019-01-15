package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Page implements Displayable {

    public static final Element FIRST = Element.builder().build();
    public static final Element LAST = Element.builder().build();
    public static final Element NEXT = Element.builder().build();
    public static final Element PREVIOUS = Element.builder().build();
    public static final Element CURRENT = Element.builder().build();

    private final List<View> views = Lists.newArrayList();
    private final View.Builder view;
    private final Layout layout;
    private final PluginContainer container;
    private final Function<Integer, Text> titleSupplier;
    private final CreateElement supplyElementFunction;

    /**
     * Creates a new {@link Page} with a backing inventory defined by the given
     * view builder and layout template.
     */
    private Page(View.Builder view,
                 Layout layout,
                 PluginContainer container,
                 Function<Integer, Text> titleSupplier,
                 CreateElement supplyElement) {
        this.view = view;
        this.layout = layout;
        this.container = container;
        this.titleSupplier = titleSupplier;
        this.supplyElementFunction = supplyElement;
    }

    /**
     * Creates a new {@link Page} using the given layout as the layout and
     * archetype for creating the backing {@link View}s.
     *
     * @see Page.Builder methods
     */
    public static Page of(InventoryArchetype archetype, Layout layout, PluginContainer container) {
        return builder().archetype(archetype).layout(layout).build(container);
    }

    /**
     * Defines this page to contain the given elements. The number of pages is
     * expanded as need be to fill the size of the elements.
     *
     * @see Layout.Builder#page(Collection)
     */
    public Page define(List<Element> elements) {
        views.clear();
        int capacity = layout.getDimension().getRows() * layout.getDimension().getColumns() - layout.getElements().size();
        int pages = elements.isEmpty() ? 1 : (elements.size() - 1) / capacity + 1;
        for (int i = 1; i <= pages; i++) {
            views.add(view.property(InventoryTitle.of(titleSupplier.apply(i)))
                    .build(container)
                    .define(Layout.builder()
                    .from(layout)
                    .replace(FIRST, supplyElementFunction.createElement(this, FIRST, i, 1))
                    .replace(LAST, supplyElementFunction.createElement(this, LAST, i, pages))
                    .replace(NEXT, supplyElementFunction.createElement(this, NEXT, i, i == pages ? i : i + 1))
                    .replace(PREVIOUS, supplyElementFunction.createElement(this, PREVIOUS, i, i == 1 ? i : i - 1))
                    .replace(CURRENT, supplyElementFunction.createElement(this, CURRENT, i, i))
                    .page(elements.subList((i - 1) * capacity, i == pages ? elements.size() : i * capacity))
                    .build()));
        }
        return this;
    }

    /**
     * Opens the given page for the player. Pages are indexed starting at 1. If
     * the index is out of bounds, the closest valid page will be opened.
     */
    public void open(Player player, int page) {
        views.get((page > 1 ? Math.min(page, views.size()) - 1 : 0)).open(player);
    }

    /**
     * Opens the first page for the player.
     *
     * @see View#open(Player)
     */
    @Override
    public void open(Player player) {
        views.get(0).open(player);
    }

    /**
     * Creates a new builder for creating {@link Page}s.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Displayable.Builder {

        private View.Builder view = View.builder();
        private Layout layout;
        private Function<Integer, Text> titleSupplier = page -> Text.of("Page " + page);

        private CreateElement supplyElement = (page, element, current, target) -> {
            String name = "";
            if (element == FIRST) {
                name = "First Page";
            } else if (element == LAST) {
                name = "Last Page";
            } else if (element == NEXT){
                name = "Next Page";
            } else if (element == PREVIOUS) {
                name = "Previous Page";
            } else if (element == CURRENT) {
                name = "Current Page";
            }

            ItemStack item = ItemStack.builder()
                    .itemType(current == target ? ItemTypes.MAP : ItemTypes.PAPER)
                    .add(Keys.DISPLAY_NAME, Text.of(name, " (", target, ")"))
                    .quantity(target)
                    .build();
            return current == target ? Element.of(item) : Element.of(item, a -> page.open(a.getPlayer(), target));
        };

        /**
         * Sets the archetype used for the backing {@link View}s. The dimension
         * of the archetype is expected to match the dimension of the layout.
         */
        @Override
        public Builder archetype(InventoryArchetype archetype) {
            view.archetype(archetype);
            return this;
        }

        /**
         * Adds a property used for the backing {@link View}s.
         */
        @Override
        public Builder property(InventoryProperty property) {
            view.property(property);
            return this;
        }

        /**
         * Sets the close action that is accepted when this page is closed. The
         * {@link InteractInventoryEvent.Close} event is only fired when the
         * inventory is closed completely, not when changing views.
         */
        @Override
        public Builder onClose(Consumer<Action<InteractInventoryEvent.Close>> action) {
            view.onClose(action);
            return this;
        }

        /**
         * Sets the layout used for the template of this view. It is expected
         * that the layout contains empty slots. The dimension of the layout is
         * expected to match the dimension of the archetype.
         */
        public Builder layout(Layout layout) {
            this.layout = layout;
            return this;
        }

        public Builder supplyTitle(Function<Integer, Text> titleSupplier) {
            this.titleSupplier = titleSupplier;
            return this;
        }

        public Builder supplyElement(CreateElement supplyFunction) {
            this.supplyElement = supplyFunction;
            return this;
        }

        /**
         * @return the created page
         */
        @Override
        public Page build(PluginContainer container) {
            Preconditions.checkState(layout != null, "layout");
            return new Page(view, layout, container, titleSupplier, supplyElement);
        }

    }

}