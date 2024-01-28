package com.chromanyan.chromaticarsenal.items.compat;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.item.base.MarkItem;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkTwisted extends MarkItem {
    private static final DamageSource TWISTED = new DamageSource("mark_twisted").bypassArmor().bypassMagic();

    public MarkTwisted() {
        super(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0),
                TWISTED,
                ChatFormatting.LIGHT_PURPLE
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.mark_twisted_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.mark_twisted_description_0"));
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.mark_twisted_description_1"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        if (slotContext.entity() instanceof Player) {
            CuriosApi.getCuriosHelper().addSlotModifier(attributes, "curio",
                    UUID.fromString("d020cd5d-c050-49e4-a0ea-ef27adf7e6d0"), 1, AttributeModifier.Operation.ADDITION);
        }

        return attributes;
    }
}
