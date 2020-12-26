package com.mcsimonflash.sponge.teslalibs.message;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Nullable;

public class MessageService {

    private static final Control CONTROL = new Control();
    private final ClassLoader loader;
    private final String name;
 

    /**
     * @see MessageService#of(ClassLoader, String)
     */
    private MessageService(ClassLoader loader, String name) {
        this.loader = loader;
        this.name = name;
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
     * Returns the {@link ResourceBundle} for the given {@link Locale} or throws
     * a {@link java.util.MissingResourceException} if no bundle could be found.
     *
     * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
     */

    public ResourceBundle getBundle(Locale locale)  {
    	  return ResourceBundle.getBundle(name, locale, loader, CONTROL);
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
        ResourceBundle.clearCache(loader);
    }
    
    /**
     * Custom {@link ResourceBundle.Control} implementation supporting UTF-8
     * properties files.
     */
    private static final class Control extends ResourceBundle.Control {

        @Override
        @Nullable
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            URL url = loader.getResource(toResourceName(toBundleName(baseName, locale), format));
            if (url != null) {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(!reload);
                try (InputStream stream = connection.getInputStream()) {
                    return new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                }
            }
            return null;
        }

    }

}

