package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.Objects;

public class CurioCryoRing extends BaseCurioItem {

    private static final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.cryo_ring.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.cryo_ring.2", "§b" + config.cryoDamage.get(), "§b" + (((float) config.chilledTicks.get()) / 20)));
        if (!Objects.equals(config.chilledTicks.get(), config.chilledTicksVulnerable.get())) {
            list.add(Component.translatable("tooltip.chromaticarsenal.cryo_ring.3", "§b" + (((float) config.chilledTicksVulnerable.get()) / 20)));
        }
    }

    @Override
    public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    public static void doCryoEffects(LivingHurtEvent event, LivingEntity target) {
        if (!target.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
            if (target.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                target.addEffect(new MobEffectInstance(ModPotions.CHILLED.get(), config.chilledTicksVulnerable.get()));
            } else {
                target.addEffect(new MobEffectInstance(ModPotions.CHILLED.get(), config.chilledTicks.get()));
            }
            event.setAmount((float) (event.getAmount() + config.cryoDamage.get()));
        }

        if (target instanceof SnowGolem && config.cryoHealsGolems.get()) {
            target.heal(config.cryoDamage.get().floatValue());
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().isProjectile() && event.getSource().getDirectEntity() != null) { // because of course getDirectEntity can be null
            if (event.getSource().getDirectEntity() instanceof Snowball) {
                doCryoEffects(event, target);
            }
        }
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.PLAYER_HURT_FREEZE, 0.5F, 1);
    }
}
