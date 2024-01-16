package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.SlotContext;

public class CurioInfernoFlower extends BaseSuperCurio {

    public CurioInfernoFlower() {
        super(ModItems.FRIENDLY_FIRE_FLOWER);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();

        if (living.getCommandSenderWorld().isClientSide) {
            return;
        }

        living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, true, true));
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        target.addEffect(new MobEffectInstance(ModEffects.INFERNO.get(), 100)); //TODO configurability
    }
}
