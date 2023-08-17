package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
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
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class CurioWardCrystal extends BaseCurioItem {

    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.1", "§b" + (int) (100 * (1.0 - getIncomingMultiplier(stack)))));
        if (!ChromaCurioHelper.isChromaticTwisted(stack, null))
            list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.2", "§b" + (int) (100 * (1.0 - getOutgoingMultiplier(stack)))));
        else
            list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.twisted", "§b" + (config.twistedWeaknessDuration.get() / 20)));
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
        if (event.getSource().isMagic() && !event.getSource().isBypassInvul()) {
            event.setAmount(event.getAmount() * getIncomingMultiplier(stack));
        } else if (ChromaCurioHelper.isChromaticTwisted(stack, player) && event.getAmount() != 0 && !event.getSource().isBypassInvul()) {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, config.twistedWeaknessDuration.get(), 1));
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().isMagic() && !event.getSource().isBypassInvul() && !ChromaCurioHelper.isChromaticTwisted(stack, player)) {
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

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.AMETHYST_BLOCK_PLACE, 0.5F, 1);
    }
}
