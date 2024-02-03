package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Unique
    private static final ModConfig.Client chromatic_workspace_19$clientConfig = ModConfig.CLIENT;

    @Unique
    private static final ResourceLocation ANON_SKIN = new ResourceLocation(ChromaticArsenal.MODID, "textures/entity/anonymous.png");

    @Unique
    private boolean chromatic_workspace_19$shouldCloak() {
        if (chromatic_workspace_19$clientConfig.anonymityOptOut.get()) return false;

        AbstractClientPlayer trueThis = (AbstractClientPlayer)(Object) this;
        return ChromaCurioHelper.getCurio(trueThis, ModItems.ANONYMITY_UMBRELLA.get()).isPresent();
    }

    // if the curio is equipped, replace their skin with our anonymous one
    @Inject(method = "getSkinTextureLocation", at = @At("RETURN"), cancellable = true)
    private void getSkinTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        if (chromatic_workspace_19$shouldCloak()) {
            cir.setReturnValue(ANON_SKIN);
        }
    }

    // effectively, this will mean the player won't be associated with their UUID for *anything* regarding rendering
    @Inject(method = "getPlayerInfo", at = @At("RETURN"), cancellable = true)
    private void getPlayerInfo(CallbackInfoReturnable<ResourceLocation> cir) {
        if (chromatic_workspace_19$shouldCloak()) {
            cir.setReturnValue(null);
        }
    }
}
