package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class CurioShieldOfUndying extends BaseSuperCurio {

    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioShieldOfUndying() {
        super(ModItems.GLASS_SHIELD);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.super_glass_shield.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_glass_shield.2", "§b" + config.shatterRevivalCooldown.get()));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_glass_shield.3", "§b" + config.revivalLimit.get()));
        CompoundTag nbt = stack.getOrCreateTag();
        if (!CooldownHelper.isCooldownFinished(nbt)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.cooldown", CooldownHelper.getCounter(nbt)).withStyle(ChatFormatting.GRAY));
        }
    }

    @SuppressWarnings("all")
    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        if (livingEntity.level.isClientSide) {
            return;
        }
        CompoundTag nbt = stack.getOrCreateTag();
        CooldownHelper.tickCounter(nbt, SoundEvents.GLASS_PLACE, livingEntity);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth()); // to be honest i have no clue if this will even do anything, depends on if onUnequip processes before or after attribute changes
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid,
                                                                        ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, Reference.MODID + ":undying_health_tradeoff", ModConfig.COMMON.healthTradeoff.get(), AttributeModifier.Operation.fromValue(2)));
        return atts;
    }

    @Override
    public void onWearerDied(LivingDeathEvent event, ItemStack stack, LivingEntity player) {
        if (!event.getSource().isBypassInvul()) {
            CompoundTag nbt = stack.getOrCreateTag();
            if (CooldownHelper.getCounter(nbt) < config.revivalLimit.get()) {
                CooldownHelper.addToCounter(nbt, config.shatterRevivalCooldown.get());
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
                player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
                if (CooldownHelper.getCounter(nbt) < config.revivalLimit.get()) {
                    player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F); // player has minimum one more revive left, let them know that
                }
            }
        }
    }
}
