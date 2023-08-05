package com.chromanyan.chromaticarsenal.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class EffectSpatial extends MobEffect {
    public EffectSpatial() {
        super(MobEffectCategory.BENEFICIAL, 0x000088);
        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "2ac7dff3-679e-48d7-8c04-f8989d118ff5", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.resetFallDistance();
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
