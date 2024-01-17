package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS_REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, ChromaticArsenal.MODID);

    public static final RegistryObject<Potion> INFERNO = POTIONS_REGISTRY.register("inferno",
            () -> new Potion(new MobEffectInstance(ModEffects.INFERNO.get(), 200))
    );

    public static final RegistryObject<Potion> LONG_INFERNO = POTIONS_REGISTRY.register("long_inferno",
            () -> new Potion(new MobEffectInstance(ModEffects.INFERNO.get(), 400))
    );

    public static void doRecipes() {
        // the best way to go about this is using an access transformer, god save us all
        PotionBrewing.addMix(Potions.AWKWARD, ModItems.SPICY_COAL.get(), INFERNO.get());
        PotionBrewing.addMix(INFERNO.get(), Items.REDSTONE, LONG_INFERNO.get());
    }
}
