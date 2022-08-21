package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.effects.EffectFractured;
import com.chromanyan.chromaticarsenal.effects.EffectSpatial;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions {
	public static final DeferredRegister<Effect> EFFECTS_REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, Reference.MODID);
	
	public static final RegistryObject<Effect> FRACTURED = modEffect("fractured", new EffectFractured());
	public static final RegistryObject<Effect> SPATIAL = modEffect("spatial", new EffectSpatial());
	
	private static RegistryObject<Effect> modEffect(String name, Effect effect) {
		return EFFECTS_REGISTRY.register(name, () -> effect);
	}
}
