package com.ddhuan.whimsymix.common.entity.render;

import com.ddhuan.whimsymix.Util.AnimationTime.AnimationTimeSequence;
import com.ddhuan.whimsymix.Util.AnimationTime.SequenceManager;
import com.ddhuan.whimsymix.common.entity.SplitHeavenGreatAxeEntity;
import com.ddhuan.whimsymix.whimsymix;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

import static com.ddhuan.whimsymix.common.entity.SplitHeavenGreatAxeEntity.maxActiveTime;

public class SplitHeavenGreatAxeEntityRender<T extends SplitHeavenGreatAxeEntity> extends ModItemEntityRender<T> {
    private static SequenceManager timeSM = null;
    public static AnimationTimeSequence timeSeq1 = null;

    protected SplitHeavenGreatAxeEntityRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        renderAxe(entityIn, matrixStackIn, bufferIn);
        renewActiveTime(entityIn);//更新客户端渲染的动画时间
    }

    private void renderAxe(T entityIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        timeSeq1 = AnimationTimeSequence.getOrCreate(timeSeq1, 2000, 2000, 2500);
        timeSM = SequenceManager.getOrCreate(timeSM, new Integer[]{2000, 2000, 2500}, new Integer[]{4000, 3000});
        Direction direction = entityIn.direction;
        int animationTime = entityIn.activeTime;//当前动画时间
        //动画最大改变两量
        double max_changeSize = 10, max_changeY = 10, max_changeZ = 15, max_changeRotate1 = 25.0, max_changeRotate2 = 83.0;
        double baseRotate = 10.0;
        double testBox_size = 0.5;//测试盒大小
        matrixStackIn.push();
        //基础位置移动
        if ((direction == Direction.EAST || direction == Direction.WEST)) {
            matrixStackIn.translate((direction == Direction.EAST ? -1 : +1) * 0.5D,
                    3D + timeSeq1.getExponentAdd(1, animationTime, 2.0, max_changeY),
                    (direction == Direction.EAST ? 1 : -1) *
                            (0.5D + timeSeq1.getExponentAdd(1, animationTime, 2.0, max_changeZ)));
        } else if ((direction == Direction.NORTH || direction == Direction.SOUTH)) {
            matrixStackIn.translate((direction == Direction.NORTH ? 1 : -1) *
                            (0.5D + timeSeq1.getExponentAdd(1, animationTime, 2.0, max_changeZ)),
                    3D + timeSeq1.getExponentAdd(1, animationTime, 2.0, max_changeY),
                    (direction == Direction.NORTH ? -1 : +1) * 0.5D);
        }


        testBox(matrixStackIn, bufferIn, testBox_size, 1, 0, 0);

        double y = 0.5925D, correctDegrees = 20.0D;
        matrixStackIn.translate(y * Math.sin(Math.toRadians(correctDegrees) - 0.275), -y * Math.cos(Math.toRadians(correctDegrees)), -0.15D);
        Float[] yRotations = {270F, 90F, 180F, 0F};//北、南、西、东
        matrixStackIn.rotate(Vector3f.YN.rotationDegrees(yRotations[direction.getIndex() - 2]));//朝向设置

        //设置大小
        float axe_size = (float) timeSeq1.getExponentAdd(1, animationTime, 2.0, max_changeSize);
        matrixStackIn.scale(0.25f + axe_size, 0.25f + axe_size, 0.25f + axe_size);

        matrixStackIn.rotate(Vector3f.ZN.rotationDegrees((float) correctDegrees));//初始角度纠正

        testBox(matrixStackIn, bufferIn, testBox_size, 0, 1, 0);//俯仰旋转中心测试

        //俯仰设置
        matrixStackIn.rotate(Vector3f.ZN.rotationDegrees((float) (baseRotate +
                timeSeq1.getReExponentAdd(1, animationTime, 2.0, -(max_changeRotate1 + baseRotate)) +
                timeSeq1.getExponentAdd(2, animationTime, 3.0, max_changeRotate1 + max_changeRotate2))));


        if (animationTime - timeSeq1.getPreSeq(3) > 0 && animationTime - timeSeq1.getPreSeq(3) <= timeSeq1.getSeq(3)) {
            matrixStackIn.translate(0.3, 3.80, 0);
            testBox(matrixStackIn, bufferIn, testBox_size, 0, 0, 1);//击中位置
            renderCircularWaveBlade(false, matrixStackIn, animationTime, max_changeRotate2);//渲染圆状波刃气
            renderCircularWaveBlade(true, matrixStackIn, animationTime, max_changeRotate2);
            matrixStackIn.translate(-0.3, -3.80, 0);
        }

        if (animationTime - timeSeq1.getPreSeq(3) > 0 && animationTime - timeSeq1.getPreSeq(3) <= timeSM.byIndex(1).getSeq(2)) {
            matrixStackIn.rotate(Vector3f.ZN.rotationDegrees((float) -correctDegrees));
            matrixStackIn.translate(3, 4.5, 0);
            renderFlakyWaveBlade(matrixStackIn, animationTime, max_changeRotate2);//渲染片状波刃气
            testBox(matrixStackIn, bufferIn, testBox_size, 0, 0, 1);
            matrixStackIn.translate(-3, -4.5, 0);
            matrixStackIn.rotate(Vector3f.ZN.rotationDegrees((float) correctDegrees));
        }

        matrixStackIn.translate(-y * Math.sin(Math.toRadians(correctDegrees) + 0.275), y * Math.cos(Math.toRadians(correctDegrees)), 0.15D);//矩阵回置
        if (animationTime > 2000 && entityIn.itemStack.getTag() != null)
            entityIn.itemStack.getTag().putBoolean("red_axe", true);
        testBox(matrixStackIn, bufferIn, testBox_size, 0, 1, 0);
        Minecraft.getInstance().getItemRenderer().renderItem(entityIn.itemStack,
                ItemCameraTransforms.TransformType.FIXED, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);

        matrixStackIn.pop();
    }

    //测试框
    private static void testBox(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, double size, float red, float green, float blue) {
        if (!Minecraft.getInstance().getRenderManager().isDebugBoundingBox()) return;//判定箱显示不开启不显示
        matrixStackIn.push();
        WorldRenderer.drawBoundingBox(matrixStackIn, bufferIn.getBuffer(RenderType.getLines()),
                -size, -size, -size, size, size, size, red, green, blue, 1.0F);

        float size1 = (float) size;
        Matrix4f matrix = matrixStackIn.getLast().getMatrix();
        IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderType.getLines());
        float[][] vertices = {
                {-size1, -size1, -size1}, {size1, size1, size1},
                {-size1, -size1, size1}, {size1, size1, -size1},
                {-size1, size1, -size1}, {size1, -size1, size1},
                {size1, -size1, -size1}, {-size1, size1, size1}
        };
        for (float[] vertex : vertices)
            vertexBuilder.pos(matrix, vertex[0], vertex[1], vertex[2]).color(red, green, blue, 1.0F).endVertex();
        matrixStackIn.pop();
    }

    //渲染片状波刃气
    private static void renderFlakyWaveBlade(MatrixStack matrixStack, int animationTime, double rotate) {
        ResourceLocation textureLocation = new ResourceLocation(whimsymix.MOD_ID, "textures/effect/flaky_wave_blade.png");
        float length = 1f;
        float height = 1f;

        matrixStack.push();
        //矩阵设置
        matrixStack.rotate(Vector3f.ZN.rotationDegrees((float) (-90 - (-90 + rotate))));
        matrixStack.translate(-5 + timeSM.byIndex(1).getLinearAdd(2, animationTime, 50), 0, 0);
        matrixStack.scale(10F, 15F, 10F);

        //设置渲染模式
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        //绑定纹理
        Minecraft.getInstance().getTextureManager().bindTexture(textureLocation);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);

        //设置纹理坐标和透明度
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        bufferBuilder.pos(matrix4f, length, 0, 0).tex(0, 1).endVertex();
        bufferBuilder.pos(matrix4f, 0, 0, 0).tex(1, 1).endVertex();
        bufferBuilder.pos(matrix4f, 0, height, 0).tex(1, 0).endVertex();
        bufferBuilder.pos(matrix4f, length, height, 0).tex(0, 0).endVertex();

        tessellator.draw();

        //恢复渲染设置
        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        matrixStack.pop();
    }

    //渲染圆状波刃气
    private void renderCircularWaveBlade(boolean flag, MatrixStack matrixStack, int animationTime, double rotate) {
        ResourceLocation textureLocation = new ResourceLocation(whimsymix.MOD_ID, "textures/effect/wave_blade.png");
        float length = 1f;
        float width = 1f;

        matrixStack.push();
        //矩阵设置
        matrixStack.rotate(Vector3f.ZN.rotationDegrees((float) -(20 + rotate)));
        if (flag) matrixStack.rotate(Vector3f.ZN.rotationDegrees(90));
        matrixStack.translate(0, 0, 0);
        float size = (float) timeSeq1.getReExponentAdd(3, animationTime, 1.5, 35);
        matrixStack.scale(1 + size, 1, 1 + size);

        //设置渲染模式
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        //绑定纹理
        Minecraft.getInstance().getTextureManager().bindTexture(textureLocation);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);

        //设置纹理坐标和透明度
        float halfLength = (length / 2.0f);
        float halfWidth = (width / 2.0f);
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        bufferBuilder.pos(matrix4f, -halfWidth, 0, -halfLength).tex(0, 0).endVertex();
        bufferBuilder.pos(matrix4f, halfWidth, 0, -halfLength).tex(0, 1).endVertex();
        bufferBuilder.pos(matrix4f, halfWidth, 0, halfLength).tex(1, 1).endVertex();
        bufferBuilder.pos(matrix4f, -halfWidth, -0, halfLength).tex(1, 0).endVertex();

        tessellator.draw();

        //恢复渲染设置
        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        matrixStack.pop();
    }

    //更新客户端渲染的动画时间
    private void renewActiveTime(T entityIn) {
        if (entityIn.lastTime == 0) entityIn.lastTime = System.currentTimeMillis();
        if (entityIn.activeTime < maxActiveTime && !Minecraft.getInstance().isGamePaused()) {
            entityIn.activeTime += (int) (System.currentTimeMillis() - entityIn.lastTime);
            entityIn.lastTime = System.currentTimeMillis();
        }
    }

}
