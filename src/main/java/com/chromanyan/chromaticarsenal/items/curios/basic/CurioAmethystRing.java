package com.chromanyan.chromaticarsenal.items.curios.basic;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class CurioAmethystRing extends BaseCurioItem {

    public CurioAmethystRing() {
        super(Rarity.COMMON, SoundEvents.AMETHYST_BLOCK_PLACE);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();

        atts.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":reach_bonus", config.amethystRingReachModifier.get(), AttributeModifier.Operation.ADDITION));

        return atts;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment != ModEnchantments.CHROMATIC_TWISTING.get() && super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
