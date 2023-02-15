package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
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
			if (CooldownHelper.getCounter(nbt) == 1) {
				if (livingEntity instanceof Player) {
					Player playerEntity = (Player) livingEntity;
					playerEntity.displayClientMessage(new TranslatableComponent("message.chromaticarsenal.revival_cooldown_finished"), false);
				}
			}
		} else {
			CooldownHelper.tickCounter(nbt, SoundEvents.IRON_GOLEM_REPAIR, livingEntity);
		}
	}

}
