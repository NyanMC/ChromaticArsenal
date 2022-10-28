package com.chromanyan.chromaticarsenal.items.curios;

import java.util.UUID;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

@SuppressWarnings("all")
public class CurioLunarCrystal extends BaseCurioItem {

	final ModConfig.Common config = ModConfig.COMMON;

	@Override
	public Component getName(ItemStack stack) {
		if (stack.getOrCreateTag().getString("crafter.id").equalsIgnoreCase("854adc0b-ae55-48d6-b7ba-e641a1eebf42") || config.everyoneIsLuna.get()) {
			return new TranslatableComponent(this.getDescriptionId(stack) + ".luna");
		}
		return super.getName(stack);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);
		if (stack.getOrCreateTag().getString("crafter.id").isEmpty()) {
			if (entity instanceof Player) {
				if (((Player) entity).getUUID() != null) {
					stack.getOrCreateTag().putString("crafter.id", ((Player) entity).getUUID().toString());
				}
			}
		} else {
			final Player player = entity.level.getPlayerByUUID(UUID.fromString(stack.getOrCreateTag().getString("crafter.id")));
			if ((player != null) && stack.getOrCreateTag().getString("crafter.name").isEmpty()) {
				stack.getOrCreateTag().putString("crafter.name", player.getDisplayName().getString());
			}
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.FALL_PROTECTION) {
			return true;
		} else {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
		atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, Reference.MODID + ":defy_gravity", config.gravityModifier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
		return atts;
	}
}
