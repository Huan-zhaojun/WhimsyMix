package com.ddhuan.whimsymix.common.entity.render;

import com.ddhuan.whimsymix.common.entity.EntityTypeRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntityRenderRegistryManager {
    private EntityRenderRegistryManager() {
    }

    public static void register() {
        //注册实体渲染器
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.splitHeavenGreatAxeEntity.get(), SplitHeavenGreatAxeEntityRender::new);
    }
}
