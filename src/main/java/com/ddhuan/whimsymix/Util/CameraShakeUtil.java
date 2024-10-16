package com.ddhuan.whimsymix.Util;

import net.minecraftforge.client.event.EntityViewRenderEvent;

//负责玩家相机镜头抖动处理
public final class CameraShakeUtil {
    private CameraShakeUtil() {
    }

    //使用柏林噪声
    public static class Perlin {
        private static final PerlinNoise perlinNoise = new PerlinNoise();

        public static void addAllShake(EntityViewRenderEvent.CameraSetup event, float shakeIntensity) {
            addAllShake(event, shakeIntensity, shakeIntensity, shakeIntensity);
        }

        //分别添加Pitch、Yaw、Roll上的抖动值，震动强度为0即不生效
        public static void addAllShake(EntityViewRenderEvent.CameraSetup event, float shakePitch, float shakeYaw, float shakeRoll) {
            if (shakePitch > 0) addPitchShake(event, shakePitch);
            if (shakeYaw > 0) addYawShake(event, shakeYaw);
            if (shakeRoll > 0) addRollShake(event, shakeRoll);
        }

        public static void addPitchShake(EntityViewRenderEvent.CameraSetup event, float shakeIntensity) {
            event.setPitch(event.getPitch() + (float) perlinNoise.noise(System.currentTimeMillis() * 0.01, 0, 0) * shakeIntensity);
        }

        public static void addYawShake(EntityViewRenderEvent.CameraSetup event, float shakeIntensity) {
            event.setYaw(event.getYaw() + (float) perlinNoise.noise(0, System.currentTimeMillis() * 0.01, 0) * shakeIntensity);
        }

        public static void addRollShake(EntityViewRenderEvent.CameraSetup event, float shakeIntensity) {
            event.setRoll(event.getRoll() + (float) perlinNoise.noise(0, 0, System.currentTimeMillis() * 0.01) * shakeIntensity);
        }
    }

    //使用正弦余弦函数
    public static class Sine {
        public static float defaultFrequency = 5;

        public static void addAllShake(EntityViewRenderEvent.CameraSetup event, float shakeIntensity) {
            addAllShake(event, shakeIntensity, defaultFrequency, shakeIntensity, defaultFrequency, shakeIntensity, defaultFrequency);
        }

        public static void addAllShake(EntityViewRenderEvent.CameraSetup event,
                                       float pitchIntensity, float yawIntensity, float rollIntensity) {
            addAllShake(event, pitchIntensity, defaultFrequency, yawIntensity, defaultFrequency, rollIntensity, defaultFrequency);
        }

        public static void addAllShake(EntityViewRenderEvent.CameraSetup event, float shakeIntensity, float frequency) {
            addAllShake(event, shakeIntensity, frequency, shakeIntensity, frequency, shakeIntensity, frequency);
        }

        public static void addAllShake(EntityViewRenderEvent.CameraSetup event,
                                       float pitchIntensity, float pitchFrequency,
                                       float yawIntensity, float yawFrequency,
                                       float rollIntensity, float rollFrequency) {
            long currentTime = System.nanoTime();
            float time = (currentTime / 1_000_000_000F) % 1; //获取时间并限制在0到1之间

            // 计算震动效果
            float pitchShake = (pitchFrequency > 0) ? (float) (Math.cos(time * Math.PI * 2 * pitchFrequency) * pitchIntensity) : 0;
            float yawShake = (yawFrequency > 0) ? (float) (Math.sin(time * Math.PI * 2 * yawFrequency) * yawIntensity) : 0;
            float rollShake = (rollFrequency > 0) ? (float) (Math.sin(time * Math.PI * 2 * rollFrequency) * rollIntensity) : 0;

            //应用震动到镜头
            if (pitchIntensity > 0) event.setPitch(event.getPitch() + pitchShake);
            if (yawIntensity > 0) event.setYaw(event.getYaw() + yawShake);
            if (rollIntensity > 0) event.setRoll(event.getRoll() + rollShake);
        }
    }
}
