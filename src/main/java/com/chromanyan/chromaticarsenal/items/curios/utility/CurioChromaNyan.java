package com.chromanyan.chromaticarsenal.items.curios.utility;

import com.chromanyan.chromaticarsenal.init.ModBlocks;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class CurioChromaNyan extends BlockItem implements IChromaCurio, ICurioItem {

    public CurioChromaNyan() {
        super(ModBlocks.CHROMANYAN.get(), new Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .defaultDurability(0));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.vanity"));
        list.add(Component.translatable("tooltip.chromaticarsenal.can_place"));
        list.add(Component.translatable("tooltip.chromaticarsenal.impersonation"));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), this).isEmpty();
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.WOOL_PLACE, 0.5F, 1);
    }
}
