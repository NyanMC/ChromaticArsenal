package com.chromanyan.chromaticarsenal.items.food;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModPotions;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class Cosmicola extends Item {

	public Cosmicola() {
		super(new Item.Properties()
				.tab(ChromaticArsenal.GROUP)
				.stacksTo(16)
				.rarity(Rarity.EPIC)
				.food(new Food.Builder()
						.nutrition(15)
						.saturationMod(0.7F)
						.alwaysEat()
						.effect(()-> new EffectInstance(ModPotions.SPATIAL.get(), 3600, 0), 1.0F)
						.build()));
	}
	
	@Override
	public SoundEvent getEatingSound() {
		// TODO Auto-generated method stub
		return SoundEvents.GENERIC_DRINK;
	}
}
