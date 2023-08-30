package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioGoldenHeart extends BaseCurioItem {

    private final Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.golden_heart.1", TooltipHelper.potionAmplifierTooltip(config.absorptionLevel.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.golden_heart.2", TooltipHelper.ticksToSecondsTooltip(getEffectCooldown(stack))));
        list.add(Component.translatable("tooltip.chromaticarsenal.golden_heart.3"));
        if (ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.golden_heart.twisted", TooltipHelper.ticksToSecondsTooltip((config.twistedWitherDuration.get()))));
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!stack.getOrCreateTag().contains("is_chromanyan")) {
            if (entity instanceof Player) {
                if (entity.getUUID().toString().equalsIgnoreCase("854adc0b-ae55-48d6-b7ba-e641a1eebf42"))
                    stack.getOrCreateTag().putInt("is_chromanyan", 1);
                else
                    stack.getOrCreateTag().putInt("is_chromanyan", 0);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerVariants() {
        ItemProperties.register(ModItems.GOLDEN_HEART.get(), new ResourceLocation(Reference.MODID, "is_chromanyan"), (stack, world, entity, thing) -> stack.getOrCreateTag().getInt("is_chromanyan"));
    }

    private int getEffectCooldown(ItemStack stack) {
        return Math.max(1, config.absorptionDuration.get() - (stack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) * config.enchantmentAbsorptionReduction.get()));
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
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (ChromaCurioHelper.isChromaticTwisted(stack, player)) {
            if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity livingAttacker && event.getAmount() > 0) {
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, config.twistedWitherDuration.get(), 0));
                livingAttacker.addEffect(new MobEffectInstance(MobEffects.WITHER, config.twistedWitherDuration.get(), 0));
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid,
                                                                        ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        double attModBonus = stack.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION) * config.enchantmentMaxHealthIncrease.get();
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

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GOLD, 0.5F, 1);
    }
}
