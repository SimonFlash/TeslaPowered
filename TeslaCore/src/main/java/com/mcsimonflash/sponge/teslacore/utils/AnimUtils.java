package com.mcsimonflash.sponge.teslacore.utils;

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

    public static void spawn(Location<World> location, ParticleEffect particle, Vector3f offset) {
        location.getExtent().spawnParticles(particle, location.getPosition().add(offset.getX(), offset.getY(), offset.getZ()));
    }

    public static ParticleEffect particle(Color color) {
        return ParticleEffect.builder()
                .type(ParticleTypes.REDSTONE_DUST)
                .option(ParticleOptions.COLOR, color)
                .build();
    }

    public static Color rainbow(float radians) {
        return Color.ofRgb((int) wave(radians, shift(2), 127.5F, 127.5F),
                (int) wave(radians, shift(4), 127.5F, 127.5F),
                (int) wave(radians, 0F, 127.5F, 127.5F));
    }

    public static final float TAU = 2F * (float) Math.PI;

    public static float shift(int segments) {
        return segments != 0 ? TAU / segments : 0;
    }

    public static float sin(float radians) {
        return TrigMath.sin(radians);
    }

    public static float cos(float radians) {
        return TrigMath.cos(radians);
    }

    public static float wave(float radians, float shift, float center, float amplitude) {
        return sin(radians + shift) * amplitude + center;
    }

    public static float[] shift(float radians, float shift) {
        float[] shifts = new float[shift == 0 ? 0 : (int) Math.abs(TAU / shift)];
        if (shifts.length > 0) {
            shifts[0] = radians;
            for (int i = 1; i < shifts.length; i++) {
                shifts[i] = shifts[i - 1] + shift;
            }
        }
        return shifts;
    }

    public static Vector3f circle(float radians, float radius) {
        return new Vector3f(cos(radians) * radius, 0F, sin(radians) * radius);
    }

    public static Vector3f circle(float radians, float radius, Vector3f axis) {
        return Quaternionf.fromAngleRadAxis(radians, axis).rotate(Vector3f.from(-axis.getZ(), 0F, axis.getX())).mul(radius);
    }

    public static Vector3f[] parametric(float radians, int segments) {
        Vector3f[] vecs = new Vector3f[Math.abs(segments)];
        if (vecs.length > 0) {
            float sin = sin(radians);
            float[] shifts = shift(radians + radians / segments, TAU / segments);
            for (int i = 0; i < vecs.length; i++) {
                vecs[i] = new Vector3f(cos(shifts[i]), sin, sin(shifts[i]));
            }
        }
        return vecs;
    }

}