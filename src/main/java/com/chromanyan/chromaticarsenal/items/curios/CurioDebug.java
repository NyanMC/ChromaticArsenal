package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModSounds;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.CompletionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CurioDebug extends BaseCurioItem {

    public CurioDebug() {
        super(ModSounds.DIAL_UP.get());
    }

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

        if (!(living instanceof ServerPlayer serverPlayer)) return;

        Tuple<Integer, Integer> advancementCount = CompletionHelper.getCompletedAndTotalAdvancements(serverPlayer);

        if (advancementCount == null) return;

        ChromaticArsenal.LOGGER.debug("Completed " + advancementCount.getA() + " out of " + advancementCount.getB() + " advancements");
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide) {
            living.setGlowingTag(false);
        }
    }
}
