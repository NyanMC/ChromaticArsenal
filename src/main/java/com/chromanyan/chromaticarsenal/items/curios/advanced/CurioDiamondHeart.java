package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;

public class CurioDiamondHeart extends BaseSuperCurio {

	public CurioDiamondHeart() {
		super(ModItems.GOLDEN_HEART);
	}

	@SuppressWarnings("all")
	@Override
	public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		super.curioTick(identifier, index, livingEntity, stack);
		CompoundNBT nbt = stack.getOrCreateTag();
		if (livingEntity.level.isClientSide) {
			if (nbt.contains("counter") && nbt.getInt("counter") == 1) {
				if (livingEntity instanceof PlayerEntity) {
					PlayerEntity playerEntity = (PlayerEntity) livingEntity;
					playerEntity.displayClientMessage(new TranslationTextComponent("message.chromaticarsenal.revival_cooldown_finished"), false);
				}
			}
		} else {
			if (nbt.contains("counter") && nbt.getInt("counter") > 0) {
				nbt.putInt("counter", nbt.getInt("counter") - 1);
				if (nbt.getInt("counter") == 0) {
					livingEntity.getCommandSenderWorld().playSound((PlayerEntity)null, livingEntity.blockPosition(), SoundEvents.IRON_GOLEM_REPAIR, SoundCategory.PLAYERS, 0.5F, 1.0F);
				}
			}
		}
	}

}
