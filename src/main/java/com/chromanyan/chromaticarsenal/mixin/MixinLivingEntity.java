package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    private static final ModConfig.Common config = ModConfig.COMMON;

    @ModifyVariable(method = "getVisibilityPercent", at = @At("STORE"), ordinal = 0)
    private double getVisibilityPercent(double d0, @Nullable Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (ChromaCurioHelper.getCurio(livingEntity, ModItems.SHADOW_TREADS.get()).isPresent()) {
                ItemStack stack = ChromaCurioHelper.getCurio(livingEntity, ModItems.SHADOW_TREADS.get()).get().stack();
                if (stack.getEnchantmentLevel(Enchantments.SWIFT_SNEAK) > 0 && config.swiftSneakDetectionReduction.get() > 0) {
                    return Math.max(0, d0 * (1 - (stack.getEnchantmentLevel(Enchantments.SWIFT_SNEAK) * config.swiftSneakDetectionReduction.get())));
                }
            }
        }
        return d0;
    }
}
