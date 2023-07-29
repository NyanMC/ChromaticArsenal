package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModStats;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;


public class CurioGlassShield extends BaseCurioItem {

    private final Random rand = new Random();
    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.glass_shield.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.glass_shield.2", "§b" + (((float) getCooldownDuration(stack)) / 20)));
        if (getFreeBlockChance(stack) > 0) {
            list.add(Component.translatable("tooltip.chromaticarsenal.glass_shield.3", "§b" + getFreeBlockChance(stack)));
        }
        CompoundTag nbt = stack.getOrCreateTag();
        if (!CooldownHelper.isCooldownFinished(nbt)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.cooldown", CooldownHelper.getCounter(nbt)).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        if (livingEntity.level.isClientSide) {
            return;
        }
        CompoundTag nbt = stack.getOrCreateTag();
        CooldownHelper.tickCounter(nbt, SoundEvents.GLASS_PLACE, livingEntity);
    }

    private int getFreeBlockChance(ItemStack stack) {
        if (stack.getEnchantmentLevel(Enchantments.UNBREAKING) > 0) {
            return (int) Math.ceil(stack.getEnchantmentLevel(Enchantments.UNBREAKING) * config.enchantmentFreeBlockChance.get());
        }
        return 0;
    }

    private int getCooldownDuration(ItemStack stack) {
        int savedTicks = 0; // if you can name a better way to write this without running into "reassigned local variable" i'm all ears
        if (stack.getEnchantmentLevel(Enchantments.MENDING) > 0) {
            savedTicks = stack.getEnchantmentLevel(Enchantments.MENDING) * config.enchantmentCooldownReduction.get(); // i doubt this will ever be > 1, but maybe some mod uses mixins to increase it. i want to be ready for that.
        }
        return config.cooldownDuration.get() - savedTicks;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getAmount() != 0 && !event.getSource().isBypassInvul()) {
            CompoundTag nbt = stack.getOrCreateTag();
            if (CooldownHelper.isCooldownFinished(nbt)) {
                int randBlock = rand.nextInt(99);
                if (randBlock < getFreeBlockChance(stack)) { // not <= because rand.nextInt is always one less than i want it to be
                    player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                    player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
                } else {
                    CooldownHelper.updateCounter(nbt, getCooldownDuration(stack));
                    player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
                }
                if (player instanceof Player playerEntity) {
                    int hitDamage = Math.round(event.getAmount() * 10.0F);
                    playerEntity.awardStat(ModStats.GSHIELD_TOTAL_BLOCK, hitDamage);
                }
                event.setAmount(0);
                event.setCanceled(true);
            }
        }
    }
}
