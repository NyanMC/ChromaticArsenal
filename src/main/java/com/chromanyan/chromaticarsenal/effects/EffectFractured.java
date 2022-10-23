package com.chromanyan.chromaticarsenal.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class EffectFractured extends MobEffect {

	public EffectFractured() {
		super(MobEffectCategory.HARMFUL, 0x00003C);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "69d353d6-f113-41ce-a437-35095cb2cae8", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
	
	@Override
	public List<ItemStack> getCurativeItems() {
		// ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		// return ret;
		return new ArrayList<ItemStack>(); // in other words, not curable
	}

}
