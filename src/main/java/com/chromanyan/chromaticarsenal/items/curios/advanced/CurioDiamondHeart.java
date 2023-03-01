package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import top.theillusivec4.curios.api.SlotContext;

public class CurioDiamondHeart extends BaseSuperCurio {

    private final ModConfig.Common config = ModConfig.COMMON;

    public CurioDiamondHeart() {
        super(ModItems.GOLDEN_HEART);
    }

    @SuppressWarnings("all")
    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        super.curioTick(context, stack);
        CompoundTag nbt = stack.getOrCreateTag();
        if (livingEntity.level.isClientSide) {
            if (CooldownHelper.getCounter(nbt) == 1) {
                if (livingEntity instanceof Player) {
                    Player playerEntity = (Player) livingEntity;
                    playerEntity.displayClientMessage(new TranslatableComponent("message.chromaticarsenal.revival_cooldown_finished"), false);
                }
            }
        } else {
            CooldownHelper.tickCounter(nbt, SoundEvents.IRON_GOLEM_REPAIR, livingEntity);
        }
    }

    @Override
    public void onWearerDied(LivingDeathEvent event, ItemStack stack, LivingEntity player) {
        if (!player.hasEffect(ModPotions.FRACTURED.get())) {
            if (!event.getSource().isBypassInvul()) {
                CompoundTag nbt = stack.getOrCreateTag();
                if (CooldownHelper.isCooldownFinished(nbt)) {
                    CooldownHelper.updateCounter(nbt, config.revivalCooldown.get());
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth());
                    player.addEffect(new MobEffectInstance(ModPotions.FRACTURED.get(), config.fracturedDuration.get(), config.fracturedPotency.get()));
                    player.setHealth(player.getMaxHealth()); // lazy max health correction, just set the value twice lol
                    player.getCommandSenderWorld().playSound((Player) null, player.blockPosition(), SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 0.5F, 1.0F);
                }
            }
        }
    }
}
