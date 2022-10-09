package com.chromanyan.chromaticarsenal.items.base;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BaseCurioItem extends Item implements ICurioItem {

	public BaseCurioItem() {
		super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(1).rarity(Rarity.RARE).defaultDurability(0));
	}
	
	@Override
	public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
		return !CuriosApi.getCuriosHelper().findEquippedCurio(this, livingEntity).isPresent();
	}
	
	@Override
	public boolean isEnchantable(ItemStack p_77616_1_) {
		return true;
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.BINDING_CURSE || enchantment == Enchantments.VANISHING_CURSE) {
			return true;
		} else {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}
	}

}
