package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

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
}
