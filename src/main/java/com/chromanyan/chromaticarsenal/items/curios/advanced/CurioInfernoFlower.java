package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
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

import java.util.List;

public class CurioInfernoFlower extends BaseSuperCurio {

    public CurioInfernoFlower() {
        super(ModItems.FRIENDLY_FIRE_FLOWER, SoundEvents.FIRECHARGE_USE);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("tooltip.chromaticarsenal.super_friendly_fire_flower.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_friendly_fire_flower.2"));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_friendly_fire_flower.3", TooltipHelper.multiplierAsPercentTooltip(config.viciousFireDamageMultplier.get())));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();

        if (living.getCommandSenderWorld().isClientSide) {
            return;
        }

        living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, true, true));
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (!event.getSource().isProjectile()) {
            target.addEffect(new MobEffectInstance(ModEffects.INFERNO.get(), config.infernoDuration.get()));
        }

        if (target.isOnFire()) {
            event.setAmount(event.getAmount() * config.viciousFireDamageMultplier.get().floatValue());
        }
    }
}
