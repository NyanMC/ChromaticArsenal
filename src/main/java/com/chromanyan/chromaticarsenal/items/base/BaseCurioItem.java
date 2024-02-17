package com.chromanyan.chromaticarsenal.items.base;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BaseCurioItem extends Item implements ICurioItem, IChromaCurio {

    protected static final ModConfig.Common config = ModConfig.COMMON;
    protected static final ModConfig.Client clientConfig = ModConfig.CLIENT;

    @Nullable
    protected final SoundEvent soundEvent;

    public BaseCurioItem(Rarity rarity, @Nullable SoundEvent soundEvent) {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .rarity(rarity)
                .defaultDurability(0));
        this.soundEvent = soundEvent;
    }

    public BaseCurioItem(Rarity rarity) {
        this(rarity, null);
    }

    public BaseCurioItem() {
        this(Rarity.RARE);
    }

    public BaseCurioItem(SoundEvent soundEvent) {
        this(Rarity.RARE, soundEvent);
    }

    public BaseCurioItem(Item.Properties properties, @Nullable SoundEvent soundEvent) {
        super(properties);
        this.soundEvent = soundEvent;
    }

    public BaseCurioItem(Item.Properties properties) {
        this(properties, null);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), this).isEmpty();
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (
                enchantment == Enchantments.BINDING_CURSE ||
                enchantment == Enchantments.VANISHING_CURSE ||
                enchantment == ModEnchantments.CHROMATIC_TWISTING.get()
        ) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        if (ChromaCurioHelper.isChromaticTwisted(stack, null))
            return ModRarities.TWISTED;
        else
            return super.getRarity(stack);
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        if (this.soundEvent != null) {
            return new ICurio.SoundInfo(soundEvent, 0.5F, 1);
        }
        return ICurioItem.super.getEquipSound(slotContext, stack);
    }
}
