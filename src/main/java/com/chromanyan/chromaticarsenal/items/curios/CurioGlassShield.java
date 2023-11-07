package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModStats;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
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
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.Random;


public class CurioGlassShield extends BaseCurioItem {

    private final Random rand = new Random();
    private final ModConfig.Common config = ModConfig.COMMON;
    private static final DamageSource GLASS_SHRAPNEL = new DamageSource("chromaticarsenal.glass_shrapnel");

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.glass_shield.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.glass_shield.2", TooltipHelper.ticksToSecondsTooltip(getCooldownDuration(stack))));
        if (getFreeBlockChance(stack) > 0) {
            list.add(Component.translatable("tooltip.chromaticarsenal.glass_shield.3", TooltipHelper.valueTooltip(getFreeBlockChance(stack))));
        }
        if (ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.glass_shield.twisted", TooltipHelper.valueTooltip(config.twistedShatterDamageMultiplier.get())));
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
        // if the attack is already zeroed out or hurts creative players, we don't care about it
        if (event.getAmount() == 0 || event.getSource().isBypassInvul()) {
            return;
        }

        CompoundTag nbt = stack.getOrCreateTag();
        // if we're on cooldown and the player's shield is twisted, do more damage, return when on cooldown
        if (!CooldownHelper.isCooldownFinished(nbt)) {
            if (ChromaCurioHelper.isChromaticTwisted(stack, player)) {
                event.setAmount(event.getAmount() * config.twistedShatterDamageMultiplier.get().floatValue());
            }
            return;
        }

        // handle random chance to block damage with unbreaking
        int randBlock = rand.nextInt(99);
        if (randBlock < getFreeBlockChance(stack)) { // got the free block
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
        } else {
            CooldownHelper.updateCounter(nbt, getCooldownDuration(stack));
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
        }

        // if the shield is twisted, return the damage to the attacker if it exists
        if (ChromaCurioHelper.isChromaticTwisted(stack, player)) {
            if (event.getSource().getEntity() instanceof LivingEntity livingAttacker) {
                livingAttacker.hurt(GLASS_SHRAPNEL, event.getAmount());
            }
        }

        // handles the stat tracking how much damage the player has blocked with the glass shield
        if (player instanceof Player playerEntity) {
            int hitDamage = Math.round(event.getAmount() * 10.0F); // we need to multiply this by 10, for some reason
            playerEntity.awardStat(ModStats.GSHIELD_TOTAL_BLOCK, hitDamage);
        }
        event.setAmount(0);
        event.setCanceled(true);
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.GLASS_PLACE, 0.5F, 1);
    }
}
