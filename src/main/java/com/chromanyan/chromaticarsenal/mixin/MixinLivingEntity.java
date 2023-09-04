package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Unique
    private static final ModConfig.Common chromatic_workspace_19$config = ModConfig.COMMON;

    @Inject(method = "getVisibilityPercent", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void getVisibilityPercent(Entity entity, CallbackInfoReturnable<Double> cir, double d0) {
        if (entity instanceof LivingEntity livingEntity) {
            if (ChromaCurioHelper.getCurio(livingEntity, ModItems.SHADOW_TREADS.get()).isPresent()) {
                ItemStack stack = ChromaCurioHelper.getCurio(livingEntity, ModItems.SHADOW_TREADS.get()).get().stack();
                if (stack.getEnchantmentLevel(Enchantments.SWIFT_SNEAK) > 0 && chromatic_workspace_19$config.swiftSneakDetectionReduction.get() > 0) {
                    cir.setReturnValue(Math.max(0, d0 * (1 - (stack.getEnchantmentLevel(Enchantments.SWIFT_SNEAK) * chromatic_workspace_19$config.swiftSneakDetectionReduction.get()))));
                }
            }
        }
    }
}
