package com.mcsimonflash.sponge.teslalibs.inventory;

/**
 * Created by Frani on 15/01/2019.
 */
@FunctionalInterface
public interface CreateElement {

    /**
     * Used to replace a {@link Page}'s page button depending of
     * it's current and target page. Take a look at the default {@link Page.Builder}
     * implementation of this function.
     *
     * @param page The page holding this element
     * @param element The element that is going to be replaced by this
     * @param currentPage The current page index
     * @param targetPage The target page index
     * @return a new {@link Element} with customized actions
     */
    Element createElement(Page page, Element element, int currentPage, int targetPage);

}
