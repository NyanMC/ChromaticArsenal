package com.chromanyan.chromaticarsenal.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class EffectBubblePanic extends MobEffect {
    public EffectBubblePanic() {
        super(MobEffectCategory.BENEFICIAL, 0x4287f5);
        this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "2ac7dff3-679e-48d7-8c04-f8989d118ff5", 0.5, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
