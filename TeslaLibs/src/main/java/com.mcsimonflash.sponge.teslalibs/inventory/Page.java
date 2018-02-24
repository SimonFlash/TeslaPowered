package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.collect.Lists;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.LinkedList;
import java.util.List;

public class Page {

    public static final Element FIRST = Element.of(ItemStack.empty());
    public static final Element LAST = Element.of(ItemStack.empty());
    public static final Element NEXT = Element.of(ItemStack.empty());
    public static final Element PREVIOUS = Element.of(ItemStack.empty());
    public static final Element CURRENT = Element.of(ItemStack.empty());

    private LinkedList<View> views = Lists.newLinkedList();
    private Layout template;
    private PluginContainer container;

    /**
     * @see #of(Layout, PluginContainer)
     */
    public Page(Layout template, PluginContainer container) {
        this.template = template;
        this.container = container;
    }

    /**
     * Creates a new Page with the given template for the given container.
     *
     * @param template the page template
     * @param container the container creating this page
     * @return the new page
     */
    public static Page of(Layout template, PluginContainer container) {
        return new Page(template, container);
    }

    /**
     * Defines this page to contain the given elements. The implementation
     * currently assumes that the layout contains 54 slots (double chest); this
     * will be changed in a future version.
     *
     * @param elements the list of elements
     * @return this page
     */
    public Page define(List<Element> elements) {
        views.clear();
        int pages = elements.size() != 0 ? elements.size() / (54 - template.getElements().size()) : 1;
        for (int i = 1; i <= pages; i++) {
            views.add(View.of(InventoryArchetypes.DOUBLE_CHEST, container));
        }
        for (int i = 1; i <= pages; i++) {
            views.get(i - 1).define(Layout.builder()
                    .from(template)
                    .replace(FIRST, Element.of(ItemStack.builder().itemType(i == 1 ? ItemTypes.MAP : ItemTypes.PAPER).quantity(1).add(Keys.DISPLAY_NAME, Text.of("First")).build(), i == 1 ? p -> {} : views.get(0)::open))
                    .replace(LAST, Element.of(ItemStack.builder().itemType(i == pages ? ItemTypes.MAP : ItemTypes.PAPER).quantity(pages).add(Keys.DISPLAY_NAME, Text.of("Last")).build(), i == pages ? p -> {} : views.get(pages - 1)::open))
                    .replace(NEXT, Element.of(ItemStack.builder().itemType(i == pages ? ItemTypes.MAP : ItemTypes.PAPER).quantity(i == pages ? i : i + 1).add(Keys.DISPLAY_NAME, Text.of("Next")).build(), i == pages ? p -> {} : views.get(i)::open))
                    .replace(PREVIOUS, Element.of(ItemStack.builder().itemType(i == 1 ? ItemTypes.MAP : ItemTypes.PAPER).quantity(i == 1 ? i : i - 1).add(Keys.DISPLAY_NAME, Text.of("Previous")).build(), i == 1 ? p -> {} : views.get(i - 2)::open))
                    .replace(CURRENT, Element.of(ItemStack.builder().itemType(ItemTypes.MAP).quantity(i).add(Keys.DISPLAY_NAME, Text.of("Current")).build()))
                    .page(elements.subList((i - 1) * (54 - template.getElements().size()), i == pages ? elements.size() : (i) * (54 - template.getElements().size())))
                    .build());
        }
        return this;
    }

    /**
     * Opens the first page for this player.
     *
     * @param player the player
     */
    public void open(Player player) {
        views.get(0).open(player);
    }

}