package com.chromanyan.chromaticarsenal.items.curios.basic;

import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;

public class CurioVitalStone extends BaseCurioItem {
    private static final Random rand = new Random();

    public CurioVitalStone() {
        super(Rarity.COMMON, SoundEvents.STONE_BREAK);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!Screen.hasShiftDown()) return;
        list.add(Component.translatable("tooltip.chromaticarsenal.vital_stone.1", TooltipHelper.ticksToSecondsTooltip(config.vitalStoneFrequency.get())));
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (CooldownHelper.isCooldownFinished(nbt)) {
            CooldownHelper.updateCounter(nbt, config.vitalStoneFrequency.get());
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().getCommandSenderWorld().isClientSide) return;
        LivingEntity livingEntity = slotContext.entity();

        CompoundTag nbt = stack.getOrCreateTag();
        if (!(CooldownHelper.tickCounter(nbt) || CooldownHelper.isCooldownFinished(nbt))) return;

        livingEntity.heal(1);

        CooldownHelper.updateCounter(nbt, config.vitalStoneFrequency.get());
    }

    public static void handleDrop(LivingDropsEvent event, LivingEntity dying) {
        int chance = config.vitalStoneDropChance.get() - event.getLootingLevel() * config.vitalStoneDropLootingModifier.get();

        if (chance <= 0 || rand.nextInt(chance) == 0) {
            event.getDrops().add(dying.spawnAtLocation(new ItemStack(ModItems.VITAL_STONE.get())));
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment != ModEnchantments.CHROMATIC_TWISTING.get() && super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
