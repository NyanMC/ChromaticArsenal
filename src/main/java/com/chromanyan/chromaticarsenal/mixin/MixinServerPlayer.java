package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {
    @Shadow public abstract boolean isCreative();

    @Redirect(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isCreative()Z"))
    private boolean shouldSkipHostileChecks(ServerPlayer instance) {
        return ChromaCurioHelper.getCurio(instance, ModItems.BLAHAJ.get()).isPresent() || isCreative();
    }
}
