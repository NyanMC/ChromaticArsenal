package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class CurioDiamondHeart extends BaseSuperCurio {

	public CurioDiamondHeart() {
		super(ModItems.GOLDEN_HEART);
	}

	@SuppressWarnings("all")
	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		LivingEntity livingEntity = context.entity();
		super.curioTick(context, stack);
		CompoundTag nbt = stack.getOrCreateTag();
		if (livingEntity.level.isClientSide) {
			if (nbt.contains("counter") && nbt.getInt("counter") == 1) {
				if (livingEntity instanceof Player) {
					Player playerEntity = (Player) livingEntity;
					playerEntity.displayClientMessage(new TranslatableComponent("message.chromaticarsenal.revival_cooldown_finished"), false);
				}
			}
		} else {
			if (nbt.contains("counter") && nbt.getInt("counter") > 0) {
				nbt.putInt("counter", nbt.getInt("counter") - 1);
				if (nbt.getInt("counter") == 0) {
					livingEntity.getCommandSenderWorld().playSound((Player)null, livingEntity.blockPosition(), SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 0.5F, 1.0F);
				}
			}
		}
	}

}
