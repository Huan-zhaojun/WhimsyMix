package com.ddhuan.whimsymix.common.item;

import com.ddhuan.whimsymix.whimsymix;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, whimsymix.MOD_ID);

    public static final ItemGroup WhimsyMix = new ItemGroup("whimsymix") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(splitHeavenGreatAxe.get());
        }
    };

    //开天神斧
    public static RegistryObject<SplitHeavenGreatAxe> splitHeavenGreatAxe = ITEMS.register("split_heaven_great_axe",
            () -> new SplitHeavenGreatAxe(new Item.Properties().group(ItemRegistry.WhimsyMix).maxDamage(Integer.MAX_VALUE).isImmuneToFire()));


    //添加物品标签谓词
    public static void registerPredicates() {
        ItemModelsProperties.registerProperty(splitHeavenGreatAxe.get(),
                new ResourceLocation(whimsymix.MOD_ID, "red_axe"),
                (stack, world, entity) -> {
                    if (stack.getTag() != null)
                        return stack.getTag().getBoolean("red_axe") ? 1 : 0;
                    return 0;
                });
    }
}