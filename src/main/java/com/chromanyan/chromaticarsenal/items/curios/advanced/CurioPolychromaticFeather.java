package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class CurioPolychromaticFeather extends BaseSuperCurio {

    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioPolychromaticFeather() {
        super(ModItems.HARPY_FEATHER);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("tooltip.chromaticarsenal.utility"));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_harpy_feather.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_harpy_feather.2"));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (level.isClientSide()) {
            return;
        }
        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(this)) {
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 5, 0));
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
    public void curioTick(SlotContext context, ItemStack stack) {
        super.curioTick(context, stack);
        LivingEntity entity = context.entity();
        Vec3 deltaMovement = entity.getDeltaMovement();
        AttributeInstance gravity = entity.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        double forceMultiplier = gravity != null ? gravity.getValue() / 0.08 : 1;
        if (
                entity.jumping
                && deltaMovement.y > 0
                && !entity.isFallFlying()
                && !entity.isNoGravity()
        ) {
            entity.setDeltaMovement(deltaMovement.add(0, 0.05 * forceMultiplier, 0));
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isDiscrete())
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());

        player.getCooldowns().addCooldown(this, 60);
        if (!level.isClientSide()) {
            player.resetFallDistance();
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.8f, 3f);
        }
        Vec3 vec3 = player.getDeltaMovement();
        if (player.getVehicle() != null) {
            player.getVehicle().setDeltaMovement(vec3.x, config.superJumpForce.get(), vec3.z);
        } else {
            player.setDeltaMovement(vec3.x, config.superJumpForce.get(), vec3.z);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8F, 3F);
    }
}
