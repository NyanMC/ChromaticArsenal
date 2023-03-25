package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;

public class CurioDispellingCrystal extends BaseSuperCurio {
    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioDispellingCrystal() {
        super(ModItems.WARD_CRYSTAL);
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().isMagic() && !event.getSource().isBypassInvul()) {
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierIncoming.get()));
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().isMagic() && !event.getSource().isBypassInvul()) {
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierOutgoing.get()));
        }
    }

    @Override
    public void onPotionApplied(PotionEvent.PotionAddedEvent event) {
        event.getPotionEffect().duration *= config.potionDurationMultiplier.get(); // because why should forge let you set the duration of a potion effect without an access transformer?
    }
}
