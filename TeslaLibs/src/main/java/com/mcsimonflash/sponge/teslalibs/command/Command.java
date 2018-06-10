package com.mcsimonflash.sponge.teslalibs.command;

import com.google.common.collect.*;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.teslalibs.message.Message;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.*;
import org.spongepowered.api.text.Text;

import java.util.*;

public abstract class Command implements CommandExecutor {

    private final CommandSpec spec;
    private final ImmutableList<Command> children;
    private final ImmutableList<String> aliases;
    private final Optional<String> permission;
    private final Optional<Text> description;
    private final Text usage;

    @Deprecated //v1.2.0
    protected Command(CommandService service, Settings settings) {
        this(settings.setService(service));
    }

    protected Command(Settings settings) {
        settings.setAnnotations(getClass());
        children = ImmutableList.copyOf(settings.children);
        aliases = ImmutableList.copyOf(settings.aliases);
        permission = Optional.ofNullable(settings.permission);
        description = Optional.ofNullable(settings.description);
        CommandSpec.Builder builder = CommandSpec.builder().executor(this);
        if (!settings.elements.isEmpty()) {
            builder.arguments(settings.elements.size() == 1 ? settings.elements.get(0) : Arguments.sequence(settings.elements.toArray(new CommandElement[0])));
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
    @Deprecated
    protected static Settings settings() {
        return new Settings(null);
    }

    protected static class Settings {

        private CommandService service;
        private final List<Command> children = Lists.newArrayList();
        private final List<String> aliases = Lists.newArrayList();
        private final List<CommandElement> elements = Lists.newArrayList();
        private String permission;
        private Text description;
        private Text usage;

        protected Settings(CommandService service) {
            this.service = service;
        }

        /**
         * Creates a new {@link Settings} instance with the given service.
         */
        public static Settings of(CommandService service) {
            return new Settings(service);
        }

        /**
         * Sets the arguments used for the representing {@link Command}. If
         * multiple {@link CommandElement}s are present, they are reduced to a
         * single element using {@link Arguments#sequence(CommandElement...)}.
         */
        public Settings elements(CommandElement... elements) {
            Collections.addAll(this.elements, elements);
            return this;
        }

        /**
         * Adds children to the representing {@link Command}.
         */
        public final Settings children(Command... children) {
            Collections.addAll(this.children, children);
            return this;
        }

        /**
         * Adds children to the representing {@link Command}. The class instance
         * is retrieved from the {@link CommandService} and added to the
         * {@link CommandSpec} with their given aliases.
         */
        @SafeVarargs
        public final Settings children(Class<? extends Command>... children) {
            Collections.addAll(this.children, Arrays.stream(children).map(service::getInstance).toArray(Command[]::new));
            return this;
        }

        /**
         * Adds aliases the representing {@link Command}. Aliases should be
         * lowercase without spaces and in order of priority.
         */
        public Settings aliases(String... aliases) {
            Collections.addAll(this.aliases, aliases);
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

        @Deprecated private boolean deprecated = false;

        @Deprecated
        private Settings setService(CommandService service) {
            this.service = service;
            deprecated = true;
            return this;
        }

        @Deprecated
        private void setAnnotations(Class<? extends Command> clazz) {
            if (deprecated) {
                Optional.ofNullable(clazz.getAnnotation(Aliases.class)).ifPresent(a -> aliases(a.value()));
                Optional.ofNullable(clazz.getAnnotation(Children.class)).ifPresent(c -> children(c.value()));
                Optional.ofNullable(clazz.getAnnotation(Description.class)).ifPresent(d -> description(Message.toText(d.value())));
                Optional.ofNullable(clazz.getAnnotation(Permission.class)).ifPresent(p -> permission(p.value()));
            }
        }

    }

}