package com.ddhuan.whimsymix.common.block;

import com.ddhuan.whimsymix.whimsymix;
import net.minecraft.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, whimsymix.MOD_ID);

    public static void setBlockRenderType() {
        //RenderTypeLookup.setRenderLayer(, RenderType.getCutout());
    }
}
