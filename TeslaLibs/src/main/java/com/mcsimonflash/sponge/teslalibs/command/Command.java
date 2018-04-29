package com.mcsimonflash.sponge.teslalibs.command;

import com.google.common.collect.ImmutableList;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.teslalibs.message.Message;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

public abstract class Command implements CommandExecutor {

    private final CommandSpec spec;
    private final ImmutableList<? extends Command> children;
    private final ImmutableList<String> aliases;
    private final Optional<String> permission;
    private final Optional<Text> description;
    private final Text usage;

    protected Command(CommandService service, Settings settings) {
        settings.children = settings.children != null ? settings.children : Optional.ofNullable(getClass().getAnnotation(Children.class)).map(Children::value).orElse(null);
        settings.aliases = settings.aliases != null ? settings.aliases : Optional.ofNullable(getClass().getAnnotation(Aliases.class)).map(Aliases::value).orElse(null);
        children = settings.children != null ? Arrays.stream(settings.children).map(service::getInstance).collect(ImmutableList.toImmutableList()) : ImmutableList.of();
        aliases = settings.aliases != null ? ImmutableList.copyOf(settings.aliases) : ImmutableList.of();
        permission = settings.permission != null ? Optional.of(settings.permission) : Optional.ofNullable(getClass().getAnnotation(Permission.class)).map(Permission::value);
        description = settings.description != null ? Optional.of(settings.description) : Optional.ofNullable(getClass().getAnnotation(Description.class)).map(d -> Message.of(d.value()).toText());
        CommandSpec.Builder builder = CommandSpec.builder().executor(this);
        if (settings.arguments != null) {
            builder.arguments(settings.arguments.length == 1 ? settings.arguments[0] : Arguments.sequence(settings.arguments));
        }
        children.forEach(c -> builder.child(c.getSpec(), c.getAliases()));
        permission.ifPresent(builder::permission);
        description.ifPresent(builder::description);
        spec = builder.build();
        usage = settings.usage != null ? settings.usage : spec.getUsage(Sponge.getServer().getConsole());
    }

    /**
     * @return the CommandSpec
     */
    public CommandSpec getSpec() {
        return spec;
    }

    /**
     * @return the list of children Commands
     */
    public ImmutableList<? extends Command> getChildren() {
        return children;
    }

    /**
     * @return the list of aliases
     */
    public ImmutableList<String> getAliases() {
        return aliases;
    }

    /**
     * @return the optional permission
     */
    public Optional<String> getPermission() {
        return permission;
    }

    /**
     * @return the optional description
     */
    public Optional<Text> getDescription() {
        return description;
    }

    /**
     * @return the usage
     */
    public Text getUsage() {
        return usage;
    }

    /**
     * @return a new Settings instance
     */
    protected static Settings settings() {
        return new Settings();
    }

    protected static class Settings {

        @Nullable CommandElement[] arguments;
        @Nullable Class<? extends Command>[] children;
        @Nullable String[] aliases;
        @Nullable String permission;
        @Nullable Text description;
        @Nullable Text usage;

        /**
         * Sets the arguments used for the representing {@link Command}. If
         * multiple {@link CommandElement}s are present, they are reduced to a
         * single element using {@link Arguments#sequence(CommandElement...)}.
         */
        public Settings arguments(CommandElement... arguments) {
            this.arguments = arguments;
            return this;
        }

        /**
         * Sets the child classes for the representing {@link Command}. The
         * class instance is retrieved from the {@link CommandService} and added
         * to the {@link CommandSpec} with their given aliases.
         */
        @SafeVarargs
        public final Settings children(Class<? extends Command>... children) {
            this.children = children;
            return this;
        }

        /**
         * Sets the aliases used for the representing {@link Command}. Aliases
         * should lowercase without spaces and in order of priority. The first
         * alias that could be registered is considered the primary alias.
         */
        public Settings aliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        /**
         * Sets the permission used for the representing {@link Command}. This
         * is equivalent to {@link CommandSpec.Builder#permission(String)}.
         */
        public Settings permission(String permission) {
            this.permission = permission;
            return this;
        }

        /**
         * Sets the description used for the representing {@link Command}. This
         * is equivalent to {@link CommandSpec.Builder#description(Text)}.
         */
        public Settings description(Text description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the usage used for the representing {@link Command}. If not
         * defined, the usage retrieved from the {@link CommandSpec} for the
         * server console is used instead.
         */
        public Settings usage(Text usage) {
            this.usage = usage;
            return this;
        }

    }

}