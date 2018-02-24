package com.mcsimonflash.sponge.teslalibs.command;

import com.google.common.collect.ImmutableList;
import com.mcsimonflash.sponge.teslalibs.command.arguments.Arguments;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public abstract class Command implements CommandExecutor {

    private final ImmutableList<CommandElement> arguments;
    private final ImmutableList<Class<? extends Command>> children;
    private final ImmutableList<String> aliases;
    private final Optional<String> permission;
    private final Optional<Text> description;
    private final CommandSpec spec;

    protected Command(CommandService service, Settings settings) {
        arguments = settings.arguments != null ? ImmutableList.copyOf(settings.arguments) : ImmutableList.of();
        children = settings.children != null ? ImmutableList.copyOf(settings.children) : getClass().isAnnotationPresent(Children.class) ? ImmutableList.copyOf(getClass().getAnnotation(Children.class).value()) : ImmutableList.of();
        aliases = settings.aliases != null ? ImmutableList.copyOf(settings.aliases) : getClass().isAnnotationPresent(Aliases.class) ? ImmutableList.copyOf(getClass().getAnnotation(Aliases.class).value()) : ImmutableList.of();
        permission = settings.permission != null ? Optional.of(settings.permission) : getClass().isAnnotationPresent(Permission.class) ? Optional.of(getClass().getAnnotation(Permission.class).value()) : Optional.empty();
        description = settings.description != null ? Optional.of(settings.description) : getClass().isAnnotationPresent(Description.class) ? Optional.of(Text.of(getClass().getAnnotation(Description.class).value())) : Optional.empty();
        CommandSpec.Builder builder = CommandSpec.builder().executor(this);
        if (!arguments.isEmpty()) {
            builder.arguments(arguments.size() == 1 ? arguments.get(0) : Arguments.sequence(settings.arguments));
        }
        for (Class<? extends Command> child : children) {
            Command command = service.getInstance(child);
            builder.child(command.spec, command.aliases);
        }
        permission.ifPresent(builder::permission);
        description.ifPresent(builder::description);
        this.spec = builder.build();
    }

    public ImmutableList<CommandElement> getArguments() {
        return arguments;
    }

    public ImmutableList<Class<? extends Command>> getChildren() {
        return children;
    }

    public ImmutableList<String> getAliases() {
        return aliases;
    }

    public Optional<String> getPermission() {
        return permission;
    }

    public Optional<Text> getDescription() {
        return description;
    }

    public CommandSpec getSpec() {
        return spec;
    }

}