package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioHarpyFeather extends BaseCurioItem {

    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.utility"));
        if (!ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.1"));
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.2", TooltipHelper.multiplierAsPercentTooltip(config.featherFallDamageReduction.get())));
        } else {
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.twisted"));
            list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.twisted2"));
        }
        list.add(Component.translatable("tooltip.chromaticarsenal.harpy_feather.3"));
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().isFall()) {
            event.setAmount(event.getAmount() * config.featherFallDamageReduction.get().floatValue());
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        if (ChromaCurioHelper.isChromaticTwisted(stack, slotContext.entity())) // this is safe because the livingEntity parameter is @Nullable
            atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":twisted_feather_gravity", config.twistedFeatherGravityModifier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        return atts;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (level.isClientSide()) {
            return;
        }
        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(this)) {
                if (player.getVehicle() == null) {
                    if (player.isOnGround()) {
                        player.getCooldowns().removeCooldown(this);
                    } else {
                        player.getCooldowns().addCooldown(this, 60);
                    }
                } else {
                    if (player.getVehicle().isOnGround()) {
                        player.getCooldowns().removeCooldown(this);
                    } else {
                        player.getCooldowns().addCooldown(this, 60);
                    }
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isDiscrete())
            return InteractionResultHolder.pass(itemstack);

        if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
            player.getCooldowns().addCooldown(this, 60);
        if (!level.isClientSide()) {
            if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
                player.resetFallDistance();
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.8f, 5f);
        }
        Vec3 vec3 = player.getDeltaMovement();
        if (player.getVehicle() != null) {
            if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
                player.getVehicle().setDeltaMovement(vec3.x, config.jumpForce.get(), vec3.z);
            else
                player.getVehicle().setDeltaMovement(0, -1, 0);
        } else {
            if (!ChromaCurioHelper.isChromaticTwisted(itemstack, player))
                player.setDeltaMovement(vec3.x, config.jumpForce.get(), vec3.z);
            else
                player.setDeltaMovement(0, -1, 0);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        if (ChromaCurioHelper.isChromaticTwisted(stack, null))
            return ModRarities.TWISTED;
        else
            return ModRarities.UTILITY;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8F, 5F);
    }
}
