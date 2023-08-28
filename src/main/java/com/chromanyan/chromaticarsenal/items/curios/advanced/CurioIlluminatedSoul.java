package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CurioIlluminatedSoul extends BaseSuperCurio {
    public CurioIlluminatedSoul() {
        super(null);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.getCommandSenderWorld().isClientSide) {
            entity.setGlowingTag(true);
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 410, 0, false, false));
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        entity.removeEffect(MobEffects.NIGHT_VISION);
        entity.setGlowingTag(false);
    }

    @Override
    public void onGetImmunities(MobEffectEvent.Applicable event, ItemStack stack, MobEffect effect) {
        if (event.getEffectInstance().getEffect() == MobEffects.BLINDNESS || event.getEffectInstance().getEffect() == MobEffects.DARKNESS) {
            event.setResult(Event.Result.DENY);
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1200));
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.5F, 1);
    }
}
