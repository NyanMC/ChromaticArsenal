package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

public class CurioGoldenHeart extends BaseCurioItem {

    private final Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(new TranslatableComponent("tooltip.chromaticarsenal.golden_heart.1", "§b" + (config.absorptionLevel.get() + 1)));
        list.add(new TranslatableComponent("tooltip.chromaticarsenal.golden_heart.2", "§b" + (getEffectCooldown(stack) / 20)));
        list.add(new TranslatableComponent("tooltip.chromaticarsenal.golden_heart.3"));
    }

    private int getEffectCooldown(ItemStack stack) {
        return Math.max(1, config.absorptionDuration.get() - (getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, stack) * config.enchantmentAbsorptionReduction.get()));
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (!slotContext.entity().getCommandSenderWorld().isClientSide) {
            slotContext.entity().addEffect(new MobEffectInstance(MobEffects.ABSORPTION, (getEffectCooldown(stack) + 5), config.absorptionLevel.get(), true, true));
        }
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide && living.tickCount % getEffectCooldown(stack) == 0) {
            living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, (getEffectCooldown(stack) + 5), config.absorptionLevel.get(), true, true));
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        entity.removeEffect(MobEffects.ABSORPTION);
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth()); // to be honest i have no clue if this will even do anything, depends on if onUnequip processes before or after attribute changes
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid,
                                                                        ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        double attModBonus = getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, stack) * config.enchantmentMaxHealthIncrease.get();
        atts.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, Reference.MODID + ":health_bonus", config.maxHealthBoost.get() + attModBonus, AttributeModifier.Operation.fromValue(config.maxHealthBoostOperation.get())));
        return atts;
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true; // i dreamed hard, and now it works
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.ALL_DAMAGE_PROTECTION || enchantment == Enchantments.BLOCK_EFFICIENCY) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

}
