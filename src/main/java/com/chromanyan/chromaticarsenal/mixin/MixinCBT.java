package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.textstack.band_of_gigantism.util.CurioHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// never did i ever think i would be mixin injecting into someone's balls...ouch
@Pseudo
@Mixin(targets = "net.textstack.band_of_gigantism.event.EventHandlerMyBallsInYourMouth", remap = false)
public class MixinCBT {
    @Inject(method = "colorMark", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void colorMark(LivingEntity living, String message, CallbackInfoReturnable<String> cir, String newMessage) {
        if (CurioHelper.hasCurio(living, ModItems.MARK_TWISTED.get()) && newMessage == null) {
            cir.setReturnValue("§d" + message + "§r");
        }
    }
}
