package com.mcsimonflash.sponge.teslalibs.animation;

import com.flowpowered.math.TrigMath;
import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.vector.Vector3f;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class AnimUtils {

    public static final float TAU = 2F * (float) Math.PI;

    /**
     * Spawns a particle at an offset of the given location.
     */
    public static void spawn(Location<World> location, ParticleEffect particle, Vector3f offset) {
        location.getExtent().spawnParticles(particle, location.getPosition().add(offset.getX(), offset.getY(), offset.getZ()));
    }

    /**
     * Creates a {@link ParticleEffect} of redstone dust with the given color.
     */
    public static ParticleEffect particle(Color color) {
        return ParticleEffect.builder()
                .type(ParticleTypes.REDSTONE_DUST)
                .option(ParticleOptions.COLOR, color)
                .build();
    }

    /**
     * Returns a {@link Color} representing a color in the rainbow for the given
     * radians. An input of 0 results in a red color, with increasing values
     * moving towards orange.
     */
    public static Color rainbow(float radians) {
        return Color.ofRgb((int) wave(radians, shift(2), 127.5F, 127.5F),
                (int) wave(radians, shift(4), 127.5F, 127.5F),
                (int) wave(radians, 0F, 127.5F, 127.5F));
    }

    /**
     * Returns a standard sine wave evaluated in it's general form.
     */
    public static float wave(float radians, float shift, float center, float amplitude) {
        return sin(radians + shift) * amplitude + center;
    }

    /**
     * Returns the sine of the given radians, obtained from a cached table.
     *
     * @see TrigMath#sin(double)
     */
    public static float sin(float radians) {
        return TrigMath.sin(radians % TAU);
    }

    /**
     * Returns the cosine of the given radians, obtained from a cached table.
     *
     * @see TrigMath#cos(double)
     */
    public static float cos(float radians) {
        return TrigMath.cos(radians % TAU);
    }

    /**
     * Returns the increase in radians diving a circle into the given number of
     * segments. A negative input returns a negative shift; 0 returns 0.
     */
    public static float shift(int segments) {
        return segments != 0 ? TAU / segments : 0;
    }

    /**
     * Returns an array containing all radians at or above the given value that
     * are multiple of the given shift and within Tau radians. An input of
     * (0, TAU/8) will return [0, TAU/8, 2TAU/8, 3TAU/8, ..., 7TAU/8].
     */
    public static float[] shift(float radians, float shift) {
        float[] shifts = new float[shift != 0 ? (int) Math.abs(TAU / shift) : 0];
        if (shifts.length > 0) {
            shifts[0] = radians;
            for (int i = 1; i < shifts.length; i++) {
                shifts[i] = shifts[i - 1] + shift;
            }
        }
        return shifts;
    }

    /**
     * Return a point along a circle in the xz plane for the given radians.
     */
    public static Vector3f circle(float radians) {
        return Vector3f.from(cos(radians), 0F, -sin(radians));
    }

    /**
     * Returns a point along a circle around a given axis for the given radians.
     * The axis is oriented such that the x' axis is on the xz plane and the z'
     * axis is in the positive y direction. For rotating about the y axis, it is
     * recommended to use {@link AnimUtils#circle(float)} instead.
     */
    public static Vector3f circle(float radians, Vector3f axis) {
        return Quaternionf.fromAngleRadAxis(radians, axis).rotate(axis.equals(Vector3f.UNIT_Y) ? Vector3f.UNIT_X : Vector3f.from(-axis.getZ(), 0F, axis.getX()).normalize());
    }

    /**
     * Returns an array containing all points along a parametric equation for
     * the given radians with the given number of segments. The height of the
     * returned points is the same.
     */
    public static Vector3f[] parametric(float radians, int segments) {
        Vector3f[] vecs = new Vector3f[Math.abs(segments)];
        if (vecs.length > 0) {
            float sin = sin(radians);
            float[] shifts = shift(radians + radians / segments, TAU / segments);
            for (int i = 0; i < vecs.length; i++) {
                vecs[i] = Vector3f.from(cos(shifts[i]), sin, sin(shifts[i]));
            }
        }
        return vecs;
    }

}