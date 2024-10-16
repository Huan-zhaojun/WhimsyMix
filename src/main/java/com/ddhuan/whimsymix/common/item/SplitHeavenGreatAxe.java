package com.ddhuan.whimsymix.common.item;

import com.ddhuan.whimsymix.common.entity.SplitHeavenGreatAxeEntity;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SplitHeavenGreatAxe extends Item {
    private final Multimap<Attribute, AttributeModifier> itemAttributes;

    public SplitHeavenGreatAxe(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", Double.MAX_VALUE, AttributeModifier.Operation.ADDITION));
        this.itemAttributes = builder.build();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (handIn.equals(Hand.MAIN_HAND)) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("red_axe", false);
            SplitHeavenGreatAxeEntity itemEntity = new SplitHeavenGreatAxeEntity(worldIn,
                    playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                    new ItemStack(this, 1, nbt), playerIn.getUniqueID(), playerIn.getHorizontalFacing());
            worldIn.addEntity(itemEntity);
            playerIn.setHeldItem(Hand.MAIN_HAND,ItemStack.EMPTY);
            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        } else
            return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(2, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.itemAttributes : super.getAttributeModifiers(equipmentSlot);
    }
}
