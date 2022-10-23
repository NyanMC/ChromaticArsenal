package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class CurioFriendlyFireFlower extends BaseCurioItem {

    public CurioFriendlyFireFlower() {
        super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(1).rarity(Rarity.RARE).defaultDurability(25));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide && living.isOnFire() && !living.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, true, true));
            stack.hurtAndBreak(1, living, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(context));
        }
    }
}
