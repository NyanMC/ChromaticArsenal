package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class CurioIlluminatedSoul extends BaseSuperCurio {

    private static final ModConfig.Common config = ModConfig.COMMON;

    public CurioIlluminatedSoul() {
        super(null);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("tooltip.chromaticarsenal.super_glow_ring.1"));
        if (config.glowingDuration.get() > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.super_glow_ring.2", TooltipHelper.ticksToSecondsTooltip(config.glowingDuration.get())));
        if (config.illuminatedUndeadMultiplier.get() > 1)
            list.add(Component.translatable("tooltip.chromaticarsenal.super_glow_ring.3", TooltipHelper.multiplierAsPercentTooltip(config.illuminatedUndeadMultiplier.get())));
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
        if (entity.getCommandSenderWorld().isClientSide)
            return;
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
        if (config.glowingDuration.get() > 0)
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, config.glowingDuration.get()));
        if (target.getMobType() == MobType.UNDEAD)
            event.setAmount(event.getAmount() * config.illuminatedUndeadMultiplier.get().floatValue());
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.5F, 1);
    }
}
