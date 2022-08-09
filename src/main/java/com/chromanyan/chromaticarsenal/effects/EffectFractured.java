package com.chromanyan.chromaticarsenal.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectFractured extends Effect {

	public EffectFractured() {
		super(EffectType.HARMFUL, 0x3C0000);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "69d353d6-f113-41ce-a437-35095cb2cae8", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
	
	@Override
	public List<ItemStack> getCurativeItems() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		return ret; // in other words, not curable
	}

}
