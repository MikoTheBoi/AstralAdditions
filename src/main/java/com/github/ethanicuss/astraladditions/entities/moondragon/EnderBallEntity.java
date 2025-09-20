package com.github.ethanicuss.astraladditions.entities.moondragon;

import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class EnderBallEntity extends ExplosiveProjectileEntity {

    private int timer = 20;
    private int despawntimer = 60;
    public EnderBallEntity(EntityType<? extends EnderBallEntity> entityType, World world) {
        super((EntityType<? extends ExplosiveProjectileEntity>)entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (timer == 20 && hitResult.getType() == HitResult.Type.BLOCK){
            setVelocity(0, 0, 0);
            timer--;
        }
    }



    @Override
    public void tick() {
        super.tick();

        if (despawntimer == 0 && timer == 20){
            timer--;
        }
        else {
            despawntimer--;
        }
        if (timer < 20) {
            timer--;
            ModUtils.pullPlayer(this, this.world, 0.01, 0, this.getX(), this.getZ(), this.getX()-16, this.getY()-32, this.getZ()-16, this.getX()+16, this.getY()+32, this.getZ()+16);
            if (timer == 0){
                ModUtils.pullPlayer(this, this.world, -0.1, -0.01, this.getX(), this.getZ(), this.getX()-16, this.getY()-32, this.getZ()-16, this.getX()+16, this.getY()+32, this.getZ()+16);
                int rad = 3;
                for (var _y = 0; _y < this.world.getHeight(); _y++){
                    for (var _x = 0; _x < rad; _x++){
                        for (var _z = 0; _z < rad; _z++){
                            BlockPos pos = new BlockPos(this.getBlockX() - 1 + _x, _y, this.getBlockZ() - 1 + _z);
                            this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
                            this.world.addParticle(this.getParticleType(), this.getBlockX() - 1 + _x, _y + 0.5, this.getBlockZ() - 1 + _z, (-1 + _x)/4.0, 0.0, (-1 + _z)/4.0);
                        }
                    }
                }
            }
            for (var _y = 0; _y < this.world.getHeight(); _y++){
                BlockPos pos = new BlockPos(this.getBlockX(), _y, this.getBlockZ());
                this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
                if (_y / 2.0 == Math.round(_y / 2.0)) {
                    this.world.addParticle(this.getParticleType(), this.getBlockX(), _y, this.getBlockZ(), 0.0, 0.0, 0.0);
                }
            }
        }
        if (timer < 0){
            this.discard();
        }
    }

    @Override
    public boolean collides() {
        return false;
    }

    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.END_ROD;
    }

    @Override
    protected boolean isBurning() {
        return false;
    }
}
