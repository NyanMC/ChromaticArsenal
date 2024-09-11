package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

@Mixin(Player.class)
public abstract class MixinPlayer {

    @Unique
    private static final ModConfig.Common chromatic_workspace_19$config = ModConfig.COMMON;

    @ModifyReturnValue(method = "isStayingOnGroundSurface", at = @At("RETURN"))
    private boolean isStayingOnGroundSurface(boolean original) {
        Player trueThis = (Player)(Object)this;

        if (ChromaCurioHelper.getCurio(trueThis, ModItems.VERTICAL_STASIS.get()).isPresent()) {
            ItemStack stack = ChromaCurioHelper.getCurio(trueThis, ModItems.VERTICAL_STASIS.get()).get().stack();
            if (stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active")) {
                return false;
            }
        }
        return original;
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V"))
    private void maybeDontStopSprinting(Player instance, boolean b, Operation<Void> original) {
        if (ChromaCurioHelper.getCurio(instance, ModItems.MOMENTUM_STONE.get()).isEmpty()) {
            original.call(instance, b);
        }
    }

    @WrapOperation(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    private void modifyFoodExhaustionRate(Player instance, float f, Operation<Void> original) {
        if (!instance.isSprinting()) {
            original.call(instance, f);
            return;
        }

        Optional<SlotResult> slotResult = ChromaCurioHelper.getCurio(instance, ModItems.MOMENTUM_STONE.get());

        if (slotResult.isEmpty() || !ChromaCurioHelper.isChromaticTwisted(slotResult.get().stack(), instance)) {
            original.call(instance, f);
            return;
        }

        original.call(instance, f * chromatic_workspace_19$config.twistedMomentumStoneExhaustion.get().floatValue());
    }
}
