package com.ddhuan.whimsymix.common.entity;

import com.ddhuan.whimsymix.Util.ModNBTUtil;
import com.ddhuan.whimsymix.common.SoundEventRegistry;
import com.ddhuan.whimsymix.common.event.EntityViewRenderEvent;
import com.ddhuan.whimsymix.common.item.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

import static com.ddhuan.whimsymix.common.entity.render.SplitHeavenGreatAxeEntityRender.timeSeq1;

public class SplitHeavenGreatAxeEntity extends ModItemEntity {
    public int activeTime = 0;
    public static final DataParameter<Integer> activeTimeData = EntityDataManager.createKey(SplitHeavenGreatAxeEntity.class, DataSerializers.VARINT);
    public long lastTime = 0;
    public static int maxActiveTime = 10_000;

    public UUID playerUuid = UUID.randomUUID();
    public Direction direction = Direction.EAST;
    public static final DataParameter<Direction> directionData = EntityDataManager.createKey(SplitHeavenGreatAxeEntity.class, DataSerializers.DIRECTION);
    public boolean explosion = true;

    public SplitHeavenGreatAxeEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public SplitHeavenGreatAxeEntity(World worldIn, double x, double y, double z, ItemStack itemStack, UUID playerUuid, Direction direction) {
        super(EntityTypeRegistry.splitHeavenGreatAxeEntity.get(), worldIn, x, y, z, itemStack);
        this.playerUuid = playerUuid;
        this.direction = direction;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(activeTimeData, 0);
        this.dataManager.register(directionData, Direction.NORTH);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        activeTime = compound.getInt("SplitHeavenGreatAxeEntity&activeTime");
        playerUuid = compound.getUniqueId("SplitHeavenGreatAxeEntity&playerUuid");
        direction = ModNBTUtil.readDirection(compound);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("SplitHeavenGreatAxeEntity&activeTime", activeTime);
        compound.putUniqueId("SplitHeavenGreatAxeEntity&playerUuid", playerUuid);
        ModNBTUtil.writeDirection(compound, direction);
    }

    @Override
    public void synch() {
        super.synch();
        if (!world.isRemote) {
            this.dataManager.set(directionData, direction);
        }
        if (world.isRemote) {
            this.direction = this.dataManager.get(directionData);
        }
    }

    @Override
    public void onlyOnceSynch() {
        if (!world.isRemote && !onlyOnceSynch) {
            this.dataManager.set(activeTimeData, activeTime);
            this.onlyOnceSynch = true;
        }
        if (world.isRemote && !onlyOnceSynch) {
            this.activeTime = this.dataManager.get(activeTimeData);
            this.onlyOnceSynch = true;
        }
    }

    private void renewActiveTime() {
        if (this.lastTime == 0) this.lastTime = System.currentTimeMillis();
        if (this.activeTime < maxActiveTime) {
            this.activeTime += (int) (System.currentTimeMillis() - this.lastTime);
            this.lastTime = System.currentTimeMillis();
        }
    }

    @Override
    public void tick() {
        super.tick();
        renewActiveTime();//更新服务端的活跃时间
        //震动
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (world.isRemote && player != null && player.getPositionVec().subtract(this.getPositionVec()).length() <= 200 &&
                timeSeq1 != null && activeTime > timeSeq1.getPreSeq(3) && activeTime <= timeSeq1.getPreSeq(4)) {
            float max = 50;
            EntityViewRenderEvent.shakeIntensity = max - (float) timeSeq1.getReExponentAdd(3, activeTime, 3, max);
        }
        if (activeTime >= maxActiveTime) {
            EntityViewRenderEvent.shakeIntensity = 0;
            if (!world.isRemote) {
                PlayerEntity player1 = world.getPlayerByUuid(playerUuid);
                if (player1 != null)
                    player1.addItemStackToInventory(new ItemStack(ItemRegistry.splitHeavenGreatAxe.get(), 1));
            }
            remove();
        }

        if (activeTime > 4000 && explosion) {
            world.playSound(null, getPosition(), SoundEventRegistry.explosionSound.get(), SoundCategory.VOICE, 100f, 0.75f);
            explosion = false;
        }
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        if (!world.isRemote && activeTime > 4000) {
            double v = ((activeTime - 4000) / 55.0) * 9;
            mutable.setPos(this.getPosition().offset(direction.rotateY(), 15).offset(direction, (int) (-15 + v)).offset(Direction.DOWN, 150));
            for (int i = 1; i <= 250; i++) {
                for (int j = 0; j < 7; j++) {
                    for (int k = 0; k < 12; k++) {
                        world.setBlockState(mutable.offset(direction.rotateY(), -3 + j).offset(direction, k), Blocks.AIR.getDefaultState());
                    }
                }
                mutable.setY(mutable.getY() + 1);
            }
        }
    }

    @Override
    public void onKillCommand() {
        super.onKillCommand();
    }
}
