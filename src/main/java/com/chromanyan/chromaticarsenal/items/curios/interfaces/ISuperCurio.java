package com.chromanyan.chromaticarsenal.items.curios.interfaces;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public interface ISuperCurio {
    @Nullable RegistryObject<Item> getInferiorVariant();
}
