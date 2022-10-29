package com.chromanyan.chromaticarsenal.items.curios;

import java.util.UUID;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class CurioShadowTreads extends BaseCurioItem {
	final Common config = ModConfig.COMMON;

	@SuppressWarnings("all")
	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		LivingEntity livingEntity = context.entity();
		Level world = livingEntity.getCommandSenderWorld();
		if (world != null && !world.isClientSide()) {
			if (world.getMaxLocalRawBrightness(livingEntity.blockPosition()) <= config.maxLightLevel.get()) {
				livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 25, config.darkspeedPotency.get(), true, true));
			}
		}
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SOUL_SPEED, stack) > 0) {
			atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":speed_bonus", EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SOUL_SPEED, stack) * config.enchantmentSpeedMultiplier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
		}
		return atts;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.SOUL_SPEED) {
			return true;
		} else {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}
	}
	
}
