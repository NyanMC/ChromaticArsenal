package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Unique
    private static final ModConfig.Common chromatic_workspace_19$config = ModConfig.COMMON;

    @Unique
    private static final float BLUE_ICE_FRICTION = 0.989F; // the most slippery block in vanilla

    @ModifyVariable(method = "travel", at = @At("STORE"), ordinal = 0)
    private double gravity(double d0) {
        LivingEntity trueThis = (LivingEntity)(Object) this;
        AttributeInstance gravity = trueThis.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        double baseGravity = gravity != null ? gravity.getBaseValue() : 0.08;
        if (d0 < baseGravity) {
            if (ChromaCurioHelper.getCurio(trueThis, ModItems.GRAVITY_STONE.get()).isPresent()) {
                return baseGravity;
            }
        }
        return d0;
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
    private float getFriction(float original) {
        LivingEntity trueThis = (LivingEntity)(Object) this;

        if (trueThis.getBlockSpeedFactor() > 1 || trueThis.isSprinting()) return original;

        Optional<SlotResult> slotResult = ChromaCurioHelper.getCurio(trueThis, ModItems.MOMENTUM_STONE.get());
        if (slotResult.isEmpty() || ChromaCurioHelper.isChromaticTwisted(slotResult.get().stack(), trueThis)) return original;

        float newFriction = original + chromatic_workspace_19$config.momentumStoneFriction.get().floatValue();

        return Math.min(newFriction, BLUE_ICE_FRICTION); // high levels of friction are buggy
    }
}
