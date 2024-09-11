package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public class MixinThrownTrident {

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("RETURN"))
    private void thrownTrident(Level level, LivingEntity livingEntity, ItemStack itemStack, CallbackInfo ci) {
        if (EnchantmentHelper.hasChanneling(itemStack) && ChromaCurioHelper.getCurio(livingEntity, ModItems.THUNDERGUARD.get()).isPresent()) {
            ((ThrownTrident)(Object) this).getPersistentData().putBoolean("chromaticarsenal.thunderguard_boost", true);
        }
    }

    @ModifyExpressionValue(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z"))
    private boolean shouldBypassThunderRequirement(boolean original) {
        return ((ThrownTrident)(Object) this).getPersistentData().getBoolean("chromaticarsenal.thunderguard_boost") || original;
    }

}
