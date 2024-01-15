package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioShadowTreads extends BaseCurioItem {

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.2", TooltipHelper.potionAmplifierTooltip(config.darkspeedPotency.get()), TooltipHelper.valueTooltip(config.maxLightLevel.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.3"));
        if (stack.getEnchantmentLevel(Enchantments.SWIFT_SNEAK) > 0 && config.swiftSneakDetectionReduction.get() > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.swift_sneak", TooltipHelper.percentTooltip(stack.getEnchantmentLevel(Enchantments.SWIFT_SNEAK) * config.swiftSneakDetectionReduction.get())));
        if (ChromaCurioHelper.isChromaticTwisted(stack, null))
            list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.twisted", TooltipHelper.percentTooltip(config.twistedShadowDodgeChance.get())));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        Level world = livingEntity.getCommandSenderWorld();
        if (!world.isClientSide()) {
            if (world.getMaxLocalRawBrightness(livingEntity.blockPosition()) <= config.maxLightLevel.get()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 25, config.darkspeedPotency.get(), true, true), livingEntity);
            } else if (ChromaCurioHelper.isChromaticTwisted(stack, context.entity())) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 105, 0, true, true), livingEntity);
            }
        }
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (ChromaCurioHelper.isChromaticTwisted(stack, player) && !ChromaCurioHelper.shouldIgnoreDamageEvent(event)) {
            Level world = player.getCommandSenderWorld(); // we already checked that this is serverside back in the event class
            if (world.getMaxLocalRawBrightness(player.blockPosition()) <= config.maxLightLevel.get() && Math.random() < config.twistedShadowDodgeChance.get()) {
                player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.8f, 3f);
                event.setAmount(0);
                event.setCanceled(true);
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        if (stack.getEnchantmentLevel(Enchantments.SOUL_SPEED) > 0) {
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":speed_bonus", EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SOUL_SPEED, stack) * config.enchantmentSpeedMultiplier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return atts;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.SOUL_SPEED || enchantment == Enchantments.SWIFT_SNEAK) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public void onGetImmunities(MobEffectEvent.Applicable event, ItemStack stack, MobEffect effect) {
        if (event.getEffectInstance().getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
            event.setResult(Event.Result.DENY);
        }
    }

    @Override
    public void onGetVisibility(LivingEvent.LivingVisibilityEvent event, ItemStack stack) {
        int swiftSneakLevel = stack.getEnchantmentLevel(Enchantments.SWIFT_SNEAK);
        double swiftSneakReduction = config.swiftSneakDetectionReduction.get();
        if (swiftSneakLevel > 0 && swiftSneakReduction > 0) {
            event.modifyVisibility(1 - (swiftSneakLevel * swiftSneakReduction));
        }
    }

    @Override
    public void onVanillaEvent(VanillaGameEvent event, ItemStack stack, LivingEntity player) {
        if (event.getVanillaEvent() == GameEvent.STEP || event.getVanillaEvent() == GameEvent.HIT_GROUND) {
            if (player.fallDistance < 3.0F && !player.isSprinting()) {
                event.setCanceled(true);
            }
        }
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.SOUL_ESCAPE, 1, 1);
    }
}
