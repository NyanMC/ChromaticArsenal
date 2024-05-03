package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.projectile.Snowball;
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
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CurioCryoRing extends BaseCurioItem {

    public CurioCryoRing() {
        super(SoundEvents.PLAYER_HURT_FREEZE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!Screen.hasShiftDown()) return;
        list.add(Component.translatable("tooltip.chromaticarsenal.cryo_ring.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.cryo_ring.2", TooltipHelper.valueTooltip(config.cryoDamage.get()), "§b" + TooltipHelper.ticksToSecondsTooltip(config.chilledTicks.get())));
        if (!Objects.equals(config.chilledTicks.get(), config.chilledTicksVulnerable.get())) {
            list.add(Component.translatable("tooltip.chromaticarsenal.cryo_ring.3", TooltipHelper.ticksToSecondsTooltip(config.chilledTicksVulnerable.get())));
        }
        if (ChromaCurioHelper.isChromaticTwisted(stack, Minecraft.getInstance().player))
            list.add(Component.translatable("tooltip.chromaticarsenal.cryo_ring.twisted", TooltipHelper.valueTooltip(config.twistedCryoFireDamageMultiplier.get())));
    }

    private static boolean shouldDoTwistedPenalty(LivingEntity entity, ItemStack stack) {
        return entity.getCommandSenderWorld().getBiome(entity.blockPosition()).is(BiomeTags.SNOW_GOLEM_MELTS) && ChromaCurioHelper.isChromaticTwisted(stack, entity);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // tricks curios into updating the attribute, as it only does such when NBT updates
        LivingEntity entity = slotContext.entity();
        stack.getOrCreateTag().putBoolean("shouldDoTwistedPenalty", shouldDoTwistedPenalty(entity, stack));
    }

    @Override
    public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    public static void doCryoPotionEffects(LivingEntity target, @Nullable LivingEntity player) {
        if (!target.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
            if (target.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                target.addEffect(new MobEffectInstance(ModEffects.CHILLED.get(), config.chilledTicksVulnerable.get()), player);
            } else {
                target.addEffect(new MobEffectInstance(ModEffects.CHILLED.get(), config.chilledTicks.get()), player);
            }
        }
    }

    public static void doCryoEffects(LivingHurtEvent event, LivingEntity target) {
        if (!target.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
            doCryoPotionEffects(target, event.getEntity());
            event.setAmount(event.getAmount() + config.cryoDamage.get().floatValue());
        }

        if (target instanceof SnowGolem && config.cryoHealsGolems.get()) {
            target.heal(config.cryoDamage.get().floatValue());
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        LivingEntity entity = slotContext.entity();
        if (entity == null) {
            ChromaticArsenal.LOGGER.warn("Tried to get attribute modifiers for cryo ring but entity was null");
            return atts; // should hopefully fix a NPE when reloading resources with F3+T
        }

        double speedPenalty = shouldDoTwistedPenalty(entity, stack) ? config.twistedCryoSpeedPenalty.get() : 0D;
        double damagePenalty = shouldDoTwistedPenalty(entity, stack) ? config.twistedCryoDamagePenalty.get() : 0D;

        atts.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":cryo_speed_penalty", speedPenalty, AttributeModifier.Operation.MULTIPLY_TOTAL));
        atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, ChromaticArsenal.MODID + ":cryo_damage_penalty", damagePenalty, AttributeModifier.Operation.MULTIPLY_TOTAL));

        return atts;
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) && event.getSource().getDirectEntity() != null) { // because of course getDirectEntity can be null
            if (event.getSource().getDirectEntity() instanceof Snowball) {
                doCryoEffects(event, target);
            }
        }
        if (ChromaCurioHelper.isChromaticTwisted(stack, player)
                && !(event.getSource().is(DamageTypeTags.IS_PROJECTILE) || event.getSource().is(DamageTypeTags.IS_EXPLOSION) || event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO) || event.getSource().is(DamageTypeTags.IS_FIRE)))
            doCryoPotionEffects(target, player);
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE) && ChromaCurioHelper.isChromaticTwisted(stack, player)) {
            event.setAmount(event.getAmount() * config.twistedCryoFireDamageMultiplier.get().floatValue());
        }
    }

    @Override
    public void onGetImmunities(MobEffectEvent.Applicable event, ItemStack stack, MobEffect effect) {
        if (event.getEffectInstance().getEffect() == ModEffects.CHILLED.get()) {
            event.setResult(Event.Result.DENY);
        }
    }
}
