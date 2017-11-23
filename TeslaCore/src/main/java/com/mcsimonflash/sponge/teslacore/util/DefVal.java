package com.mcsimonflash.sponge.teslacore.util;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class DefVal<T> {

    private T def;
    @Nullable private T val;

    private DefVal(T def) {
        this.def = def;
    }

    public static <T> DefVal<T> of(T def) {
        return new DefVal<>(def);
    }

    public boolean isPresent() {
        return val != null;
    }

    public void ifPresent(Consumer<T> consumer) {
        if (val != null) {
            consumer.accept(val);
        }
    }

    public T get() {
        return val != null ? val : def;
    }

    public T getDef() {
        return def;
    }

    public void setDef(T def) {
        this.def = def;
    }

    @Nullable public T getVal() {
        return val;
    }

    public void setVal(@Nullable T val) {
        this.val = val;
    }

}