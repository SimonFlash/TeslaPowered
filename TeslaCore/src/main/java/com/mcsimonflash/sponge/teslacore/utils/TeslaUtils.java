package com.mcsimonflash.sponge.teslacore.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class TeslaUtils {

    public static Optional<URL> parseURL(String url) {
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException ignored) {
            return Optional.empty();
        }
    }

}