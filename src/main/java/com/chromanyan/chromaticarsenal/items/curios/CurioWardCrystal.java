package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurioWardCrystal extends BaseCurioItem {

    public CurioWardCrystal() {
        super(SoundEvents.AMETHYST_BLOCK_PLACE);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.1", TooltipHelper.multiplierAsPercentTooltip(getIncomingMultiplier(stack))));
        if (!ChromaCurioHelper.isChromaticTwisted(stack, null))
            list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.2", TooltipHelper.multiplierAsPercentTooltip(getOutgoingMultiplier(stack))));
        else
            list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.twisted", TooltipHelper.ticksToSecondsTooltip(config.twistedWeaknessDuration.get())));
    }

    private float getIncomingMultiplier(ItemStack stack) {
        float mult = config.antiMagicMultiplierIncoming.get().floatValue();
        int protLevel = stack.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION);
        return Math.max(0, mult - (protLevel * config.antiMagicProtectionModifier.get().floatValue()));
    }

    private float getOutgoingMultiplier(ItemStack stack) {
        float mult = config.antiMagicMultiplierOutgoing.get().floatValue();
        int protLevel = stack.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION);
        return Math.max(0, mult - (protLevel * config.antiMagicProtectionModifier.get().floatValue()));
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().isMagic() && !ChromaCurioHelper.shouldIgnoreDamageEvent(event)) {
            if (!config.damageSourceBlacklist.get().isEmpty()) {
                for (String blacklisted : config.damageSourceBlacklist.get()) {
                    if (event.getSource().getMsgId().equals(blacklisted))
                        return;
                }
            }
            event.setAmount(event.getAmount() * getIncomingMultiplier(stack));
        } else if (ChromaCurioHelper.isChromaticTwisted(stack, player) && !ChromaCurioHelper.shouldIgnoreDamageEvent(event)) {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, config.twistedWeaknessDuration.get(), 1), player);
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().isMagic() && !ChromaCurioHelper.shouldIgnoreDamageEvent(event) && !ChromaCurioHelper.isChromaticTwisted(stack, player)) {
            event.setAmount(event.getAmount() * getOutgoingMultiplier(stack));
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.ALL_DAMAGE_PROTECTION) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }
}
