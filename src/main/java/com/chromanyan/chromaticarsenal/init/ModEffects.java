package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS_REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChromaticArsenal.MODID);

    public static final RegistryObject<MobEffect> FRACTURED = EFFECTS_REGISTRY.register("fractured", EffectFractured::new);
    public static final RegistryObject<MobEffect> SPATIAL = EFFECTS_REGISTRY.register("spatial", EffectSpatial::new);
    public static final RegistryObject<MobEffect> CHILLED = EFFECTS_REGISTRY.register("chilled", EffectChilled::new);
    public static final RegistryObject<MobEffect> BUBBLE_PANIC = EFFECTS_REGISTRY.register("bubble_panic", EffectBubblePanic::new);
    public static final RegistryObject<MobEffect> CURSED_REVIVAL = EFFECTS_REGISTRY.register("cursed_revival", EffectCursedRevival::new);
    public static final RegistryObject<MobEffect> INFERNO = EFFECTS_REGISTRY.register("inferno", EffectInferno::new);

}
