package com.ddhuan.whimsymix.common.entity.render;

import com.ddhuan.whimsymix.common.entity.ModItemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public abstract class ModItemEntityRender<T extends ModItemEntity> extends EntityRenderer<T> {
    protected ModItemEntityRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public boolean shouldRender(T livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntityIn, camera, camX, camY, camZ);
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
