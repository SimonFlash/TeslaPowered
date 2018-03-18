package com.mcsimonflash.sponge.teslalibs.message;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageService {

    private final LoadingCache<Locale, ResourceBundle> cache;

    /**
     * @see MessageService#of(ClassLoader, String)
     */
    private MessageService(ClassLoader loader, String name) {
        cache = Caffeine.newBuilder().build(k -> ResourceBundle.getBundle(name, k, loader));
    }

    /**
     * Creates a new {@link MessageService} using the given {@link ClassLoader}
     * and the base name for a {@link ResourceBundle}. Caution should be used to
     * prevent conflictions from plugins using the same class loader.
     */
    public static MessageService of(ClassLoader loader, String name) {
        return new MessageService(loader, name);
    }

    /**
     * Creates a new {@link MessageService} with a {@link URLClassLoader} from
     * the given path.
     *
     * @throws MalformedURLException if the url could not be created
     * @see MessageService#of(ClassLoader, String)
     */
    public static MessageService of(Path path, String name) throws MalformedURLException {
        return of(new URLClassLoader(new URL[] {path.toUri().toURL()}), name);
    }

    /**
     * Creates a new {@link MessageService} with a {@link URLClassLoader} from
     * the url pointing to the assets folder of the given container.
     *
     * @see MessageService#of(ClassLoader, String)
     */
    public static MessageService of(PluginContainer container, String name) {
        return of(new URLClassLoader(new URL[] {Sponge.class.getClassLoader().getResource("assets/" + container.getId())}), name);
    }

    /**
     * Returns the {@link ResourceBundle} for the given {@link Locale}. This may
     * throw multiple unchecked exceptions as documented in the methods linked
     * below, most notably {@link java.util.MissingResourceException}.
     *
     * @see LoadingCache#get(Object)
     * @see ResourceBundle#getBundle(String, Locale)
     */
    public ResourceBundle getBundle(Locale locale) {
        return cache.get(locale);
    }

    /**
     * Creates a new {@link Message} from the string retrieved from the locale's
     * {@link ResourceBundle} and this service's prefix and suffix. If the
     * bundle does not contain the given key, the key itself is used.
     *
     * @see MessageService#getBundle(Locale)
     */
    public Message get(String key, Locale locale) {
        ResourceBundle bundle = getBundle(locale);
        return Message.of(bundle.containsKey(key) ? bundle.getString(key) : key);
    }

    /**
     * Reloads the cache, invalidating any stored {@link ResourceBundle}s.
     */
    public void reload() {
        cache.invalidateAll();
    }

}