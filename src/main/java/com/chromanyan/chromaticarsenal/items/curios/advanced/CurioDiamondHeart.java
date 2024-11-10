package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CurioDiamondHeart extends BaseSuperCurio {

    public CurioDiamondHeart() {
        super(ModItems.GOLDEN_HEART, SoundEvents.ARMOR_EQUIP_DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if (!Screen.hasShiftDown()) return;
        TooltipHelper.itemTooltipLine(stack, 1, list);
        TooltipHelper.itemTooltipLine(stack, 2, list, TooltipHelper.valueTooltip((config.fracturedPotency.get() + 1) * 10), TooltipHelper.ticksToSecondsTooltip(config.fracturedDuration.get()));
        TooltipHelper.itemTooltipLine(stack, 3, list, TooltipHelper.ticksToSecondsTooltip(config.revivalCooldown.get()));
        TooltipHelper.cooldownTooltipLine(stack, list);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        super.curioTick(context, stack);
        CompoundTag nbt = stack.getOrCreateTag();
        if (livingEntity.getCommandSenderWorld().isClientSide) {
            if (CooldownHelper.getCounter(nbt) == 1) {
                if (livingEntity instanceof Player playerEntity) {
                    playerEntity.displayClientMessage(Component.translatable("message.chromaticarsenal.revival_cooldown_finished"), false);
                }
            }
        } else {
            CooldownHelper.tickCounter(nbt, SoundEvents.IRON_GOLEM_REPAIR, livingEntity);
        }
    }

    @Override
    public void onWearerDied(LivingDeathEvent event, ItemStack stack, LivingEntity player) {
        if (player.hasEffect(ModEffects.FRACTURED.get())) return;
        if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;

        CompoundTag nbt = stack.getOrCreateTag();
        if (!CooldownHelper.isCooldownFinished(nbt)) return;

        CooldownHelper.updateCounter(nbt, config.revivalCooldown.get());
        event.setCanceled(true);
        player.setHealth(player.getMaxHealth());
        player.addEffect(new MobEffectInstance(ModEffects.FRACTURED.get(), config.fracturedDuration.get(), config.fracturedPotency.get()), player);
        player.setHealth(player.getMaxHealth()); // lazy max health correction, just set the value twice lol
        player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 0.5F, 1.0F);
    }
}
