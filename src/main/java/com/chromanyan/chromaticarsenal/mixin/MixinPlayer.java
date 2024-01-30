package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

@Mixin(Player.class)
public abstract class MixinPlayer {

    @Inject(method = "isStayingOnGroundSurface", at = @At("RETURN"), cancellable = true)
    private void isStayingOnGroundSurface(CallbackInfoReturnable<Boolean> cir) {
        if (ChromaCurioHelper.getCurio((Player)(Object)this, ModItems.VERTICAL_STASIS.get()).isPresent()) {
            ItemStack stack = ChromaCurioHelper.getCurio((Player)(Object)this, ModItems.VERTICAL_STASIS.get()).get().stack();
            if (stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active")) {
                cir.setReturnValue(false);
            }
        }
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V"))
    private void setSprinting(Player instance, boolean b) {
        if (ChromaCurioHelper.getCurio(instance, ModItems.MOMENTUM_STONE.get()).isPresent()) {
            return; // don't actually stop sprinting
        }
        instance.setSprinting(false); // continue as normal
    }

    @Redirect(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    private void causeFoodExhaustion(Player instance, float p_36400_) {
        if (!instance.isSprinting()) {
            instance.causeFoodExhaustion(p_36400_);
            return;
        }

        Optional<SlotResult> slotResult = ChromaCurioHelper.getCurio(instance, ModItems.MOMENTUM_STONE.get());

        if (slotResult.isEmpty() || !ChromaCurioHelper.isChromaticTwisted(slotResult.get().stack(), instance)) {
            instance.causeFoodExhaustion(p_36400_);
            return;
        }

        instance.causeFoodExhaustion(p_36400_ * 1.5F); //TODO configurability
    }
}
