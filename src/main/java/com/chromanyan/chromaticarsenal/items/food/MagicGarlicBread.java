package com.chromanyan.chromaticarsenal.items.food;

import java.util.Optional;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;

import top.theillusivec4.curios.api.CuriosApi;

public class MagicGarlicBread extends Item {
	final Common config = ModConfig.COMMON;
	public MagicGarlicBread() {
		super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(64).rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(10).saturationMod(0.7F).alwaysEat().build()));
	}

	@SuppressWarnings("all")
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity player) {
		if (!world.isClientSide) {
			Optional<ImmutableTriple<String, Integer, ItemStack>> rings = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.DUALITY_RINGS.get(), (LivingEntity) player);
			if (rings.isPresent()) {
				player.addEffect(new MobEffectInstance(MobEffects.SATURATION, config.saturationDuration.get(), config.saturationLevel.get(), true, true));
				player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, config.healthBoostDuration.get(), config.healthBoostLevel.get(), true, true));
			}
		}
		return super.finishUsingItem(stack, world, player);
	}

}
