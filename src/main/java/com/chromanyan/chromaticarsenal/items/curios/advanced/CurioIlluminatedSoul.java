package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.Optional;

public class CurioIlluminatedSoul extends BaseSuperCurio {

    public CurioIlluminatedSoul() {
        super(null);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("tooltip.chromaticarsenal.super_glow_ring.1"));
        if (config.glowingDuration.get() > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.super_glow_ring.2", TooltipHelper.ticksToSecondsTooltip(config.glowingDuration.get())));
        if (config.illuminatedUndeadMultiplier.get() > 1)
            list.add(Component.translatable("tooltip.chromaticarsenal.super_glow_ring.3", TooltipHelper.multiplierAsPercentTooltip(config.illuminatedUndeadMultiplier.get())));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        LivingEntity entity = slotContext.entity();
        if (!entity.getCommandSenderWorld().isClientSide) {
            entity.setGlowingTag(true);
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 410, 0, false, false), entity);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.getCommandSenderWorld().isClientSide)
            return;
        entity.removeEffect(MobEffects.NIGHT_VISION);
        entity.setGlowingTag(false);
    }

    @Override
    public void onGetImmunities(MobEffectEvent.Applicable event, ItemStack stack, MobEffect effect) {
        if (event.getEffectInstance().getEffect() == MobEffects.BLINDNESS || event.getEffectInstance().getEffect() == MobEffects.DARKNESS) {
            event.setResult(Event.Result.DENY);
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (config.glowingDuration.get() > 0)
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, config.glowingDuration.get()), player);
        if (target.getMobType() == MobType.UNDEAD)
            event.setAmount(event.getAmount() * config.illuminatedUndeadMultiplier.get().floatValue());
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerVariants() {
        ItemProperties.register(ModItems.SUPER_GLOW_RING.get(), new ResourceLocation(ChromaticArsenal.MODID, "equipped"),
                (stack, world, entity, thing) -> { // what a nightmare
                    if (entity == null) {
                        return 0;
                    }

                    Optional<SlotResult> result = CuriosApi.getCuriosHelper().findFirstCurio(entity, stack.getItem());

                    if (result.isPresent() && result.get().stack() != null && result.get().stack() == stack) {
                        return 1;
                    }

                    return 0;
                });
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.5F, 1);
    }
}
