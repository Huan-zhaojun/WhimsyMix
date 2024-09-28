package com.ddhuan.whimsymix;

import com.ddhuan.whimsymix.common.block.BlockRegistry;
import com.ddhuan.whimsymix.common.entity.EntityTypeRegistry;
import com.ddhuan.whimsymix.common.entity.render.EntityRenderRegistryManager;
import com.ddhuan.whimsymix.common.item.ItemRegistry;
import com.ddhuan.whimsymix.network.Network;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ddhuan.whimsymix.whimsymix.MOD_ID;

@Mod(MOD_ID)
public class whimsymix {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "whimsymix";

    public whimsymix() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onClientSetUp);
        //setConfig(modEventBus);//配置文件设置
        ItemRegistry.ITEMS.register(modEventBus);
        EntityTypeRegistry.ENTITIES.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        //FluidRegistry.FLUIDS.register(modEventBus);
        //EnchantmentRegistry.ENCHANTMENTS.register(modEventBus);
        //TileEntityTypeRegistry.TILE_ENTITIES.register(modEventBus);
        //DataSerializersRegistry.DATA_SERIALIZERS.register(modEventBus);
    }

    private void setConfig(IEventBus modEventBus) {//配置文件设置
        Config.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        modEventBus.addListener(this::onConfig);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        //注册网络包
        Network.registerMessage();
    }

    @OnlyIn(Dist.CLIENT)
    private void onClientSetUp(FMLClientSetupEvent event) {
        //注册实体渲染器
        EntityRenderRegistryManager.register();
        event.enqueueWork(() -> {
            //注册方块渲染器
            BlockRegistry.setBlockRenderType();

            //注册流体渲染器
            //setFluidRenderType();
        });
    }

    private void onConfig(ModConfig.ModConfigEvent event) {
        //检查是否是需要监听的配置文件
        if (event.getConfig().getSpec() == Config.COMMON_CONFIG) {
            //更新静态变量

        }
    }
}
