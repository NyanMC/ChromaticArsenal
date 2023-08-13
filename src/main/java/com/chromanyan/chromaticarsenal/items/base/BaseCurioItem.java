package com.chromanyan.chromaticarsenal.items.base;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BaseCurioItem extends Item implements ICurioItem, IChromaCurio {

    public BaseCurioItem() {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .defaultDurability(0));
    }

    public BaseCurioItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), this).isEmpty();
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.BINDING_CURSE || enchantment == Enchantments.VANISHING_CURSE) { // for some reason this doesn't happen unless a curio has durability
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        if (stack.getEnchantmentLevel(ModEnchantments.CHROMATIC_TWISTING.get()) > 0)
            return ModRarities.TWISTED;
        else
            return super.getRarity(stack);
    }
}
