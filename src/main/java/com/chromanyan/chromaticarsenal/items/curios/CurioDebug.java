package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModSounds;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.Map;

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

        if (!(living instanceof ServerPlayer serverPlayer)) return;

        MinecraftServer server = serverPlayer.level.getServer();
        if (server == null) return;

        PlayerAdvancements playerAdvancements = server.getPlayerList().getPlayerAdvancements(serverPlayer);
        int completedAdvancements = 0;
        int totalAdvancements = 0;

        for (Map.Entry<Advancement, AdvancementProgress> advancements : playerAdvancements.advancements.entrySet()) {
            Advancement advancement = advancements.getKey();
            if (advancement.getId().toString().contains("recipes")) continue; // don't count recipe advancements, too many

            totalAdvancements++;
            if (playerAdvancements.getOrStartProgress(advancement).isDone()) {
                completedAdvancements++;
            }
        }

        ChromaticArsenal.LOGGER.debug("Completed " + completedAdvancements + " out of " + totalAdvancements + " advancements");
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
