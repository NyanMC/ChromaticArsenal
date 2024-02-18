package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Unique
    private static final ModConfig.Common chromatic_workspace_19$config = ModConfig.COMMON;

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void hurt(int amount, RandomSource randomSource, @Nullable ServerPlayer serverPlayer, CallbackInfoReturnable<Boolean> cir) {
        if (serverPlayer == null) return;

        if (ChromaCurioHelper.getCurio(serverPlayer, ModItems.COPPER_RING.get()).isEmpty()) {
            if (!ModList.get().isLoaded("enigmaticlegacy")) return;
            if (ChromaCurioHelper.getCurio(serverPlayer, ModItems.OMNI_RING.get()).isEmpty()) return;
        }
        if (randomSource.nextDouble() <= chromatic_workspace_19$config.copperRingUnbreakingChance.get()) {
            cir.setReturnValue(false);
        }
    }
}
