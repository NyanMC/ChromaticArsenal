package com.chromanyan.chromaticarsenal.events;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CurioEvents {

    private final ModConfig.Common config = ModConfig.COMMON;

    @SubscribeEvent
    public void playerAttackedEvent(LivingHurtEvent event) {
        LivingEntity player = event.getEntity();
        if (player.getCommandSenderWorld().isClientSide()) return;
        if (event.isCanceled()) return;


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

    @SubscribeEvent
    public void potionImmunityEvent(MobEffectEvent.Applicable event) {
        LivingEntity player = event.getEntity();
        if (player.getCommandSenderWorld().isClientSide() || !config.potionImmunitySideCheck.get()) return;
        if (event.getResult() == Event.Result.DENY) return;

        for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
            if (stack.getItem() instanceof IChromaCurio chromaStack) {
                chromaStack.onGetImmunities(event, stack, event.getEffectInstance().getEffect());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    // CA revives should take effect after most other revivals to avoid diamond heart anti-synergy, but shouldn't take effect last either
    public void playerDeathEvent(LivingDeathEvent event) {
        if (event.isCanceled()) return;
        LivingEntity entity = event.getEntity();
        if (entity.getCommandSenderWorld().isClientSide()) return;

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
