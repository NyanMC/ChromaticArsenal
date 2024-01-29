package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

    @Unique
    private static final int NO_SPRINT_HUNGER = 6;

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;getFoodLevel()I"))
    private int fakeFoodLevel(FoodData instance) {
        if (instance.getFoodLevel() <= NO_SPRINT_HUNGER && ChromaCurioHelper.getCurio((LocalPlayer)(Object) this, ModItems.MOMENTUM_STONE.get()).isPresent()) {
            return NO_SPRINT_HUNGER + 1; // tricks the game into thinking the player has enough hunger to sprint
        }

        return instance.getFoodLevel();
    }

}
