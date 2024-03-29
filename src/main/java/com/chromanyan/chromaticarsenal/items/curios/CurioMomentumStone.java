package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class CurioMomentumStone extends BaseCurioItem {

    public CurioMomentumStone() {
        super(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.momentum_stone.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.momentum_stone.2"));
        if (ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.momentum_stone.twisted"));
        } else {
            list.add(Component.translatable("tooltip.chromaticarsenal.momentum_stone.3"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":momentum_stone_kbresist", 1, AttributeModifier.Operation.ADDITION));

        double toughnessModifier = stack.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION) * config.momentumStoneProtectionToughness.get();

        if (toughnessModifier > 0) {
            atts.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":momentum_stone_toughness", toughnessModifier, AttributeModifier.Operation.ADDITION));
        }

        return atts;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.ALL_DAMAGE_PROTECTION) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }
}
