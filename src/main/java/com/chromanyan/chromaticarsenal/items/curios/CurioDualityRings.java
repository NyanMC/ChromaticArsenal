package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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

import java.util.List;

public class CurioDualityRings extends BaseCurioItem {

    public CurioDualityRings() {
        super(SoundEvents.ARROW_HIT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!Screen.hasShiftDown()) return;
        TooltipHelper.itemTooltipLine(stack, 1, list);
        TooltipHelper.itemTooltipLine(stack, 2, list, TooltipHelper.multiplierAsPercentTooltip(getProjectileMultiplier(stack)));
        if (ChromaCurioHelper.isChromaticTwisted(stack, Minecraft.getInstance().player))
            TooltipHelper.itemTooltipLine(stack, "twisted", list, TooltipHelper.ticksToSecondsTooltip(config.twistedSaturationDuration.get()), TooltipHelper.potionAmplifierTooltip(config.twistedHungerLevel.get()));
    }

    private double getProjectileMultiplier(ItemStack stack) {
        return config.aroOfClubsMultiplier.get() + (config.powerArrowsMultiplierBonus.get() * stack.getEnchantmentLevel(Enchantments.POWER_ARROWS));
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            event.setAmount((float) (event.getAmount() * getProjectileMultiplier(stack)));
        }
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity entity = context.entity();
        if (entity.getCommandSenderWorld().isClientSide)
            return;
        if (ChromaCurioHelper.isChromaticTwisted(stack, entity)) {
            if (!entity.hasEffect(MobEffects.SATURATION)) {
                entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 25, config.twistedHungerLevel.get()), entity);
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.POWER_ARROWS) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }
}
