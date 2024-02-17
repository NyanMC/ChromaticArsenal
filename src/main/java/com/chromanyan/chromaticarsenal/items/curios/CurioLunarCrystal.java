package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CurioLunarCrystal extends BaseCurioItem {

    private static final Random rand = new Random();

    public CurioLunarCrystal() {
        super(SoundEvents.END_PORTAL_FRAME_FILL);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (!ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.lunar_crystal.1"));
        }
        list.add(Component.translatable("tooltip.chromaticarsenal.lunar_crystal.2", TooltipHelper.valueTooltip(config.levitationChance.get()), TooltipHelper.potionAmplifierTooltip(config.levitationPotency.get()), TooltipHelper.ticksToSecondsTooltip(getLevitationDuration(stack, null))));
        if (stack.getEnchantmentLevel(Enchantments.FALL_PROTECTION) > 0) {
            list.add(Component.translatable("tooltip.chromaticarsenal.lunar_crystal.3", TooltipHelper.multiplierAsPercentTooltip(getFallMultiplier(stack)))); // use Math.round so the tooltip doesn't display it as one more or less than it should be
        }
        if (ChromaCurioHelper.isChromaticTwisted(stack, null)) {
            list.add(Component.translatable("tooltip.chromaticarsenal.lunar_crystal.twisted"));
        }
    }

    private static int getLevitationDuration(ItemStack stack, @Nullable LivingEntity player) {
        int powerModifier = stack.getEnchantmentLevel(Enchantments.POWER_ARROWS) * config.levitationDurationEnchantmentModifier.get();
        if (ChromaCurioHelper.isChromaticTwisted(stack, player)) {
            return config.levitationDuration.get() + powerModifier + config.twistedLevitationDurationModifier.get();
        } else {
            return config.levitationDuration.get() + powerModifier;
        }
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        if (stack.getOrCreateTag().getString("crafter.id").equalsIgnoreCase("854adc0b-ae55-48d6-b7ba-e641a1eebf42") || config.everyoneIsLuna.get()) {
            return Component.translatable(this.getDescriptionId(stack) + ".luna");
        }
        return super.getName(stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (stack.getOrCreateTag().getString("crafter.id").isEmpty()) {
            if (entity instanceof Player) {
                stack.getOrCreateTag().putString("crafter.id", entity.getUUID().toString());
            }
        } else {
            final Player player = entity.level.getPlayerByUUID(UUID.fromString(stack.getOrCreateTag().getString("crafter.id")));
            if ((player != null) && stack.getOrCreateTag().getString("crafter.name").isEmpty()) {
                stack.getOrCreateTag().putString("crafter.name", player.getDisplayName().getString());
            }
        }
    }

    public float getFallMultiplier(ItemStack stack) {
        int fallEnchantLevel = stack.getEnchantmentLevel(Enchantments.FALL_PROTECTION);
        float percentage = (float) (1 - (fallEnchantLevel * config.fallDamageReduction.get()));
        return Math.max(0, percentage);
    }

    public static void handleDrop(LivingDropsEvent event, LivingEntity dying) {
        int chance = config.lunarCrystalDropChance.get() - (event.getLootingLevel() * config.lunarCrystalDropLootingModifier.get());
        // first check prevents an edge case crash where the player somehow has a ridiculously high looting level
        if (chance <= 0 || rand.nextInt(chance) == 0) {
            event.getDrops().add(dying.spawnAtLocation(new ItemStack(ModItems.LUNAR_CRYSTAL.get())));
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.FALL_PROTECTION || enchantment == Enchantments.POWER_ARROWS) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, ChromaticArsenal.MODID + ":defy_gravity", config.gravityModifier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        return atts;
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource() == DamageSource.FALL) {
            event.setAmount(event.getAmount() * getFallMultiplier(stack));
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        int randresult = rand.nextInt(config.levitationChance.get() - 1);
        if (randresult == 0) {
            target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, getLevitationDuration(stack, player), config.levitationPotency.get()), player);
            if (ChromaCurioHelper.isChromaticTwisted(stack, player)) {
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, getLevitationDuration(stack, player), config.levitationPotency.get()), player);
            }
        }
    }

    @Override
    public void onGetImmunities(MobEffectEvent.Applicable event, ItemStack stack, MobEffect effect) {
        if (event.getEffectInstance().getEffect() == MobEffects.LEVITATION) {
            if (!ChromaCurioHelper.isChromaticTwisted(stack, event.getEntity())) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
