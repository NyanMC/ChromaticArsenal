package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModTags;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.ConfigHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurioDispellingCrystal extends BaseSuperCurio {

    public CurioDispellingCrystal() {
        super(ModItems.WARD_CRYSTAL, SoundEvents.AMETHYST_BLOCK_PLACE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!Screen.hasShiftDown()) return;
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.1", TooltipHelper.multiplierAsPercentTooltip(config.antiMagicMultiplierIncoming.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.2", TooltipHelper.multiplierAsPercentTooltip(config.antiMagicMultiplierOutgoing.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_ward_crystal.1", TooltipHelper.multiplierAsPercentTooltip(config.potionDurationMultiplier.get())));
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().is(ModTags.DamageTypes.IMMUNE_TO_WARD_CRYSTAL)) return;

        if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO) && !ChromaCurioHelper.shouldIgnoreDamageEvent(event)) {
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierIncoming.get()));
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().is(ModTags.DamageTypes.IMMUNE_TO_WARD_CRYSTAL)) return;

        if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO) && !ChromaCurioHelper.shouldIgnoreDamageEvent(event)) {
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierOutgoing.get()));
        }
    }

    @Override
    public void onPotionApplied(MobEffectEvent.Added event) {
        if (ConfigHelper.effectInBlacklist(config.effectBlacklist.get(), event.getEffectInstance().getEffect())) return;

        event.getEffectInstance().duration *= config.potionDurationMultiplier.get(); // because why should forge let you set the duration of a potion effect without an access transformer?
    }
}
