package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModSounds;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioPrismaticCrystal extends BaseSuperCurio {

    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioPrismaticCrystal() {
        super(ModItems.LUNAR_CRYSTAL);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("tooltip.chromaticarsenal.super_lunar_crystal.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_lunar_crystal.3"));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_lunar_crystal.2", TooltipHelper.valueTooltip(config.voidBounceDamage.get())));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        super.curioTick(context, stack);
        LivingEntity living = context.entity();
        Vec3 vec3 = living.getDeltaMovement();
        if (living.blockPosition().getY() < living.getLevel().getMinBuildHeight() && vec3.y < 0) {
            living.setDeltaMovement(vec3.x, vec3.y * -config.voidBounceMultiplier.get(), vec3.z);
            living.hurt(DamageSource.OUT_OF_WORLD, config.voidBounceDamage.get().floatValue());
            if (stack.getHoverName().getString().toLowerCase().contains("spring")) {
                living.getCommandSenderWorld().playSound(null, living.blockPosition(), ModSounds.SPRING.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
        living.resetFallDistance();
    }

    @Override
    public void onGetImmunities(MobEffectEvent.Applicable event, ItemStack stack, MobEffect effect) {
        if (event.getEffectInstance().getEffect() == MobEffects.LEVITATION) {
            if (stack.getEnchantmentLevel(ModEnchantments.CHROMATIC_TWISTING.get()) == 0) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":super_defy_gravity", config.superGravityModifier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        return atts;
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource() == DamageSource.FALL) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean isEnderMask(SlotContext slotContext, EnderMan enderMan, ItemStack stack) {
        return true;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.END_PORTAL_FRAME_FILL, 0.5F, 1);
    }
}
