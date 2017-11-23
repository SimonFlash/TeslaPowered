package com.mcsimonflash.sponge.teslalibs.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

public class Layout {

    private final ImmutableMap<Integer, Element> elements;

    /**
     * Creates a new layout
     *
     * @param elements the elements
     */
    private Layout(Map<Integer, Element> elements) {
        this.elements = ImmutableMap.copyOf(elements);
    }

    /**
     * Retrieves an element at a given index
     *
     * @param index the index correlating to the slotindex
     * @return the element at that index or an empty element
     */
    public Element getElement(Integer index) {
        return elements.getOrDefault(index, Element.EMPTY);
    }

    /**
     * Retrieves the map of elements. This map is immutable.
     *
     * @return the map of elements.
     */
    public ImmutableMap<Integer, Element> getElements() {
        return elements;
    }

    /**
     * Creates a new builder for building layouts.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for creating layouts
     */
    public static class Builder {

        private Map<Integer, Element> elements = Maps.newHashMap();
        private int width;
        private int height;
        private int size;

        private void checkSize() {
            Preconditions.checkArgument(size != 0, "Layout dimension must be defined.");
        }

        /**
         * Defines this layout to have the given dimension.
         *
         * @param width the width
         * @param height the height
         * @return this builder
         */
        public Builder dimension(int width, int height) {
            this.width = width;
            this.height = height;
            this.size = width * height;
            return this;
        }

        /**
         * Registers the given element at the given index.
         *
         * @param index the index in the inventory
         * @param element the element
         * @return this builder
         */
        public Builder slot(Element element, int index) {
            elements.put(index, element);
            return this;
        }

        /**
         * Registers the given element at the given indices.
         *
         * @param element the element
         * @param indices the indices in the inventory
         * @return this builder
         */
        public Builder slots(Element element, int... indices) {
            for (int i : indices) {
                slot(element, i);
            }
            return this;
        }

        /**
         * Registers all index/element entries in the given map.
         *
         * @param elements the map of elements
         * @return this builder
         */
        public Builder slots(Map<Integer, Element> elements) {
            this.elements.putAll(elements);
            return this;
        }

        /**
         * Registers the given element for every index in the given row.
         *
         * @param element the element
         * @param index the index of the row
         * @return this builder
         * @throws IllegalArgumentException if the dimension is undefined
         */
        public Builder row(Element element, int index) {
            checkSize();
            for (int i = width * index; i < width * (index + 1); i++) {
                slot(element, i);
            }
            return this;
        }

        /**
         * Registers the given element at each index in the given column.
         *
         * @param element the element
         * @param index the index of the column
         * @return this builder
         * @throws IllegalArgumentException if the dimension is undefined
         */
        public Builder column(Element element, int index) {
            checkSize();
            for (int i = index; i < size; i += width) {
                slot(element, i);
            }
            return this;
        }

        /**
         * Registers the given element at the center of this layout, defined by
         * {@code size / 2}.
         *
         * @param element the element
         * @return this builder
         * @throws IllegalArgumentException if the dimension is undefined
         */
        public Builder center(Element element) {
            checkSize();
            return slot(element, size / 2);
        }

        /**
         * Creates a border using the given element.
         *
         * @param element the element
         * @return this builder
         * @throws IllegalArgumentException if the dimension is undefined
         */
        public Builder border(Element element) {
            checkSize();
            for (int i = 0; i <= width; i++) {
                slot(element, i);
                slot(element, size - i);
            }
            for (int i = 2; i < height - 1; i++) {
                int j = i * width;
                slot(element, j - 1);
                slot(element, j);
            }
            return this;
        }

        /**
         * Creates a checkerboard from the given elements
         *
         * @param even the element at even slot indices
         * @param odd the element at odd slot indices
         * @return this builder
         * @throws IllegalArgumentException if the dimension is undefined
         */
        public Builder checker(Element even, Element odd) {
            checkSize();
            for (int i = 0; i < size; i++) {
                slot(i % 2 == 0 ? even : odd, i);
            }
            return this;
        }

        /**
         * Fills any unregistered slots with the given element
         *
         * @param element the element
         * @return this builder
         * @throws IllegalArgumentException if the dimension is undefined
         */
        public Builder fill(Element element) {
            checkSize();
            for (int i = 0; i < size; i++) {
                if (!elements.containsKey(i)) {
                    slot(element, i);
                }
            }
            return this;
        }

        /**
         * Pages the given elements through the layout in the order of the slot
         * index and natural ordering of the collection. Indexes that are
         * already registered will be skipped.
         *
         * @param elements the collection of elements
         * @return this builder
         */
        public Builder page(Collection<Element> elements) {
            int index = 0;
            for (Element element : elements) {
                while (this.elements.containsKey(index)) {
                    index++;
                }
                slot(element, index++);
            }
            return this;
        }

        /**
         * Replaces instances of the initial element with the replacement. This
         * method is primarily used with layout templates as in pages.
         *
         * @param initial the initial element
         * @param replacement the replacement element
         * @return this builder
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
         * Registers all elements contained in the given layout, replacing if
         * necessary.
         *
         * @param layout the layout
         * @return this builder
         */
        public Builder from(Layout layout) {
            return slots(layout.getElements());
        }

        /**
         * Clears all registered elements.
         *
         * @return this builder
         */
        public Builder reset() {
            elements.clear();
            return this;
        }

        /**
         * Builds the layout represented by this builder
         *
         * @return the newly created layout
         */
        public Layout build() {
            return new Layout(elements);
        }

    }

}