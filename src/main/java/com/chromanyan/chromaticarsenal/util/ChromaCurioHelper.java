package com.chromanyan.chromaticarsenal.util;

import com.chromanyan.chromaticarsenal.items.curios.interfaces.ISuperCurio;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

@SuppressWarnings("unused")
public class ChromaCurioHelper {

    private ChromaCurioHelper() { // i also don't want people to create curio helpers

    }

    public static Optional<SlotResult> getCurio(LivingEntity livingEntity, Item item) {
        return CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, item);
    }

    public static boolean isSuperCurio(Item item) {
        return item instanceof ISuperCurio;
    }
}
