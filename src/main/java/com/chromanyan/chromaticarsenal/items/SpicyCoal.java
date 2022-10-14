package com.chromanyan.chromaticarsenal.items;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nullable;

public class SpicyCoal extends Item {
    /*
    in memory of my first ever mod
    never forget what started it all
    */
    public SpicyCoal() {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable IRecipeType<?> recipeType) {
        return 12800;
    }
}
