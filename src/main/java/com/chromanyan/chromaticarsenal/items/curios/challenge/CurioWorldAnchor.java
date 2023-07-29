package com.chromanyan.chromaticarsenal.items.curios.challenge;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioWorldAnchor extends BaseCurioItem {

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.world_anchor.1"));
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        LivingEntity entity = slotContext.entity();
        if (entity == null) {
            ChromaticArsenal.LOGGER.warn("Tried to get attribute modifiers for world anchor but entity was null");
            return atts; // should hopefully fix a NPE when reloading resources with F3+T
        }
        Level level = entity.getLevel();
        double relativeY = entity.getY() - level.getMinBuildHeight(); // the entity's y position relative to the bottom of the world, e.g. y position + 64 in the overworld
        int worldHeight = level.getMaxBuildHeight() - level.getMinBuildHeight();
        double gravityMod;

        if (relativeY > worldHeight) {
            gravityMod = 1;
        } else if (relativeY > 0) {
            gravityMod = relativeY / worldHeight;
        } else {
            gravityMod = 0;
        }

        atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, Reference.MODID + ":world_anchor_gravity", gravityMod, AttributeModifier.Operation.MULTIPLY_BASE));
        atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, Reference.MODID + ":world_anchor_speed", -gravityMod * 0.5, AttributeModifier.Operation.MULTIPLY_BASE));
        atts.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, Reference.MODID + ":world_anchor_kbresist", gravityMod, AttributeModifier.Operation.MULTIPLY_BASE));
        atts.put(Attributes.ARMOR, new AttributeModifier(uuid, Reference.MODID + ":world_anchor_armor", 4, AttributeModifier.Operation.ADDITION));
        if (entity instanceof Player) {
            CuriosApi.getCuriosHelper().addSlotModifier(atts, "charm",
                    UUID.fromString("d020cd5d-c050-49e4-a0ea-ef27adf7e6d0"), 1, AttributeModifier.Operation.ADDITION);
        }

        return atts;
    }

}
