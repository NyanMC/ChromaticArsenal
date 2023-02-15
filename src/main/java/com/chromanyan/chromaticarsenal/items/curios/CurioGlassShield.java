package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import top.theillusivec4.curios.api.SlotContext;

@SuppressWarnings("all")
public class CurioGlassShield extends BaseCurioItem {
	
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
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING) {
			return true;
		} else {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}
	}

}
