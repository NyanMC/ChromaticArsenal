package com.chromanyan.chromaticarsenal.mixin;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

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
    @ModifyReturnValue(method = "getSkinTextureLocation", at = @At("RETURN"))
    private ResourceLocation getSkinTextureLocation(ResourceLocation original) {
        if (chromatic_workspace_19$shouldCloak()) {
            return ANON_SKIN;
        }
        return original;
    }

    // effectively, this will mean the player won't be associated with their UUID for *anything* regarding rendering
    @ModifyReturnValue(method = "getPlayerInfo", at = @At("RETURN"))
    private PlayerInfo getPlayerInfo(PlayerInfo original) {
        if (chromatic_workspace_19$shouldCloak()) {
            return null;
        }
        return original;
    }
}
