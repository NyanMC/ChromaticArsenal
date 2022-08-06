package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

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
	
}
