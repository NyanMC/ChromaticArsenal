package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;

public class ModStats {
    public static Stat<ResourceLocation> GSHIELD_TOTAL_BLOCK;

    public static ResourceLocation GSHIELD_TOTAL_BLOCK_LOCATION = new ResourceLocation(Reference.MODID, "glass_shield_total_block");

    public static void load() {
        GSHIELD_TOTAL_BLOCK = Stats.CUSTOM.get(GSHIELD_TOTAL_BLOCK_LOCATION);
    }
}
