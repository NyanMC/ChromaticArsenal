package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;

public class CurioThunderguard extends BaseCurioItem {

    public CurioThunderguard() {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .defaultDurability(0)
                .fireResistant(), SoundEvents.LIGHTNING_BOLT_THUNDER); // lightning places fire. we don't want to instantly destroy the thunderguard when it gets created
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource() == DamageSource.LIGHTNING_BOLT) {
            player.addEffect(new MobEffectInstance(ModEffects.THUNDERCHARGED.get(), (int) (event.getAmount() * 60)));
            event.setCanceled(true);
            return; // otherwise it might be possible for two thunderguard users to create an infinite recursive loop? not sure but just to be safe
        }

        // we get the direct entity because it wouldn't make sense to be able to zap ranged attackers
        if (event.getSource().getDirectEntity() instanceof LivingEntity livingEntity) {
            livingEntity.hurt(DamageSource.LIGHTNING_BOLT, 3);
        }
    }

    @Override
    public boolean canBeHurtBy(@NotNull DamageSource damageSource) {
        return damageSource != DamageSource.LIGHTNING_BOLT && super.canBeHurtBy(damageSource);
        // the lightning protection item itself should be immune to lightning
    }
}
