package com.ddhuan.whimsymix.common.entity;

import com.ddhuan.whimsymix.whimsymix;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, whimsymix.MOD_ID);
}
