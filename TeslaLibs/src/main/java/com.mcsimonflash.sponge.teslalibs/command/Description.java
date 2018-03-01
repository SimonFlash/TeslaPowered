package com.mcsimonflash.sponge.teslalibs.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    /**
     * The description of the command. This value is deserialized into a
     * {@link org.spongepowered.api.text.Text} using the legacy (&) format.
     *
     * @see Command.Settings#description(org.spongepowered.api.text.Text)
     */
    String value();

}