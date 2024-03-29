package com.chromanyan.chromaticarsenal.items.curios.challenge;

import com.chromanyan.chromaticarsenal.init.ModRarities;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class CurioLimitBreak extends BaseCurioItem {
    private static final DamageSource ASCENDED = new DamageSource("chromaticarsenal.ascended").bypassArmor().bypassInvul();

    public CurioLimitBreak() {
        super(ModRarities.CHALLENGE, SoundEvents.BEACON_DEACTIVATE);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.challenge"));
        list.add(Component.translatable("tooltip.chromaticarsenal.ascended_star.1", TooltipHelper.valueTooltip(config.bonusFortune.get()), TooltipHelper.valueTooltip(config.bonusLooting.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.ascended_star.2", TooltipHelper.valueTooltip(config.damageMultiplier.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.cursed").withStyle(ChatFormatting.RED));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (entity instanceof Player playerEntity) {
            if (playerEntity.isCreative()) {
                return;
            }
        }
        entity.hurt(ASCENDED, 10000F); // if 10000 isn't enough to kill, i don't know what is
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        return super.getLootingLevel(slotContext, source, target, baseLooting, stack) + config.bonusLooting.get();
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        return super.getFortuneLevel(slotContext, lootContext, stack) + config.bonusFortune.get();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        if (slotContext.entity() instanceof Player) {
            CuriosApi.getCuriosHelper().addSlotModifier(attributes, "super_curio",
                    UUID.fromString("d020cd5d-c050-49e4-a0ea-ef27adf7e6d0"), config.bonusSlots.get(), AttributeModifier.Operation.ADDITION);
        }

        return attributes;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE;
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        event.setAmount((float) (event.getAmount() * config.damageMultiplier.get()));
    }
}
