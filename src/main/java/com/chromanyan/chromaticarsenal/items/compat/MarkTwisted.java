package com.chromanyan.chromaticarsenal.items.compat;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkTwisted extends Item implements ICurioItem {
    private static final DamageSource TWISTED = new DamageSource("mark_twisted").bypassArmor().bypassMagic();

    public MarkTwisted() {
        super(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        living.hurt(TWISTED, living.getMaxHealth() - 1);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), this).isEmpty();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.chromaticarsenal.void"));
        if (!(ModList.get().isLoaded("band_of_gigantism") || ModConfig.CLIENT.suppressMissingModNotices.get())) {
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.missing_bog"));
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.missing_bog2"));
        }
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.mark_twisted_description_flavor"));
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.void"));
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.mark_twisted_description_0"));
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.void"));
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.chromaticarsenal.shift"));
        }
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
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
