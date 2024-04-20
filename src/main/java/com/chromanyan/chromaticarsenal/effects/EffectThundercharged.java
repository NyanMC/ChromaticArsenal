package com.chromanyan.chromaticarsenal.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectThundercharged extends MobEffect {
    public EffectThundercharged() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "55b24e97-2c92-465d-a0a6-710260e9dbee", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "5b42e3fa-2f94-4485-a63a-59db27adeae9", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
