package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioShadowTreads extends BaseCurioItem {

    private final Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.2", "§b" + (config.darkspeedPotency.get() + 1), "§b" + config.maxLightLevel.get()));
        list.add(Component.translatable("tooltip.chromaticarsenal.shadow_treads.3"));
    }

    @SuppressWarnings("all")
    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        Level world = livingEntity.getCommandSenderWorld();
        if (world != null && !world.isClientSide()) {
            if (world.getMaxLocalRawBrightness(livingEntity.blockPosition()) <= config.maxLightLevel.get()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 25, config.darkspeedPotency.get(), true, true));
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SOUL_SPEED, stack) > 0) {
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":speed_bonus", EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SOUL_SPEED, stack) * config.enchantmentSpeedMultiplier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return atts;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.SOUL_SPEED) {
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
