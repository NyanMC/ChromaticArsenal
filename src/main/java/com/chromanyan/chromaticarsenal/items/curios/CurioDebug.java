package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.init.ModSounds;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public class CurioDebug extends BaseCurioItem {

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.viewer_item.1").withStyle(ChatFormatting.RED));
        list.add(Component.translatable("tooltip.chromaticarsenal.viewer_item.2").withStyle(ChatFormatting.RED));
    }

    @Override
    public void onEquip(SlotContext context, ItemStack prevStack, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide) {
            living.setGlowingTag(true);
        }
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide) {
            living.setGlowingTag(false);
        }
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(ModSounds.DIAL_UP.get(), 0.5F, 1);
    }
}
