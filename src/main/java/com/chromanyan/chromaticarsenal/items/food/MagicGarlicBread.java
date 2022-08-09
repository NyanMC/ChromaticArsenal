package com.chromanyan.chromaticarsenal.items.food;

import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;

public class MagicGarlicBread extends Item {
	Common config = ModConfig.COMMON;
	public MagicGarlicBread() {
		super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(64).rarity(Rarity.RARE).food(new Food.Builder().nutrition(10).saturationMod(0.7F).alwaysEat().build()));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity player) {
		if (!world.isClientSide) {
			Optional<ImmutableTriple<String, Integer, ItemStack>> rings = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.DUALITY_RINGS.get(), (LivingEntity) player);
			if (rings.isPresent()) {
				player.addEffect(new EffectInstance(Effects.SATURATION, config.saturationDuration.get(), config.saturationLevel.get(), true, true));
				player.addEffect(new EffectInstance(Effects.HEALTH_BOOST, config.healthBoostDuration.get(), config.healthBoostLevel.get(), true, true));
			}
		}
		return super.finishUsingItem(stack, world, player);
	}

}
