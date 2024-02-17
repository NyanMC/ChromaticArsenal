package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.CompletionHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioAdvancingHeart extends BaseCurioItem {

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.advancing_heart.1"));
        CompoundTag tag = stack.getOrCreateTag();

        int completed = tag.getInt("completedAdvancements");
        int total = tag.getInt("totalAdvancements");
        if (total > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.advancing_heart.progresstracker", TooltipHelper.valueTooltip(completed), TooltipHelper.valueTooltip(total)));
        else
            list.add(Component.translatable("tooltip.chromaticarsenal.advancing_heart.progresstracker.equipfirst"));
    }

    public static void updateNBTForStack(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof ServerPlayer serverPlayer)) return;

        Tuple<Integer, Integer> advancementCount = CompletionHelper.getCompletedAndTotalAdvancements(serverPlayer);
        if (advancementCount == null) return;

        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("completedAdvancements", advancementCount.getA());
        tag.putInt("totalAdvancements", advancementCount.getB());
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (ChromaCurioHelper.getCurio(slotContext.entity(), this).isEmpty()) return;
        updateNBTForStack(slotContext, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (ChromaCurioHelper.getCurio(slotContext.entity(), this).isPresent()) return;
        stack.removeTagKey("completedAdvancements");
        stack.removeTagKey("totalAdvancements");
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int p_41407_, boolean p_41408_) {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (ChromaCurioHelper.getCurio(livingEntity, this).isPresent()) return;
        stack.removeTagKey("completedAdvancements");
        stack.removeTagKey("totalAdvancements");
    }

    private static int getHealthModifier(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        float percentage = (float) tag.getInt("completedAdvancements") / tag.getInt("totalAdvancements");
        if (percentage == Float.POSITIVE_INFINITY) percentage = 0; // in case the item's NBT is manually edited or a weird bug happens
        return (int) (config.advancingHealthModifier.get() * percentage / 2) * 2; // the divide and re-multiply by 2 is to ensure an even number
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();

        atts.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":advancing_health_bonus", getHealthModifier(stack), AttributeModifier.Operation.ADDITION));

        return atts;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment != ModEnchantments.CHROMATIC_TWISTING.get() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.UI_TOAST_IN, 0.5F, 1);
    }
}
