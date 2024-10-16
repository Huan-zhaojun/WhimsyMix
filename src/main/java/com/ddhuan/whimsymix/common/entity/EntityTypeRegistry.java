package com.ddhuan.whimsymix.common.entity;

import com.ddhuan.whimsymix.whimsymix;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, whimsymix.MOD_ID);

    public static final RegistryObject<EntityType<SplitHeavenGreatAxeEntity>> splitHeavenGreatAxeEntity = ENTITIES.register("item_entity",
            () -> EntityType.Builder.<SplitHeavenGreatAxeEntity>create(SplitHeavenGreatAxeEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).trackingRange(10).updateInterval(10).build("split_heaven_great_axe"));
}
