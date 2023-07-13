package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

public class CurioFriendlyFireFlower extends BaseCurioItem {

    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(new TranslatableComponent("tooltip.chromaticarsenal.friendly_fire_flower.1"));
        list.add(new TranslatableComponent("tooltip.chromaticarsenal.friendly_fire_flower.2", "§b" + (((float) getEffectDuration(stack)) / 20)));
        if (config.canBeDamaged.get()) {
            list.add(new TranslatableComponent("tooltip.chromaticarsenal.friendly_fire_flower.3"));
        }
    }

    private int getEffectDuration(ItemStack stack) {
        return config.fireResistanceDuration.get() * (1 + getItemEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack));
    }

    public CurioFriendlyFireFlower() {
        super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(1).rarity(Rarity.RARE).defaultDurability(25).fireResistant());
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide && living.isOnFire()) {
            if (!(living.hasEffect(MobEffects.FIRE_RESISTANCE) || living.fireImmune())) { // will fireImmune() ever even trigger for a player? hell if i know
                living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, getEffectDuration(stack), 0, true, true));
                if (config.canBeDamaged.get()) {
                    stack.hurtAndBreak(1, living, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(context));
                }
            } else {
                living.clearFire();
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.FIRE_PROTECTION) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (player == target) {
            event.setAmount(0);
        }
    }
}
