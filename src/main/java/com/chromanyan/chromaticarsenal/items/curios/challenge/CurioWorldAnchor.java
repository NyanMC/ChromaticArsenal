package com.chromanyan.chromaticarsenal.items.curios.challenge;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioWorldAnchor extends BaseCurioItem {

    private static final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.world_anchor.1"));
        if (getFortuneLevel(stack) > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.world_anchor.2", TooltipHelper.valueTooltip(getFortuneLevel(stack))));
        else
            list.add(Component.translatable("tooltip.chromaticarsenal.world_anchor.2alt"));
        if (stack.getEnchantmentLevel(ModEnchantments.CHROMATIC_TWISTING.get()) > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.world_anchor.twisted", TooltipHelper.valueTooltip(config.twistedAnchorGravityMultiplier.get())));
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        if (config.anchorSoulbound.get()) {
            return ICurio.DropRule.ALWAYS_KEEP;
        } else {
            return ICurio.DropRule.DEFAULT;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        LivingEntity entity = slotContext.entity();
        if (entity == null) {
            ChromaticArsenal.LOGGER.warn("Tried to get attribute modifiers for world anchor but entity was null");
            return atts; // should hopefully fix a NPE when reloading resources with F3+T
        }
        Level level = entity.getLevel();
        double relativeY = entity.getY() - level.getMinBuildHeight(); // the entity's y position relative to the bottom of the world, e.g. y position + 64 in the overworld
        int worldHeight = level.getMaxBuildHeight() - level.getMinBuildHeight();
        double gravityMod;

        if (relativeY > worldHeight) {
            gravityMod = 1;
        } else if (relativeY > 0) {
            gravityMod = relativeY / worldHeight;
        } else {
            gravityMod = 0;
        }

        if (stack.getEnchantmentLevel(ModEnchantments.CHROMATIC_TWISTING.get()) > 0) {
            gravityMod *= config.twistedAnchorGravityMultiplier.get();
        }

        atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, Reference.MODID + ":world_anchor_gravity", gravityMod * config.anchorGravityMultiplier.get(), AttributeModifier.Operation.MULTIPLY_BASE));
        atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":world_anchor_speed", gravityMod * config.anchorSpeedMultiplier.get(), AttributeModifier.Operation.MULTIPLY_BASE));
        atts.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, Reference.MODID + ":world_anchor_kbresist", gravityMod * config.anchorKnockbackResistanceMultiplier.get(), AttributeModifier.Operation.ADDITION));
        atts.put(Attributes.ARMOR, new AttributeModifier(uuid, Reference.MODID + ":world_anchor_armor", config.anchorArmor.get(), AttributeModifier.Operation.ADDITION));

        return atts;
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        return getFortuneLevel(stack);
    }

    private static int getFortuneLevel(ItemStack stack) {
        return stack.getEnchantmentLevel(ModEnchantments.CHROMATIC_TWISTING.get()) + stack.getEnchantmentLevel(Enchantments.BINDING_CURSE);
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ANVIL_LAND, 0.5F, 1);
    }
}
