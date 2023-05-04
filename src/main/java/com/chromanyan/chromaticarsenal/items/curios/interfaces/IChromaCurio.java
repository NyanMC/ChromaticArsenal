package com.chromanyan.chromaticarsenal.items.curios.interfaces;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;

public interface IChromaCurio {

    default void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {

    }

    default void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {

    }

    default void onGetImmunities(MobEffectEvent.Applicable event, ItemStack stack, MobEffect effect) {

    }

    default void onWearerDied(LivingDeathEvent event, ItemStack stack, LivingEntity player) {

    }

    default void onVanillaEvent(VanillaGameEvent event, ItemStack stack, LivingEntity player) {

    }

    default void onPotionApplied(MobEffectEvent.Added event) {

    }
}
