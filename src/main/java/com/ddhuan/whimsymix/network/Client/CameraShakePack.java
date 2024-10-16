package com.ddhuan.whimsymix.network.Client;

import com.ddhuan.whimsymix.common.event.EntityViewRenderEvent;
import com.ddhuan.whimsymix.network.NetworkPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CameraShakePack extends NetworkPack {
    private final float shakeIntensity;

    public CameraShakePack(float shakeIntensity) {
        this.shakeIntensity = shakeIntensity;
    }

    public CameraShakePack(PacketBuffer buffer) {
        this.shakeIntensity = buffer.readFloat();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeFloat(shakeIntensity);
    }

    @Override
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientWorld world = mc.world;
            if (world != null && world.isRemote) {
                EntityViewRenderEvent.shakeIntensity = shakeIntensity;
            }
        });
        context.setPacketHandled(true);
    }
}
