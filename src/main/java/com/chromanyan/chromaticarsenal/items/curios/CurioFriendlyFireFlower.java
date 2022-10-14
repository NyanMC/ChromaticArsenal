package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import top.theillusivec4.curios.api.CuriosApi;

public class CurioFriendlyFireFlower extends BaseCurioItem {

    public CurioFriendlyFireFlower() {
        super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(1).rarity(Rarity.RARE).defaultDurability(25));
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
        if (!living.getCommandSenderWorld().isClientSide && living.isOnFire() && !living.hasEffect(Effects.FIRE_RESISTANCE)) {
            living.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 200, 0, true, true));
            stack.hurtAndBreak(1, living, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, living));
        }
    }
}
