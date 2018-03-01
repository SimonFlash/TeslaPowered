package com.mcsimonflash.sponge.teslalibs.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    /**
     * The permission required for the command.
     *
     * @see Command.Settings#permission(String)
     */
    String value();

}