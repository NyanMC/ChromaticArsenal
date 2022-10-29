package com.chromanyan.chromaticarsenal.items;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeType;

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
    public int getBurnTime(ItemStack itemStack, @org.jetbrains.annotations.Nullable RecipeType<?> recipeType) {
        return 12800;
    }
}
