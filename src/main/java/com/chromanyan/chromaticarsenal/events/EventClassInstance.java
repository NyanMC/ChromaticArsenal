package com.chromanyan.chromaticarsenal.events;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.items.curios.CurioAdvancingHeart;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AdvancementEvent.AdvancementEarnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

public class EventClassInstance {

    private final Common config = ModConfig.COMMON;

    @SubscribeEvent
    public void playerAttackedEvent(LivingHurtEvent event) {
        LivingEntity player = event.getEntity();
        if (!player.getCommandSenderWorld().isClientSide()) {
            // spatial: block fall damage
            if (player.hasEffect(ModEffects.SPATIAL.get()) && event.getSource() == DamageSource.FALL) {
                event.setAmount(0); // just in case, you know?
                event.setCanceled(true);
            }
            if (event.isCanceled()) {
                return; // the rest of the effects should only fire if they're even applicable
            }

            for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                if (stack.getItem() instanceof IChromaCurio chromaStack) {
                    chromaStack.onWearerHurt(event, stack, player);
                }
            }

            // attacker events
            Entity possibleAttacker = event.getSource().getEntity();
            if (possibleAttacker instanceof LivingEntity livingAttacker) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(livingAttacker)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onWearerAttack(event, stack, livingAttacker, player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void potionImmunityEvent(MobEffectEvent.Applicable event) {
        LivingEntity player = event.getEntity();
        if (player.getCommandSenderWorld().isClientSide() || !config.potionImmunitySideCheck.get()) return;
        if (event.getResult() == Result.DENY) return;

        for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
            if (stack.getItem() instanceof IChromaCurio chromaStack) {
                chromaStack.onGetImmunities(event, stack, event.getEffectInstance().getEffect());
            }
        }
    }

    @SubscribeEvent
    public void playerGotAdvancement(AdvancementEarnEvent event) {
        Player player = event.getEntity();

        Optional<SlotResult> slotResult = ChromaCurioHelper.getCurio(player, ModItems.ADVANCING_HEART.get());
        if (slotResult.isEmpty()) return;

        CurioAdvancingHeart.updateNBTForStack(slotResult.get().slotContext(), slotResult.get().stack());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    // CA revives should take effect after most other revivals to avoid diamond heart anti-synergy, but shouldn't take effect last either
    public void playerDeathEvent(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if (entity.getCommandSenderWorld().isClientSide()) {
            return;
        }

        // those damn creepers, allowing a mob to have both cursed revival and fractured
        if (entity.hasEffect(ModEffects.CURSED_REVIVAL.get())
                && !entity.hasEffect(ModEffects.FRACTURED.get())
                && !(entity instanceof Player)) {
            event.setCanceled(true);
            entity.removeEffect(ModEffects.CURSED_REVIVAL.get());
            entity.addEffect(new MobEffectInstance(ModEffects.FRACTURED.get(), 72000, config.cursedTotemFracturedLevel.get()));
            entity.setHealth(entity.getMaxHealth());
            entity.getCommandSenderWorld().playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.HOSTILE, 0.5F, 1.0F);
        }

        for (ItemStack stack : ChromaCurioHelper.getFlatStacks(entity)) {
            if (stack.getItem() instanceof IChromaCurio chromaStack) {
                chromaStack.onWearerDied(event, stack, entity);
            }
        }
    }

    @SubscribeEvent
    public void entityVisibilityEvent(LivingEvent.LivingVisibilityEvent event) {
        LivingEntity player = event.getEntity();

        if (!player.getCommandSenderWorld().isClientSide()) {
            for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                if (stack.getItem() instanceof IChromaCurio chromaStack) {
                    chromaStack.onGetVisibility(event, stack);
                }
            }
        }
    }

    @SubscribeEvent
    public void potionAppliedEvent(MobEffectEvent.Added event) {
        LivingEntity player = event.getEntity();

        if (!player.getCommandSenderWorld().isClientSide()) {
            for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                if (stack.getItem() instanceof IChromaCurio chromaStack) {
                    chromaStack.onPotionApplied(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void vanillaEvent(VanillaGameEvent event) {
        if (event.isCanceled()) {
            return;
        }
        Entity entity = event.getCause();
        if (entity instanceof LivingEntity player) {
            if (!player.getCommandSenderWorld().isClientSide()) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onVanillaEvent(event, stack, player);
                    }
                }
            }
        }
    }
}
