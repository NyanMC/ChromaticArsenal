package com.chromanyan.chromaticarsenal.items.curios.advanced;

import java.util.UUID;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import top.theillusivec4.curios.api.SlotContext;

public class CurioShieldOfUndying extends BaseSuperCurio {

	public CurioShieldOfUndying() {
		super(ModItems.GLASS_SHIELD);
	}

	@SuppressWarnings("all")
	@Override
	public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		if (livingEntity.level.isClientSide) {
			return;
		}
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt.contains("counter") && nbt.getInt("counter") > 0) {
			nbt.putInt("counter", nbt.getInt("counter") - 1);
			if (nbt.getInt("counter") == 0) {
				livingEntity.getCommandSenderWorld().playSound((PlayerEntity)null, livingEntity.blockPosition(), SoundEvents.GLASS_PLACE, SoundCategory.PLAYERS, 0.5F, 1.0F);
				livingEntity.getCommandSenderWorld().playSound((PlayerEntity)null, livingEntity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 1.0F);
			}
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
