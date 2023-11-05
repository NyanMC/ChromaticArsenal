package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class CurioBubbleAmulet extends BaseCurioItem {
    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();
        ChromaticArsenal.LOGGER.info("Current air supply: " + living.getAirSupply());
        //TODO actually implement this
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        //TODO make swim speed attribute here configurable
        atts.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":bubble_swimming", 0.5, AttributeModifier.Operation.MULTIPLY_BASE));
        return atts;
    }
}
