package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioBubbleAmulet extends BaseCurioItem {
    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        CompoundTag nbt = stack.getOrCreateTag();
        list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.1").withStyle(ChatFormatting.GRAY));
        if (config.bubblePanicDuration.get() > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.2", TooltipHelper.ticksToSecondsTooltip(config.bubblePanicDuration.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.3", TooltipHelper.ticksToSecondsTooltip(getCooldownDuration(stack))));
        if (!CooldownHelper.isCooldownFinished(nbt)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.cooldown", CooldownHelper.getCounter(nbt)).withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.cooldown", CooldownHelper.getCounter(nbt)).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();

        if (living.level.isClientSide) {
            return; // the server will replicate our changes to the client for us
        }

        int airSupply = living.getAirSupply();
        int maxAirSupply = living.getMaxAirSupply();

        if (airSupply <= 0) {
            CompoundTag nbt = stack.getOrCreateTag();
            if (!CooldownHelper.isCooldownFinished(nbt))
                return;

            living.getCommandSenderWorld().playSound(null, living.blockPosition(), SoundEvents.BOTTLE_EMPTY,
                    SoundSource.PLAYERS, 0.5F, 1.0F);
            living.setAirSupply(maxAirSupply);
            CooldownHelper.updateCounter(nbt, getCooldownDuration(stack));
            living.addEffect(new MobEffectInstance(ModPotions.BUBBLE_PANIC.get(), config.bubblePanicDuration.get()), living);
        } else if (airSupply >= maxAirSupply) {
            CompoundTag nbt = stack.getOrCreateTag();
            if (CooldownHelper.isCooldownFinished(nbt))
                return;

            CooldownHelper.tickCounter(nbt, SoundEvents.BOTTLE_FILL, living);
        }
    }

    private int getCooldownDuration(ItemStack stack) {
        return config.baseBubbleCooldown.get() - (config.respirationCooldownReduction.get() * stack.getEnchantmentLevel(Enchantments.RESPIRATION));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":bubble_swimming", config.amuletSwimSpeed.get(), AttributeModifier.Operation.MULTIPLY_BASE));
        return atts;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.RESPIRATION) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.BUCKET_FILL, 0.5F, 1);
    }
}
