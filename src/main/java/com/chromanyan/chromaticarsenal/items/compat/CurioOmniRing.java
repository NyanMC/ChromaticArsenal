package com.chromanyan.chromaticarsenal.items.compat;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class CurioOmniRing extends ItemBaseCurio {

    private static final ModConfig.Common config = ModConfig.COMMON;

    public CurioOmniRing() {
        super(new Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .defaultDurability(0));
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
        if (Screen.hasShiftDown()) {
            if (Minecraft.getInstance().player != null && SuperpositionHandler.isTheCursedOne(Minecraft.getInstance().player)) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.goldenRing1Cursed");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.goldenRing1");
            }
            ItemLoreHelper.addLocalizedString(list, "tooltip.chromaticarsenal.omni_ring_copper", ChatFormatting.GOLD, (int) (config.copperRingUnbreakingChance.get() * 100) + "%");
        } else {
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(Attributes.ARMOR, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":armor_bonus", 1.0, AttributeModifier.Operation.ADDITION));
        attributes.put(Attributes.LUCK, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":luck_bonus", 1.0, AttributeModifier.Operation.ADDITION));
        attributes.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":reach_bonus", config.amethystRingReachModifier.get(), AttributeModifier.Operation.ADDITION));
        return attributes;
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    public boolean isPiglinCurrency(ItemStack stack) {
        return true;
    }
}
