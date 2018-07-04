package com.mcsimonflash.sponge.teslalibs.animation;

import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Deprecated
public class Animator<T extends Animatable<U>, U> {

    private T handler;
    private List<Frame<U>> frames;
    private PluginContainer container;
    private Runnable completion;
    private Task runner;

    /**
     * @see #of(Animatable, List, PluginContainer)
     */
    private Animator(T handler, List<Frame<U>> frames, PluginContainer container) {
        this.handler = handler;
        this.frames = frames;
        this.container = container;
    }

    /**
     * Creates a new Animator with the given handler and frames for the given
     * container.
     *
     * @param handler the handler used to process frames
     * @param frames the animation frames
     * @param container the container creating this animator
     * @return the new animator
     */
    public static <T extends Animatable<U>, U> Animator<T, U> of(T handler, List<Frame<U>> frames, PluginContainer container) {
        return new Animator<>(handler, frames, container);
    }

    /**
     * Sets a runnable to execute when this animation stops naturally.
     *
     * @param completion the runnable
     */
    public void onCompletion(Runnable completion) {
        this.completion = completion;
    }

    /**
     * Starts the animation with the given delay and whether it should loop.
     *
     * @param delay the delay
     * @param loop true if the animation should loop, else false
     */
    public void start(int delay, boolean loop) {
        runner = Task.builder().execute(task -> run(0, loop)).delay(delay, TimeUnit.MILLISECONDS).submit(container);
    }

    /**
     * Runs an individual frame of the animation.
     *
     * @param index the frame index
     * @param loop whether the animation loops
     */
    private void run(int index, boolean loop) {
        Frame<U> frame = frames.get(index++);
        handler.nextFrame(frame.getFrame());
        int nextIndex = loop ? index % frames.size() : index;
        if (nextIndex < frames.size()) {
            runner = Task.builder().execute(task -> run(nextIndex, loop)).delay(frame.getLength(), TimeUnit.MILLISECONDS).submit(container);
        } else {
            if (completion != null) {
                completion.run();
            }
            stop();
        }
    }

    /**
     * Stops a running animation manually. This method will not fire a runnable
     * set by {@link #onCompletion(Runnable)}.
     */
    public void stop() {
        if (runner != null) {
            runner.cancel();
        }
    }

}