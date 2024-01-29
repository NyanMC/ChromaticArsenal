package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

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

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
    private float getFriction(BlockState instance, LevelReader levelReader, BlockPos blockPos, Entity entity) {
        float originalReturn = instance.getFriction(levelReader, blockPos, entity);

        if (!(entity instanceof LivingEntity livingEntity)) return originalReturn;
        if (livingEntity.getBlockSpeedFactor() > 1 || livingEntity.isSprinting()) return originalReturn; // never combine friction and >1 speed factor
        if (ChromaCurioHelper.getCurio(livingEntity, ModItems.MOMENTUM_STONE.get()).isEmpty()) return originalReturn;

        float newFriction = originalReturn + 0.3F;

        return Math.min(newFriction, BLUE_ICE_FRICTION); // high levels of friction are buggy
    }
}
