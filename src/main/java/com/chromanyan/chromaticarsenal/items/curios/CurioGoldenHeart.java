package com.chromanyan.chromaticarsenal.items.curios;

import java.util.UUID;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import top.theillusivec4.curios.api.SlotContext;

public class CurioGoldenHeart extends BaseCurioItem {
	
	Common config = ModConfig.COMMON;
	
	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		if (!slotContext.getWearer().getCommandSenderWorld().isClientSide) {
			slotContext.getWearer().addEffect(new EffectInstance(Effects.ABSORPTION, (config.absorptionDuration.get() + 5), config.absorptionLevel.get(), true, true));
	    }
	}
	
	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (!living.getCommandSenderWorld().isClientSide && living.tickCount % config.absorptionDuration.get() == 0) {
	    	living.addEffect(new EffectInstance(Effects.ABSORPTION, (config.absorptionDuration.get() + 5), config.absorptionLevel.get(), true, true));
	    }
	}
	
	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		slotContext.getWearer().removeEffect(Effects.ABSORPTION);
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid,
			ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
		atts.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, Reference.MODID + ":health_bonus", config.maxHealthBoost.get(), AttributeModifier.Operation.fromValue(config.maxHealthBoostOperation.get())));
		return atts;
	}
	
	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true; // doesn't work, but i can dream can't i
	}
	
	@Override
	public boolean isPiglinCurrency(ItemStack stack) {
		return true;
	}

}
