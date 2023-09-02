package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurioHarpyFeather extends BaseCurioItem {

    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (!ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.1"));
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.2"));
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.3"));
        } else {
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.twisted"));
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.twisted2"));
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(this)) {
                if (player.getVehicle() == null) {
                    if (player.isOnGround()) {
                        player.getCooldowns().removeCooldown(this);
                    } else {
                        player.getCooldowns().addCooldown(this, 60);
                    }
                } else {
                    if (player.getVehicle().isOnGround()) {
                        player.getCooldowns().removeCooldown(this);
                    } else {
                        player.getCooldowns().addCooldown(this, 60);
                    }
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isDiscrete())
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());

        if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
            player.getCooldowns().addCooldown(this, 60);
        if (!level.isClientSide()) {
            if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
                player.resetFallDistance();
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.8f, 5f);
        }
        Vec3 vec3 = player.getDeltaMovement();
        if (player.getVehicle() != null) {
            if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
                player.getVehicle().setDeltaMovement(vec3.x, config.jumpForce.get(), vec3.z);
            else
                player.getVehicle().setDeltaMovement(0, -1, 0);
        } else {
            if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
                player.setDeltaMovement(vec3.x, config.jumpForce.get(), vec3.z);
            else
                player.setDeltaMovement(0, -1, 0);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        if (ChromaCurioHelper.isChromaticTwisted(stack, null))
            return ModRarities.TWISTED;
        else
            return Rarity.UNCOMMON;
    }
}
