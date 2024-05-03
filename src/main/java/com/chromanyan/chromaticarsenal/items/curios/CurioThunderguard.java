package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurioThunderguard extends BaseCurioItem {

    public CurioThunderguard() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .defaultDurability(0)
                .fireResistant(), SoundEvents.LIGHTNING_BOLT_THUNDER); // lightning places fire. we don't want to instantly destroy the thunderguard when it gets created
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!Screen.hasShiftDown()) return;
        if (ChromaCurioHelper.isChromaticTwisted(stack, Minecraft.getInstance().player))
            list.add(Component.translatable("tooltip.chromaticarsenal.thunderguard.twisted"));
        else
            list.add(Component.translatable("tooltip.chromaticarsenal.thunderguard.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.thunderguard.2", TooltipHelper.valueTooltip(config.thunderguardZapDamage.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.thunderguard.3"));
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().is(DamageTypes.LIGHTNING_BOLT) && !(ChromaCurioHelper.isChromaticTwisted(stack, player))) {
            player.addEffect(new MobEffectInstance(ModEffects.THUNDERCHARGED.get(), (int) (event.getAmount() * config.thunderchargedDuration.get())));
            event.setCanceled(true);
            return; // otherwise it might be possible for two thunderguard users to create an infinite recursive loop? not sure but just to be safe
        }

        // we get the direct entity because it wouldn't make sense to be able to zap ranged attackers
        if (event.getSource().getDirectEntity() instanceof LivingEntity livingEntity) {
            livingEntity.hurt(livingEntity.getCommandSenderWorld().damageSources().lightningBolt(), config.thunderguardZapDamage.get().floatValue());
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) || !(ChromaCurioHelper.isChromaticTwisted(stack, player))) return;
        Level level = player.getCommandSenderWorld();
        if (level.isClientSide || !(level.isThundering() && level.canSeeSky(target.blockPosition()))) return;

        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        if (lightningBolt == null) return;

        if (player instanceof ServerPlayer serverPlayer) {
            lightningBolt.setCause(serverPlayer);
        }

        lightningBolt.setPos(target.position());
        level.addFreshEntity(lightningBolt);
    }

    @Override
    public boolean canBeHurtBy(@NotNull DamageSource damageSource) {
        return damageSource.is(DamageTypes.LIGHTNING_BOLT) && super.canBeHurtBy(damageSource);
        // the lightning protection item itself should be immune to lightning
    }
}
