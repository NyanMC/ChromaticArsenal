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
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class CurioDualityRings extends BaseCurioItem {
    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.duality_rings.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.duality_rings.2", "§b" + Math.round((config.aroOfClubsMultiplier.get() - 1) * 100)));
        if (ChromaCurioHelper.isChromaticTwisted(stack, null))
            list.add(Component.translatable("tooltip.chromaticarsenal.duality_rings.twisted", "§b" + (config.twistedSaturationDuration.get() / 20), "§b" + (config.twistedHungerLevel.get() + 1)));
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().isProjectile()) {
            event.setAmount((float) (event.getAmount() * config.aroOfClubsMultiplier.get()));
        }
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        if (ChromaCurioHelper.isChromaticTwisted(stack, context.entity())) {
            LivingEntity entity = context.entity();
            if (!entity.hasEffect(MobEffects.SATURATION)) {
                entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 25, config.twistedHungerLevel.get()));
            }
        }
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARROW_HIT, 0.5F, 1);
    }
}
