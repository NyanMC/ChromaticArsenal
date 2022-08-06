package com.chromanyan.chromaticarsenal.items.base;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BaseCurioItem extends Item implements ICurioItem {

	public BaseCurioItem() {
		super(new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1).rarity(Rarity.RARE).defaultDurability(0));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
		return !CuriosApi.getCuriosHelper().findEquippedCurio(this, livingEntity).isPresent();
	}

}
