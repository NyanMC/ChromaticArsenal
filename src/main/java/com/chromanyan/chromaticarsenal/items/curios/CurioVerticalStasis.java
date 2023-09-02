package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CurioVerticalStasis extends BaseCurioItem {

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.vertical_stasis_stone.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.vertical_stasis_stone.2"));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        LivingEntity entity = context.entity();

        nbt.putBoolean("active", entity.isDiscrete() && (entity.isOnGround() || nbt.getBoolean("active")));
        entity.setNoGravity(nbt.getBoolean("active"));
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        if (!context.entity().getCommandSenderWorld().isClientSide()) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putBoolean("active", false);
            context.entity().setNoGravity(false);
        }
    }
}
