package com.mcsimonflash.sponge.teslalibs.message;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Message {

    private String message;

    /**
     * @see #of(String)
     */
    private Message(String message) {
        this.message = message;
    }

    /**
     * Creates a new {@link Message} with the given message.
     */
    public static Message of(String message) {
        return new Message(message);
    }

    /**
     * Applies an argument to the message with the given name. All occurrences
     * of the '<name>' will be replaced with the string value of the argument.
     */
    public Message arg(String name, Object arg) {
        message = message.replace("<" + name + ">", String.valueOf(arg));
        return this;
    }

    /**
     * Applies multiple arguments to the message in a varargs form. Elements in
     * the array with even indexes are considered to be the argument name, while
     * those with odd numbers are the argument itself.
     */
    public Message args(Object... args) {
        for (int i = 1; i < args.length; i += 2) {
            arg(String.valueOf(args[i-1]), args[i]);
        }
        return this;
    }

    /**
     * @return the message
     */
    public String toString() {
        return message;
    }

    /**
     * @return the deserialized message as a {@link Text}
     */
    public Text toText() {
        return TextSerializers.FORMATTING_CODE.deserialize(message);
    }

}