package com.mcsimonflash.sponge.teslalibs.message;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageService {

    private LoadingCache<Locale, ResourceBundle> cache;

    /**
     * @see #of(Path, String)
     */
    private MessageService(ClassLoader loader, String name) {
        cache = Caffeine.newBuilder().build(key -> ResourceBundle.getBundle(name, key, loader));
        cache.invalidateAll();
    }

    /**
     * Creates a new MessageService for the given class loader and base name.
     *
     * @param loader the classloader
     * @param name the base name of the bundle
     */
    public static MessageService of(ClassLoader loader, String name) {
        return new MessageService(loader, name);
    }

    /**
     * Creates a new MessageService for the given directory and base name. This
     * method creates an {@link URLClassLoader} from the directory.
     *
     * @param dir the directory of properties files
     * @param name the base name of the bundle
     * @return the new MessageService
     * @throws MalformedURLException if the URL could not be created
     */
    public static MessageService of(Path dir, String name) throws MalformedURLException {
        return new MessageService(new URLClassLoader(new URL[] {dir.toUri().toURL()}), name);
    }

    /**
     * Creates a new Formatter for the message retrieved from the bundle in the
     * given locale. A message will be attempted
     *
     * @param key the message key
     * @return a new Formatter for the message
     */
    public Formatter get(String key, Locale locale) {
        ResourceBundle bundle = cache.get(locale);
        return new Formatter(bundle != null && bundle.containsKey(key) ? bundle.getString(key) : key);
    }

    /**
     * Reloads the MessageService, invalidating any cached ResourceBundles.
     */
    public void reload() {
        cache.invalidateAll();
    }

    public static class Formatter {

        private String template;

        /**
         * Creates a new Formatter with the given template.
         *
         * @param template the template
         */
        public Formatter(String template) {
            this.template = template;
        }

        /**
         * Formats the template using the given argument. All occurrences of
         * '<name>' will be replaced with the string value of the argument.
         *
         * @param name the name of the argument
         * @param arg the argument
         * @return this formatter
         */
        public Formatter arg(String name, Object arg) {
            template = template.replace("<" + name + ">", String.valueOf(arg));
            return this;
        }

        /**
         * Formats the template with pairs of arguments created from the given
         * array. The first element in a pair is used as the name and the second
         * is used as the argument. Any excess arguments are ignored.
         *
         * @param args the array of argument pairs
         * @return this formatter
         */
        public Formatter args(Object... args) {
            for (int i = 1; i < args.length; i += 2) {
                arg(String.valueOf(args[i-1]), args[i]);
            }
            return this;
        }

        /**
         * @return the template
         */
        public String toString() {
            return template;
        }

        /**
         * @return the deserialized template
         */
        public Text toText() {
            return TextSerializers.FORMATTING_CODE.deserialize(template);
        }

    }

}