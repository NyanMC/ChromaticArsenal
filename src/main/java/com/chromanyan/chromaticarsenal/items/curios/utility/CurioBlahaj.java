package com.chromanyan.chromaticarsenal.items.curios.utility;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModBlocks;
import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class CurioBlahaj extends BlockItem implements IChromaCurio, ICurioItem {

    public CurioBlahaj() {
        super(ModBlocks.BLAHAJ.get(), new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .defaultDurability(0));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.utility"));
        list.add(Component.translatable("tooltip.chromaticarsenal.blahaj.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.blahaj.2"));
        list.add(Component.translatable("tooltip.chromaticarsenal.blahaj.3"));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity instanceof ServerPlayer player) {
            player.getStats().setValue(player, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), 0);
        }
    }

    @Override
    public void onGetVisibility(LivingEvent.LivingVisibilityEvent event, ItemStack stack) {
        if (event.getLookingEntity() instanceof Phantom) {
            event.modifyVisibility(0);
        }
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE || enchantment == Enchantments.VANISHING_CURSE;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.WOOL_PLACE, 0.5F, 1);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return ModRarities.UTILITY;
    }
}
