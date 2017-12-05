package com.mcsimonflash.sponge.teslalibs.command;

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;

public class Settings {

    @Nullable CommandElement[] arguments;
    @Nullable Class<? extends Command>[] children;
    @Nullable String[] aliases;
    @Nullable String permission;
    @Nullable Text description;

    private Settings() {}

    public static Settings create() {
        return new Settings();
    }

    public Settings arguments(CommandElement... arguments) {
        this.arguments = arguments;
        return this;
    }

    @SafeVarargs
    public final Settings children(Class<? extends Command>... children) {
        this.children = children;
        return this;
    }

    public Settings aliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public Settings permission(String permission) {
        this.permission = permission;
        return this;
    }

    public Settings description(Text description) {
        this.description = description;
        return this;
    }

}