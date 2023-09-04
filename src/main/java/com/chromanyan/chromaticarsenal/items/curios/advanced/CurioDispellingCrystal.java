package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class CurioDispellingCrystal extends BaseSuperCurio {
    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioDispellingCrystal() {
        super(ModItems.WARD_CRYSTAL);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.1", TooltipHelper.multiplierAsPercentTooltip(config.antiMagicMultiplierIncoming.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.2", TooltipHelper.multiplierAsPercentTooltip(config.antiMagicMultiplierOutgoing.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_ward_crystal.1", TooltipHelper.multiplierAsPercentTooltip(config.potionDurationMultiplier.get())));
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().isMagic() && !event.getSource().isBypassInvul()) {
            if (!config.damageSourceBlacklist.get().isEmpty()) {
                for (String blacklisted : config.damageSourceBlacklist.get()) {
                    if (event.getSource().getMsgId().equals(blacklisted))
                        return;
                }
            }
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierIncoming.get()));
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().isMagic() && !event.getSource().isBypassInvul()) {
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierOutgoing.get()));
        }
    }

    @Override
    public void onPotionApplied(MobEffectEvent.Added event) {
        event.getEffectInstance().duration *= config.potionDurationMultiplier.get(); // because why should forge let you set the duration of a potion effect without an access transformer?
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.AMETHYST_BLOCK_PLACE, 0.5F, 1);
    }
}
