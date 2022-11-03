package com.chromanyan.chromaticarsenal.items.curios.advanced;

import java.util.UUID;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class CurioShieldOfUndying extends BaseSuperCurio {

	public CurioShieldOfUndying() {
		super(ModItems.GLASS_SHIELD);
	}

	@SuppressWarnings("all")
	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		LivingEntity livingEntity = context.entity();
		if (livingEntity.level.isClientSide) {
			return;
		}
		CompoundTag nbt = stack.getOrCreateTag();
		CooldownHelper.tickCounter(nbt, SoundEvents.GLASS_PLACE, livingEntity);
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		LivingEntity entity = slotContext.entity();
		if (entity.getHealth() > entity.getMaxHealth()) {
			entity.setHealth(entity.getMaxHealth()); // to be honest i have no clue if this will even do anything, depends on if onUnequip processes before or after attribute changes
		}
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid,
																		ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
		atts.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, Reference.MODID + ":undying_health_tradeoff", ModConfig.COMMON.healthTradeoff.get(), AttributeModifier.Operation.fromValue(2)));
		return atts;
	}

}
