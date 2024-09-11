package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

    @ModifyReturnValue(method = "hasEnoughFoodToStartSprinting", at = @At("RETURN"))
    private boolean canSprintDespiteLowHunger(boolean original) {
        if (ChromaCurioHelper.getCurio((LocalPlayer)(Object) this, ModItems.MOMENTUM_STONE.get()).isPresent()) {
            return true;
        }
        return original;
    }

}
