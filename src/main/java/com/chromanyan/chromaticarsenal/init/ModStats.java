package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;

public class ModStats {
    public static Stat<ResourceLocation> GSHIELD_TOTAL_BLOCK;
    public static Stat<ResourceLocation> CHROMA_SALVAGER_USES;

    public static ResourceLocation GSHIELD_TOTAL_BLOCK_LOCATION = new ResourceLocation(ChromaticArsenal.MODID, "glass_shield_total_block");
    public static ResourceLocation CHROMA_SALVAGER_USES_LOCATION = new ResourceLocation(ChromaticArsenal.MODID, "chroma_salvager_uses");

    public static void load() {
        GSHIELD_TOTAL_BLOCK = Stats.CUSTOM.get(GSHIELD_TOTAL_BLOCK_LOCATION);
        CHROMA_SALVAGER_USES = Stats.CUSTOM.get(CHROMA_SALVAGER_USES_LOCATION);
    }
}
