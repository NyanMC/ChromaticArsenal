package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioCelestialCharm extends BaseSuperCurio {

    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioCelestialCharm() {
        super(ModItems.SHADOW_TREADS);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("tooltip.chromaticarsenal.super_shadow_treads.1"));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        LivingEntity entity = slotContext.entity();
        if (entity == null) {
            ChromaticArsenal.LOGGER.warn("Tried to get attribute modifiers for celestial charm but entity was null");
            return atts; // should hopefully fix a NPE when reloading resources with F3+T
        }
        long time = entity.level.getDayTime() % 24000; // no see
        if (time <= 6000) {
            long compareTime = time + 6000;
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":celestial_speed_bonus", config.speedModifierMax.get() * (1 - ((float) compareTime / 12000F)), AttributeModifier.Operation.MULTIPLY_TOTAL));
            atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":celestial_damage_bonus", config.damageModifierMax.get() * ((float) compareTime / 12000F), AttributeModifier.Operation.MULTIPLY_TOTAL));

        } else if (time <= 18000) {
            long compareTime = time - 6000;
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":celestial_speed_bonus", config.speedModifierMax.get() * ((float) compareTime / 12000F), AttributeModifier.Operation.MULTIPLY_TOTAL));
            atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":celestial_damage_bonus", config.damageModifierMax.get() * (1 - ((float) compareTime / 12000F)), AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else {
            long compareTime = time - 18000;
            atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":celestial_speed_bonus", config.speedModifierMax.get() * (1 - ((float) compareTime / 12000F)), AttributeModifier.Operation.MULTIPLY_TOTAL));
            atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":celestial_damage_bonus", config.damageModifierMax.get() * ((float) compareTime / 12000F), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return atts;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.GENERIC_EXTINGUISH_FIRE, 0.5F, 1);
    }
}
