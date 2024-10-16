package com.ddhuan.whimsymix.common.event;

import com.ddhuan.whimsymix.Util.CameraShakeUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public final class EntityViewRenderEvent {
    private EntityViewRenderEvent() {
    }

    public static float shakeIntensity = 0;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onCameraSetup(net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup event) {
        //CameraShakeUtil.Sine.addAllShake(event,0.5f,20);//正余弦函数生成震动
        CameraShakeUtil.Perlin.addAllShake(event, shakeIntensity);//柏林噪声生成震动
    }
}
