package com.ddhuan.whimsymix.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;

public abstract class abstractModEntity extends Entity {
    public boolean onlyOnceSynch = false;//只会同步一次

    public abstractModEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected abstract void registerData();

    @Override
    protected abstract void readAdditional(CompoundNBT compound);

    @Override
    protected abstract void writeAdditional(CompoundNBT compound);

    public abstract void synch();

    public abstract void onlyOnceSynch();

    @Override
    public abstract IPacket<?> createSpawnPacket();

    @Override
    public void tick() {
        super.tick();
        this.onlyOnceSynch();//仅在重新进入游戏同步一次数据到客户端
        this.synch();//同步服务端数据到客户端
    }
}
