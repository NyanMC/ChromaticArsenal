package com.chromanyan.chromaticarsenal.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectInferno extends MobEffect {
    public EffectInferno() {
        super(MobEffectCategory.HARMFUL, 0xFF7700);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getRemainingFireTicks() < 10) {
            entity.setRemainingFireTicks(20);
        }
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
