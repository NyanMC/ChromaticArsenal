package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class CurioThunderguard extends BaseCurioItem {

    public CurioThunderguard() {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .defaultDurability(0)
                .fireResistant()); // lightning places fire. we don't want to instantly destroy the thunderguard when it gets created
    }

    @Override
    public boolean canBeHurtBy(@NotNull DamageSource damageSource) {
        return damageSource != DamageSource.LIGHTNING_BOLT && super.canBeHurtBy(damageSource);
    }
}
