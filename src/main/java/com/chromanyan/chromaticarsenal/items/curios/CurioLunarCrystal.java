package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

@SuppressWarnings("all")
public class CurioLunarCrystal extends BaseCurioItem {

    private final Random rand = new Random();
    private final ModConfig.Common config = ModConfig.COMMON;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(new TranslatableComponent("tooltip.chromaticarsenal.lunar_crystal.1"));
        list.add(new TranslatableComponent("tooltip.chromaticarsenal.lunar_crystal.2", "§b" + config.levitationChance.get(), "§b" + (config.levitationPotency.get() + 1), "§b" + (((float) config.levitationDuration.get()) / 20)));
        if (getItemEnchantmentLevel(Enchantments.FALL_PROTECTION, stack) > 0) {
            list.add(new TranslatableComponent("tooltip.chromaticarsenal.lunar_crystal.3", "§b" + (int) Math.round(100 * (1.0 - getFallMultiplier(stack))))); // use Math.round so the tooltip doesn't display it as one more or less than it should be
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.getOrCreateTag().getString("crafter.id").equalsIgnoreCase("854adc0b-ae55-48d6-b7ba-e641a1eebf42") || config.everyoneIsLuna.get()) {
            return new TranslatableComponent(this.getDescriptionId(stack) + ".luna");
        }
        return super.getName(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (stack.getOrCreateTag().getString("crafter.id").isEmpty()) {
            if (entity instanceof Player) {
                if (((Player) entity).getUUID() != null) {
                    stack.getOrCreateTag().putString("crafter.id", ((Player) entity).getUUID().toString());
                }
            }
        } else {
            final Player player = entity.level.getPlayerByUUID(UUID.fromString(stack.getOrCreateTag().getString("crafter.id")));
            if ((player != null) && stack.getOrCreateTag().getString("crafter.name").isEmpty()) {
                stack.getOrCreateTag().putString("crafter.name", player.getDisplayName().getString());
            }
        }
    }

    public float getFallMultiplier(ItemStack stack) {
        int fallEnchantLevel = getItemEnchantmentLevel(Enchantments.FALL_PROTECTION, stack);
        float percentage = (float) (1 - (fallEnchantLevel * config.fallDamageReduction.get()));
        return Math.max(0, percentage);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.FALL_PROTECTION) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, Reference.MODID + ":defy_gravity", config.gravityModifier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
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
            target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, config.levitationDuration.get(), config.levitationPotency.get()));
        }
    }

    @Override
    public void onGetImmunities(PotionEvent.PotionApplicableEvent event, ItemStack stack, MobEffect effect) {
        if (event.getPotionEffect().getEffect() == MobEffects.LEVITATION) {
            event.setResult(Event.Result.DENY);
        }
    }
}
