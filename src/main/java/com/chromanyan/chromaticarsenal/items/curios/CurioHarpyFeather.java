package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CurioHarpyFeather extends BaseCurioItem {

    final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, config.jumpCooldown.get());
        if (!level.isClientSide()) {
            player.fallDistance = 0;
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.8f, 5f);
        }
        Vec3 vec3 = player.getDeltaMovement();
        if (player.getVehicle() != null) {
            player.getVehicle().setDeltaMovement(vec3.x, config.jumpForce.get(), vec3.z);
        } else {
            player.setDeltaMovement(vec3.x, config.jumpForce.get(), vec3.z);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
