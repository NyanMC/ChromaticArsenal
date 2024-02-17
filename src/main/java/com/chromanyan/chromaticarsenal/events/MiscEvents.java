package com.chromanyan.chromaticarsenal.events;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.items.curios.CurioAdvancingHeart;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AdvancementEvent.AdvancementEarnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

public class MiscEvents {

    private final Common config = ModConfig.COMMON;

    @SubscribeEvent
    public void playerAttackedEvent(LivingHurtEvent event) {
        LivingEntity player = event.getEntity();
        if (player.getCommandSenderWorld().isClientSide()) return;

        // spatial: block fall damage
        if (player.hasEffect(ModEffects.SPATIAL.get()) && event.getSource() == DamageSource.FALL) {
            event.setAmount(0); // just in case, you know?
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void playerGotAdvancement(AdvancementEarnEvent event) {
        Player player = event.getEntity();

        Optional<SlotResult> slotResult = ChromaCurioHelper.getCurio(player, ModItems.ADVANCING_HEART.get());
        if (slotResult.isEmpty()) return;

        CurioAdvancingHeart.updateNBTForStack(slotResult.get().slotContext(), slotResult.get().stack());
    }

    @SubscribeEvent
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
    }
}
