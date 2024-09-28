package com.ddhuan.whimsymix;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static void init() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings");
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
