package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class CurioVerticalStasis extends BaseCurioItem {

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
