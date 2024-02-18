package com.chromanyan.chromaticarsenal.util;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

public class EnigmaticLegacyHelper {
    private EnigmaticLegacyHelper() {

    }

    public static boolean isTheCursedOne(Player player) {
        if (player == null) return false;
        if (!ModList.get().isLoaded("enigmaticlegacy")) return false;
        return SuperpositionHandler.isTheCursedOne(player);
    }
}
