package com.chromanyan.chromaticarsenal.items.curios;

import java.util.UUID;

import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CurioLunarCrystal extends BaseCurioItem {
	@Override
	public ITextComponent getName(ItemStack stack) {
		if (stack.getOrCreateTag().getString("crafter.id").equalsIgnoreCase("854adc0b-ae55-48d6-b7ba-e641a1eebf42")) {
			return new TranslationTextComponent(this.getDescriptionId(stack) + ".luna");
		}
		return super.getName(stack);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);
		if (stack.getOrCreateTag().getString("crafter.id").isEmpty()) {
			if (entity instanceof PlayerEntity) {
				if (((PlayerEntity) entity).getUUID() != null) {
					stack.getOrCreateTag().putString("crafter.id", ((PlayerEntity) entity).getUUID().toString());
				}
			}
		} else {
			final PlayerEntity player = entity.level.getPlayerByUUID(UUID.fromString(stack.getOrCreateTag().getString("crafter.id")));
			if ((player != null) && stack.getOrCreateTag().getString("crafter.name").isEmpty()) {
				stack.getOrCreateTag().putString("crafter.name", player.getDisplayName().getString());
			}
		}
	}
}
