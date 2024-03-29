package com.chromanyan.chromaticarsenal.items.food;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;

public class Cosmicola extends Item {

    public Cosmicola() {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(16)
                .rarity(Rarity.EPIC)
                .food(new FoodProperties.Builder()
                        .nutrition(15)
                        .saturationMod(0.7F)
                        .alwaysEat()
                        .effect(() -> new MobEffectInstance(ModEffects.SPATIAL.get(), 3600, 0), 1.0F)
                        .build()));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }
}
