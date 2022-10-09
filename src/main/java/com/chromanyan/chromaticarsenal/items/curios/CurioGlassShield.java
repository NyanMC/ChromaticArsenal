package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
@SuppressWarnings("all")
public class CurioGlassShield extends BaseCurioItem {
	
	@Override
	public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		if (livingEntity.level.isClientSide) {
			return;
		}
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt.contains("counter") && nbt.getInt("counter") > 0) {
			nbt.putInt("counter", nbt.getInt("counter") - 1);
			if (nbt.getInt("counter") == 0)
				livingEntity.getCommandSenderWorld().playSound((PlayerEntity) null, livingEntity.blockPosition(), SoundEvents.GLASS_PLACE, SoundCategory.PLAYERS, 0.5F, 1.0F);
		}
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
