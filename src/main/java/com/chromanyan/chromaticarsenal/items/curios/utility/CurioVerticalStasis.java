package com.chromanyan.chromaticarsenal.items.curios.utility;

import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class CurioVerticalStasis extends BaseCurioItem {

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.utility"));
        list.add(Component.translatable("tooltip.chromaticarsenal.vertical_stasis_stone.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.vertical_stasis_stone.2"));
        if (ChromaCurioHelper.isChromaticTwisted(stack, null))
            list.add(Component.translatable("tooltip.chromaticarsenal.vertical_stasis_stone.twisted"));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        LivingEntity entity = context.entity();
        nbt.putBoolean("active", entity.isDiscrete() && (entity.isOnGround() || nbt.getBoolean("active")));
        if (ChromaCurioHelper.isChromaticTwisted(stack, entity)) {
            if (nbt.getBoolean("active")) {
                entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 10, 2), entity);
            }
        } else {
            entity.setNoGravity(nbt.getBoolean("active"));
            if (entity.getCommandSenderWorld().isClientSide()) {
                if (nbt.getBoolean("active")) {
                    Vec3 deltaMovement = entity.getDeltaMovement();
                    entity.setDeltaMovement(deltaMovement.x, 0, deltaMovement.z); // zero out their Y momentum completely, prevents a lot of cheesing
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        if (!context.entity().getCommandSenderWorld().isClientSide()) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putBoolean("active", false);
            context.entity().setNoGravity(false);
        }
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.SHULKER_BULLET_HIT, 0.5F, 1);
    }
    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return ModRarities.UTILITY;
    }
}
