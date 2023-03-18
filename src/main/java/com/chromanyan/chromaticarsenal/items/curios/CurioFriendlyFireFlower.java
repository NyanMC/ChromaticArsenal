package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

public class CurioFriendlyFireFlower extends BaseCurioItem {

    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioFriendlyFireFlower() {
        super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(1).rarity(Rarity.RARE).defaultDurability(25).fireResistant());
    }

    //TODO move this to onWeaererAttack
    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide && living.isOnFire() && !living.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            int fireResistanceBonus = 1 + getItemEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
            living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, config.fireResistanceDuration.get() * fireResistanceBonus, 0, true, true));
            if (config.canBeDamaged.get()) {
                stack.hurtAndBreak(1, living, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(context));
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
