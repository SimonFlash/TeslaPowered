package com.mcsimonflash.sponge.teslalibs.animation;

@Deprecated
public class Frame<T> {

    private T frame;
    private int length;

    /**
     * @see #of(T, int)
     */
    private Frame(T frame, int length) {
        this.frame = frame;
        this.length = length;
    }

    /**
     * Creates a new frame with the given frame and length in milliseconds
     */
    public static <T> Frame<T> of(T frame, int length) {
        return new Frame<>(frame, length);
    }

    /**
     * @return the frame
     */
    public T getFrame() {
        return frame;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

}