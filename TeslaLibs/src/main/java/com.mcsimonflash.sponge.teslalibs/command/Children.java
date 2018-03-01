package com.mcsimonflash.sponge.teslalibs.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Children {

    /**
     * The child command classes for the command.
     *
     * @see Command.Settings#children(Class[])
     */
    Class<? extends Command>[] value();

}