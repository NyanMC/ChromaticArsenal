package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.effects.EffectFractured;
import com.chromanyan.chromaticarsenal.effects.EffectSpatial;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<MobEffect> EFFECTS_REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Reference.MODID);

    public static final RegistryObject<MobEffect> FRACTURED = EFFECTS_REGISTRY.register("fractured", EffectFractured::new);
    public static final RegistryObject<MobEffect> SPATIAL = EFFECTS_REGISTRY.register("spatial", EffectSpatial::new);

}
