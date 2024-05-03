package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

    @Inject(method = "hasEnoughFoodToStartSprinting", at = @At("HEAD"), cancellable = true)
    private void hasEnoughHungerToSprint(CallbackInfoReturnable<Boolean> cir) {
        if (ChromaCurioHelper.getCurio((LocalPlayer)(Object) this, ModItems.MOMENTUM_STONE.get()).isPresent()) {
            cir.setReturnValue(true);
        }
    }

}
