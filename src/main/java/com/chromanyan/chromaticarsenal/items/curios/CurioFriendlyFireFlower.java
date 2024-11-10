package com.chromanyan.chromaticarsenal.items.curios;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;

public class CurioFriendlyFireFlower extends BaseCurioItem {

    public CurioFriendlyFireFlower() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).defaultDurability(35).fireResistant(), SoundEvents.FIRECHARGE_USE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!Screen.hasShiftDown()) return;
        TooltipHelper.itemTooltipLine(stack, 1, list);
        if (stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0)
            TooltipHelper.itemTooltipLine(stack, "silktouch", list);

        TooltipHelper.itemTooltipLine(stack, 2, list, TooltipHelper.ticksToSecondsTooltip(getEffectDuration(stack)));
        if (stack.isDamageableItem()) // this check includes when the item has the unbreakable tag
            TooltipHelper.itemTooltipLine(stack, 3, list);

        if (ChromaCurioHelper.isChromaticTwisted(stack, Minecraft.getInstance().player))
            TooltipHelper.itemTooltipLine(stack, "twisted", list, TooltipHelper.percentTooltip(config.twistedUnbreakingChance.get()));
    }

    private int getEffectDuration(ItemStack stack) {
        return config.fireResistanceDuration.get() + (config.fireResistanceProtectionDuration.get() * stack.getEnchantmentLevel(Enchantments.FIRE_PROTECTION));
    }

    private boolean isFireImmune(@NotNull LivingEntity living) {
        return living.hasEffect(MobEffects.FIRE_RESISTANCE) || living.fireImmune(); // will fireImmune() ever even trigger for a player? hell if i know
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity living = context.entity();
        if (!living.getCommandSenderWorld().isClientSide) {
            if (living.isOnFire()) {
                if (isFireImmune(living)) {
                    living.clearFire();
                    return;
                }

                living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, getEffectDuration(stack), 0, true, true));

                // we no longer have to check canBeDamaged here because hurtAndBreak checks isDamageable
                if (!ChromaCurioHelper.isChromaticTwisted(stack, context.entity()) || Math.random() > config.twistedUnbreakingChance.get()) {
                    stack.hurtAndBreak(1, living, damager -> CuriosApi.broadcastCurioBreakEvent(context));
                }
            } else {
                if (ChromaCurioHelper.isChromaticTwisted(stack, context.entity())) {
                    if (!living.getCommandSenderWorld().isClientSide && living.tickCount % config.twistedFireDamageTicks.get() == 0) {
                        living.hurt(living.getCommandSenderWorld().damageSources().onFire(), config.twistedFireDamageValue.get().floatValue());
                    }
                }
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.FIRE_PROTECTION || enchantment == Enchantments.SILK_TOUCH) {
            return true;
        } else {
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            if (target instanceof Player
                    || (target instanceof OwnableEntity && ((OwnableEntity) target).getOwner() instanceof Player)) {
                event.setAmount(0);
                event.setCanceled(true);
                return;
            }
        }

        if (target == null) return;

        if (player == target) {
            event.setAmount(0);
            event.setCanceled(true);
            return;
        }

        if (ChromaCurioHelper.isChromaticTwisted(stack, player) && !isFireImmune(target)) {
            target.setSecondsOnFire(100);
        }
    }

    @Override
    public boolean isDamageable(ItemStack stack) { // make the config option alter the vanilla behavior, not hardcoded checks
        return ModConfig.COMMON.canBeDamaged.get() && super.isDamageable(stack);
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().is(DamageTypes.THORNS)) {
            event.setAmount(0);
            event.setCanceled(true);

            // someday these three lines are going to make someone hate me, i guarantee it
            Optional<SlotResult> slotResultOptional = ChromaCurioHelper.getCurio(player, this);
            slotResultOptional.ifPresent(slotResult -> stack.hurtAndBreak(1, player,
                    damager -> CuriosApi.broadcastCurioBreakEvent(slotResult.slotContext())));

            return;
        }

        if (!ChromaCurioHelper.isChromaticTwisted(stack, player)) {
            return;
        }
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            if (!livingAttacker.fireImmune()) {
                livingAttacker.setSecondsOnFire(100);
            }
        }
    }
}
