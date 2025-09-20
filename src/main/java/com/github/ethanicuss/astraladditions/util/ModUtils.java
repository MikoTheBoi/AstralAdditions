package com.github.ethanicuss.astraladditions.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.include.com.google.gson.JsonObject;

import java.io.StringReader;
import java.util.List;

public class ModUtils {
    public static <T extends ParticleEffect> void spawnForcedParticles(ServerWorld world, T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            world.spawnParticles(player, particle, true, x, y, z, count, deltaX, deltaY, deltaZ, speed);
        }
    }

    public static <T extends ParticleEffect> void playSound(ServerWorld world, double x, double y, double z, SoundEvent sound, SoundCategory category, float vol, float pitch, boolean falloff) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            player.world.playSound(x, y, z, sound, category, vol, pitch, falloff);
        }
    }

    //Currently the pulling method sucks in all entities and doesn't return anything. Might change in the future. You can look at EnderBallEntity code to see how to use this.
    public static void pullPlayer(Entity entityActor, World world, double strength, double vStrength, double entityPosX, double entityPosZ, double rangeX1, double rangeY1, double rangeZ1, double rangeX2, double rangeY2, double rangeZ2){

        //PlayerEntity p = this.world.getClosestPlayer(this, 32);
        List<Entity> pl = world.getOtherEntities(entityActor, new Box(rangeX1, rangeY1, rangeZ1, rangeX2, rangeY2, rangeZ2));
        for (Entity p : pl) {
            if (p instanceof LivingEntity){
                int strMult = 1;
                if (!(p instanceof PlayerEntity)) {
                    strMult *= 2;
                }
                double xdiff = entityPosX - p.getX();
                double zdiff = entityPosZ - p.getZ();
                double dist = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(zdiff, 2));
                if (dist < 10) {
                    if (xdiff == 0) {
                        xdiff = 0.01;
                    }
                    if (zdiff == 0) {
                        zdiff = 0.01;
                    }
                    double angleX = Math.atan(Math.abs(zdiff) / xdiff);
                    double angleZ = Math.atan(Math.abs(xdiff) / zdiff);
                    double cosX = Math.cos(angleX);
                    double cosZ = Math.cos(angleZ);
                    if (cosX == 0) {
                        cosX = 0.01;
                    }
                    if (cosZ == 0) {
                        cosZ = 0.01;
                    }
                    dist = -dist + 10;
                    p.addVelocity(dist * cosX * strength * strMult * (Math.abs(angleX) / angleX), dist * vStrength * strMult, dist * cosZ * strength * strMult * (Math.abs(angleZ) / angleZ));
                }
            }
        }
    }
}

