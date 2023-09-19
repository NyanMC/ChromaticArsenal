package com.chromanyan.chromaticarsenal.enchantments;

import com.chromanyan.chromaticarsenal.items.curios.challenge.CurioLimitBreak;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.ISuperCurio;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class EnchantmentChromaticTwisting extends Enchantment {
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public EnchantmentChromaticTwisting() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEARABLE, ARMOR_SLOTS);
    }

    public int getMinCost(int p_44616_) {
        return 25;
    }

    public int getMaxCost(int p_44619_) {
        return 50;
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack stack) {
        return stack.getItem() instanceof IChromaCurio && !(stack.getItem() instanceof ISuperCurio || stack.getItem() instanceof CurioLimitBreak);
    }
}
