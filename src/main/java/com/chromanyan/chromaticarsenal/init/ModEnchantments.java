package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.enchantments.EnchantmentChromaticTwisting;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS_REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChromaticArsenal.MODID);
    public static final RegistryObject<Enchantment> CHROMATIC_TWISTING = ENCHANTMENTS_REGISTRY.register("chromatic_twisting", EnchantmentChromaticTwisting::new);
}
