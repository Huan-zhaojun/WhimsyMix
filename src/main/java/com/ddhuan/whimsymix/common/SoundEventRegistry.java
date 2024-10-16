package com.ddhuan.whimsymix.common;

import com.ddhuan.whimsymix.whimsymix;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, whimsymix.MOD_ID);
    public static final RegistryObject<SoundEvent> explosionSound = SOUNDS.register("explosion",
            () -> new SoundEvent(new ResourceLocation(whimsymix.MOD_ID, "explosion")));
}
