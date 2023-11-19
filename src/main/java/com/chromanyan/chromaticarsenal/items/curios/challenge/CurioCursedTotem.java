package com.chromanyan.chromaticarsenal.items.curios.challenge;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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

public class CurioCursedTotem extends BaseCurioItem {

    private static final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.challenge"));
        list.add(Component.translatable("tooltip.chromaticarsenal.cursed_totem.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.cursed_totem.2", TooltipHelper.percentTooltip((config.cursedTotemFracturedLevel.get() + 1) * 0.1F)));
        list.add(Component.translatable("tooltip.chromaticarsenal.cursed_totem.3", TooltipHelper.valueTooltip(config.cursedTotemBonusLooting.get())));
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        // attacking a player to give them a free revival would be cool, but also really cheesy. denied
        if (target instanceof Player || target.hasEffect(ModPotions.FRACTURED.get())) {
            return;
        }

        target.addEffect(new MobEffectInstance(ModPotions.CURSED_REVIVAL.get(), 72000), player);
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        int baseCount = super.getLootingLevel(slotContext, source, target, baseLooting, stack);
        if (target.hasEffect(ModPotions.FRACTURED.get()))
            return baseCount + config.cursedTotemBonusLooting.get();
        return baseCount;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.TOTEM_USE, 0.5F, 1);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE || enchantment == Enchantments.VANISHING_CURSE;
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return ModRarities.CHALLENGE;
    }
}
