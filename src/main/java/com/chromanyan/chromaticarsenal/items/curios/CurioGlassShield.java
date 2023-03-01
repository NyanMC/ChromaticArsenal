package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

@SuppressWarnings("all")
public class CurioGlassShield extends BaseCurioItem {

    private final Random rand = new Random();
    private final ModConfig.Common config = ModConfig.COMMON;

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
            int savedTicks = 0;
            int freeBlockChance = 0;
            if (getItemEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
                savedTicks = getItemEnchantmentLevel(Enchantments.MENDING, stack) * config.enchantmentCooldownReduction.get(); // i doubt this will ever be > 1, but maybe some mod uses mixins to increase it. i want to be ready for that.
            }
            if (getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) > 0) {
                freeBlockChance = (int) Math.ceil(getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) * config.enchantmentFreeBlockChance.get());
            }
            if (CooldownHelper.isCooldownFinished(nbt)) {
                int randBlock = rand.nextInt(99);
                if (randBlock < freeBlockChance) { // not <= because rand.nextInt is always one less than i want it to be
                    player.getCommandSenderWorld().playSound((Player) null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                    player.getCommandSenderWorld().playSound((Player) null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
                } else {
                    CooldownHelper.updateCounter(nbt, config.cooldownDuration.get() - savedTicks);
                    player.getCommandSenderWorld().playSound((Player) null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
                }
                event.setAmount(0);
                event.setCanceled(true);
            }
        }
    }
}
