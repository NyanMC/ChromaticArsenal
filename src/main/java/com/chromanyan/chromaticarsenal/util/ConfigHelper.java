package com.chromanyan.chromaticarsenal.util;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ConfigHelper {

    private ConfigHelper() {

    }

    public static boolean effectInBlacklist(List<? extends String> stringList, MobEffect mobEffect) {
        if (stringList.isEmpty()) return false;

        for (String blacklisted : stringList) {
            ResourceLocation blacklistedRL = ResourceLocation.tryParse(blacklisted);

            if (blacklistedRL == null) {
                ChromaticArsenal.LOGGER.error("CONFIG PARSE ERROR: Failed to parse \"{}\" as ResourceLocation, skipping", blacklisted);
                continue;
            }

            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(blacklistedRL);

            if (effect == null) {
                ChromaticArsenal.LOGGER.error("CONFIG PARSE ERROR: The resource location \"" + blacklisted + "\" was not recognized as a potion effect, skipping");
                continue;
            }

            if (mobEffect == effect) {
                return true;
            }
        }

        return false;
    }
}
