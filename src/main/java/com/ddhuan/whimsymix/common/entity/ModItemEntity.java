package com.ddhuan.whimsymix.common.entity;

import com.ddhuan.whimsymix.Util.ModNBTUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;


public abstract class ModItemEntity extends  abstractModEntity {
    public ItemStack itemStack = ItemStack.EMPTY;
    public static final DataParameter<ItemStack> itemStackData = EntityDataManager.createKey(ModItemEntity.class, DataSerializers.ITEMSTACK);

    public ModItemEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.ignoreFrustumCheck = true;//忽略视椎体对实体渲染的影响
    }

    public ModItemEntity(EntityType<?> entityTypeIn, World worldIn, double x, double y, double z, ItemStack itemStack) {
        this(entityTypeIn, worldIn);
        this.setPosition(x, y, z);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.itemStack = itemStack;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(itemStackData, ItemStack.EMPTY);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        itemStack = ModNBTUtil.readItemStack(compound.getCompound("ModItemEntity&ItemStack"));
        this.dataManager.set(itemStackData, this.itemStack);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("ModItemEntity&ItemStack", ModNBTUtil.writeItemStack(new CompoundNBT(), itemStack));
    }

    public void synch() {
        if (!world.isRemote) {
            this.dataManager.set(itemStackData, this.itemStack);
        }
        if (world.isRemote) {
            this.itemStack = this.dataManager.get(itemStackData);
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onKillCommand() {
        super.onKillCommand();
    }
}
