package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.spongepowered.api.item.inventory.property.InventoryDimension;

import java.util.Collection;
import java.util.Map;

public class Layout {

    private final ImmutableMap<Integer, Element> elements;
    private final InventoryDimension dimension;

    /**
     * Creates a new {@link Layout} representing a map of elements to set
     * indices within an inventory.
     */
    private Layout(ImmutableMap<Integer, Element> elements, InventoryDimension dimension) {
        this.elements = elements;
        this.dimension = dimension;
    }

    /**
     * Retrieves an element at a given index in the layout. If the index is not
     * present in the map of elements, {@link Element#EMPTY} is returned.
     */
    public Element getElement(int index) {
        return elements.getOrDefault(index, Element.EMPTY);
    }

    /**
     * @return the immutable map of elements
     */
    public ImmutableMap<Integer, Element> getElements() {
        return elements;
    }

    /**
     * @return the layout dimension
     */
    public InventoryDimension getDimension() {
        return dimension;
    }

    /**
     * Creates a new builder for creating a {@link Layout}.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final InventoryDimension DEFAULT = InventoryDimension.of(9, 6);

        private Map<Integer, Element> elements = Maps.newHashMap();
        private InventoryDimension dimension = DEFAULT;
        private int rows = 6;
        private int columns = 9;
        private int capacity = 54;

        /**
         * Sets the dimension to be the given {@link InventoryDimension}. The
         * dimension is used for advanced set methods, ensuring {@link Element}s
         * are in the bounds of the layout, and ensuring a layout of the proper
         * dimension is being applied to an {@link View}.
         *
         * By default, the dimension is a double chest (6 by 9).
         */
        public Builder dimension(InventoryDimension dimension) {
            this.dimension = dimension;
            rows = dimension.getRows();
            columns = dimension.getColumns();
            capacity = rows * columns;
            return this;
        }

        /**
         * Sets the element at the given index. The index must be at least 0 and
         * less than the capacity or an {@link IndexOutOfBoundsException} will
         * be thrown.
         */
        public Builder set(Element element, int index) {
            Preconditions.checkElementIndex(index, capacity);
            elements.put(index, element);
            return this;
        }

        /**
         * Sets the element at the given indices.
         */
        public Builder set(Element element, int... indices) {
            for (int i : indices) {
                set(element, i);
            }
            return this;
        }

        /**
         * Sets all of the elements defined in the map to their assigned index.
         */
        public Builder setAll(Map<Integer, Element> elements) {
            for (Map.Entry<Integer, Element> entry : elements.entrySet()) {
                set(entry.getValue(), entry.getKey());
            }
            return this;
        }

        /**
         * Sets the element to all of the indices in the given row.
         */
        public Builder row(Element element, int index) {
            for (int i = columns * index; i < columns * (index + 1); i++) {
                set(element, i);
            }
            return this;
        }

        /**
         * Sets the element to all of the indices in the given column.
         */
        public Builder column(Element element, int index) {
            for (int i = index; i < capacity; i += columns) {
                set(element, i);
            }
            return this;
        }

        /**
         * Sets the element to the index defined by {code capacity / 2}.
         */
        public Builder center(Element element) {
            return set(element, capacity / 2);
        }

        /**
         * Sets the element to all indices along the border of the layout.
         */
        public Builder border(Element element) {
            for (int i = 0; i <= columns; i++) {
                set(element, i);
                set(element, capacity - i);
            }
            for (int i = 2; i < rows - 1; i++) {
                int j = i * columns;
                set(element, j - 1);
                set(element, j);
            }
            return this;
        }

        /**
         * Creates a checkerboard from the given even and odd elements.
         */
        public Builder checker(Element even, Element odd) {
            for (int i = 0; i < capacity; i++) {
                set(i % 2 == 0 ? even : odd, i);
            }
            return this;
        }

        /**
         * Fills any unregistered slots with the given element.
         */
        public Builder fill(Element element) {
            for (int i = 0; i < capacity; i++) {
                if (!elements.containsKey(i)) {
                    set(element, i);
                }
            }
            return this;
        }

        /**
         * Pages the given elements through the layout in the order of the set
         * index and natural ordering of the collection. Indexes that are
         * already registered will be skipped.
         */
        public Builder page(Collection<Element> elements) {
            int index = 0;
            for (Element element : elements) {
                while (this.elements.containsKey(index)) {
                    index++;
                }
                set(element, index++);
            }
            return this;
        }

        /**
         * Replaces instances of the initial element with the replacement. This
         * method is primarily for layout templates as used with {@link Page}s.
         */
        public Builder replace(Element initial, Element replacement) {
            for (Map.Entry<Integer, Element> entry : elements.entrySet()) {
                if (entry.getValue() == initial) {
                    entry.setValue(replacement);
                }
            }
            return this;
        }

        /**
         * @return the created layout
         */
        public Layout build() {
            return new Layout(ImmutableMap.copyOf(elements), dimension);
        }

    }

}