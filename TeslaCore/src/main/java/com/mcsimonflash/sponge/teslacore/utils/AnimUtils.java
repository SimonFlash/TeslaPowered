package com.mcsimonflash.sponge.teslacore.utils;

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
        return segments == 0 ? 0 : TAU / segments;
    }

    public static float sin(float radians) {
        return (float) Math.sin(radians);
    }

    public static float cos(float radians) {
        return (float) Math.cos(radians);
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
        return Quaternionf.fromAngleRadAxis(radians, axis).rotate(radius, 0, 0);
        /*Vector3f base = circle(radians, radius);
        float x = base.getX(), y = base.getY(), z = base.getZ(), u = axis.getX(), v = axis.getY(), w = axis.getZ();
        double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(radians)) + x*Math.cos(radians) + (-w*y + v*z)*Math.sin(radians);
        double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(radians)) + y*Math.cos(radians) + (w*x - u*z)*Math.sin(radians);
        double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(radians)) + z*Math.cos(radians) + (-v*x + u*y)*Math.sin(radians);
        return new Vector3f(xPrime, yPrime, zPrime);
        /*circle(radians, radius).rot

        float half = radians / 2, sin = sin(half), cos = cos(half);
        Quaternionf rot = new Quaternionf(cos, axis.getX() * sin, axis.getY() * sin, axis.getZ() * sin);
        Vector3f circle = circle(radians, radius);
        rot = rot.mul(0, circle.getX(), circle.getY(), circle.getZ()).mul(rot.conjugate());
        return new Vector3f(rot.getX(), rot.getY(), rot.getZ());

        /*float sin = sin(radians), cos = cos(radians), s = 1F / (axis.getX() * axis.getX() + axis.getZ() * axis.getZ());
        Vector3f axis2 = new Vector3f(axis.getZ() * cos * s, 0F, -axis.getX() * cos * s);
        return axis2.add(axis.cross(axis2.getX() * sin, 0F, axis2.getZ() * sin)).mul(radius);*/
    }

    private static Vector3f rotX(Vector3f v, double cos, double sin) {
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return new Vector3f(v.getX(), y, z);
    }

    private static Vector3f rotY(Vector3f v, double cos, double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return new Vector3f(x, v.getY(), z);
    }

    private static Vector3f rotZ(Vector3f v, double cos, double sin) {
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return new Vector3f(x, y, v.getZ());
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