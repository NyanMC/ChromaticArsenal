package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class CurioCelestialCharm extends BaseSuperCurio {

    final ModConfig.Common config = ModConfig.COMMON;

    public CurioCelestialCharm() {
        super(ModItems.SHADOW_TREADS);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        LivingEntity entity = slotContext.entity();
        long time = entity.level.getDayTime() % 24000; // no see
        if (time <= 6000) {
            long compareTime = time + 6000;
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":celestial_speed_bonus", config.speedModifierMax.get() * (1 - ((float) compareTime / 12000F)), AttributeModifier.Operation.MULTIPLY_TOTAL));
            atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, Reference.MODID + ":celestial_damage_bonus", config.damageModifierMax.get() * ((float) compareTime / 12000F), AttributeModifier.Operation.MULTIPLY_TOTAL));

        } else if (time <= 18000) {
            long compareTime = time - 6000;
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":celestial_speed_bonus", config.speedModifierMax.get() * ((float) compareTime / 12000F), AttributeModifier.Operation.MULTIPLY_TOTAL));
            atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, Reference.MODID + ":celestial_damage_bonus", config.damageModifierMax.get() * (1 - ((float) compareTime / 12000F)), AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else {
            long compareTime = time - 18000;
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":celestial_speed_bonus", config.speedModifierMax.get() * (1 - ((float) compareTime / 12000F)), AttributeModifier.Operation.MULTIPLY_TOTAL));
            atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, Reference.MODID + ":celestial_damage_bonus", config.damageModifierMax.get() * ((float) compareTime / 12000F), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return atts;
    }

}
