package com.ddhuan.whimsymix.network;

import com.ddhuan.whimsymix.network.Client.CameraShakePack;
import com.ddhuan.whimsymix.whimsymix;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network {
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(whimsymix.MOD_ID, "network"),
            () -> VERSION, VERSION::equals, VERSION::equals);

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE.registerMessage(nextID(), CameraShakePack.class, CameraShakePack::toBytes, CameraShakePack::new, CameraShakePack::handler);
        }
}
