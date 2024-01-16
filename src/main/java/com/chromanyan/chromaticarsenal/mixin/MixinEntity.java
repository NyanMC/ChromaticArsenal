package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Unique
    private void chromatic_workspace_19$cancelCallbackInfoIfInferno(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        if (livingEntity.hasEffect(ModEffects.INFERNO.get())) {
            ci.cancel();
        }
    }

    @Inject(method = "clearFire", at = @At("HEAD"), cancellable = true)
    private void clearFire(CallbackInfo ci) {
        chromatic_workspace_19$cancelCallbackInfoIfInferno(ci);
    }

    @Inject(method = "playEntityOnFireExtinguishedSound", at = @At("HEAD"), cancellable = true)
    private void playEntityOnFireExtinguishedSound(CallbackInfo ci) {
        chromatic_workspace_19$cancelCallbackInfoIfInferno(ci);
    }

    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    private void setRemainingFireTicks(int newTime, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        if (entity.getRemainingFireTicks() > newTime) {
            chromatic_workspace_19$cancelCallbackInfoIfInferno(ci);
        }
    }
}
