package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CurioBubbleAmulet extends BaseCurioItem {
    private static final Random rand = new Random();

    public CurioBubbleAmulet() {
        super(SoundEvents.BUCKET_FILL);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.twisted"));
            return;
        }
        CompoundTag nbt = stack.getOrCreateTag();
        list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.1"));
        if (config.bubblePanicDuration.get() > 0)
            list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.2", TooltipHelper.ticksToSecondsTooltip(config.bubblePanicDuration.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.3", TooltipHelper.ticksToSecondsTooltip(getCooldownDuration(stack))));
        if (!CooldownHelper.isCooldownFinished(nbt)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.cooldown", CooldownHelper.getCounter(nbt)).withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("tooltip.chromaticarsenal.bubble_amulet.cooldown"));
        }
    }

    private static boolean shouldDoTwistedPenalty(LivingEntity living, ItemStack stack) {
        return ChromaCurioHelper.isChromaticTwisted(stack, living) && !living.isInWaterRainOrBubble();
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();

        if (living.level.isClientSide) {
            return; // the server will replicate our changes to the client for us
        }

        if (ChromaCurioHelper.isChromaticTwisted(stack, living)) {
            // tricks curios into updating the attribute, as it only does such when NBT updates
            stack.getOrCreateTag().putBoolean("shouldDoTwistedPenalty", shouldDoTwistedPenalty(living, stack));

            living.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 25, 0, true, true), living);
            return;
        }

        int airSupply = living.getAirSupply();
        int maxAirSupply = living.getMaxAirSupply();

        if (airSupply <= 0) {
            CompoundTag nbt = stack.getOrCreateTag();
            if (!CooldownHelper.isCooldownFinished(nbt))
                return;

            living.getCommandSenderWorld().playSound(null, living.blockPosition(), SoundEvents.BOTTLE_EMPTY,
                    SoundSource.PLAYERS, 0.5F, 1.0F);
            living.setAirSupply(maxAirSupply);
            CooldownHelper.updateCounter(nbt, getCooldownDuration(stack));
            living.addEffect(new MobEffectInstance(ModEffects.BUBBLE_PANIC.get(), config.bubblePanicDuration.get()), living);
        } else if (airSupply >= maxAirSupply) {
            CompoundTag nbt = stack.getOrCreateTag();
            if (CooldownHelper.isCooldownFinished(nbt))
                return;

            CooldownHelper.tickCounter(nbt, SoundEvents.BOTTLE_FILL, living);
        }
    }

    private int getCooldownDuration(ItemStack stack) {
        return config.baseBubbleCooldown.get() - (config.respirationCooldownReduction.get() * stack.getEnchantmentLevel(Enchantments.RESPIRATION));
    }

    private double getSwimSpeedMod(ItemStack stack) {
        return config.amuletSwimSpeed.get() + (config.depthStriderAdditionalSpeed.get() * stack.getEnchantmentLevel(Enchantments.DEPTH_STRIDER));
    }

    public static void handleDrop(LivingDropsEvent event, LivingEntity dying) {
        int chance = config.amuletDropChance.get() - (event.getLootingLevel() * config.amuletDropLootingModifier.get());
        // first check prevents an edge case crash where the player somehow has a ridiculously high looting level
        if (chance <= 0 || rand.nextInt(chance) == 0) {
            event.getDrops().add(dying.spawnAtLocation(new ItemStack(ModItems.BUBBLE_AMULET.get())));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":bubble_swimming", getSwimSpeedMod(stack), AttributeModifier.Operation.MULTIPLY_BASE));
        LivingEntity living = slotContext.entity();

        double speedPenalty = shouldDoTwistedPenalty(living, stack) ? config.twistedBubbleSlowness.get() : 0D;
        atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":bubble_slowness", speedPenalty, AttributeModifier.Operation.MULTIPLY_TOTAL));

        return atts;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.RESPIRATION || enchantment == Enchantments.DEPTH_STRIDER) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }
}
