package com.chromanyan.chromaticarsenal.items.food;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

public class MagicGarlicBread extends Item {
    private final Common config = ModConfig.COMMON;

    public MagicGarlicBread() {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(64)
                .rarity(Rarity.RARE)
                .food(new FoodProperties.Builder()
                        .nutrition(10)
                        .saturationMod(0.7F)
                        .alwaysEat()
                        .build()));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level world, @NotNull LivingEntity player) {
        if (!world.isClientSide) {
            Optional<SlotResult> rings = ChromaCurioHelper.getCurio(player, ModItems.DUALITY_RINGS.get());
            if (rings.isPresent()) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, config.strengthDuration.get(), config.strengthLevel.get(), true, true), player);
                player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, config.healthBoostDuration.get(), config.healthBoostLevel.get(), true, true), player);
                if (ChromaCurioHelper.isChromaticTwisted(rings.get().stack(), player)) {
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, config.twistedSaturationDuration.get()), player);
                }
            }
        }
        return super.finishUsingItem(stack, world, player);
    }

}
