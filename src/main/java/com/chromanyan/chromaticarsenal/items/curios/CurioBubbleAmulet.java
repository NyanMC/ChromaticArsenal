package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class CurioBubbleAmulet extends BaseCurioItem {
    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        //TODO nyan please add details
        CompoundTag nbt = stack.getOrCreateTag();
        if (!CooldownHelper.isCooldownFinished(nbt)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.cooldown", CooldownHelper.getCounter(nbt)).withStyle(ChatFormatting.GRAY));
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
            //TODO configurability
            living.addEffect(new MobEffectInstance(ModPotions.BUBBLE_PANIC.get(), 1200), living);
        } else if (airSupply >= maxAirSupply) {
            CompoundTag nbt = stack.getOrCreateTag();
            if (CooldownHelper.isCooldownFinished(nbt))
                return;

            CooldownHelper.tickCounter(nbt, SoundEvents.BOTTLE_FILL, living);
        }
    }

    private int getCooldownDuration(ItemStack stack) {
        //TODO configurability, maybe make respiration enchant reduce the cooldown?
        return 2400; // 2 minutes
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        //TODO make swim speed attribute here configurable
        atts.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":bubble_swimming", 0.5, AttributeModifier.Operation.MULTIPLY_BASE));
        return atts;
    }
}
