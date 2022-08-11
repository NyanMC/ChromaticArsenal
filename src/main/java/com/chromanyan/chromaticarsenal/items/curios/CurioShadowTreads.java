package com.chromanyan.chromaticarsenal.items.curios;

import java.util.UUID;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

public class CurioShadowTreads extends BaseCurioItem {
	
	@Override
	public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		World world = livingEntity.getCommandSenderWorld();
		if (world != null && !world.isClientSide()) {
			// why must forge 1.16 be this way
			// hey you, code viewer! you're smart, have any suggestions on how to fix this to work like the 1.12 version? if so, please open an issue, or better yet, a PR. i'm stumped.
			if (world.getLightEngine().getRawBrightness(livingEntity.blockPosition(), 0) <= 7) {
				livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 25, ModConfig.COMMON.darkspeedPotency.get(), true, true));
			}
		}
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid,
			ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SOUL_SPEED, stack) > 0) {
			atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":speed_bonus", EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SOUL_SPEED, stack) * ModConfig.COMMON.enchantmentSpeedMultiplier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
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
