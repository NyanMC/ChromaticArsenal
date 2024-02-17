package com.chromanyan.chromaticarsenal.items.curios.advanced;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import com.chromanyan.chromaticarsenal.util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurioDispellingCrystal extends BaseSuperCurio {

    public CurioDispellingCrystal() {
        super(ModItems.WARD_CRYSTAL, SoundEvents.AMETHYST_BLOCK_PLACE);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.1", TooltipHelper.multiplierAsPercentTooltip(config.antiMagicMultiplierIncoming.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.ward_crystal.2", TooltipHelper.multiplierAsPercentTooltip(config.antiMagicMultiplierOutgoing.get())));
        list.add(Component.translatable("tooltip.chromaticarsenal.super_ward_crystal.1", TooltipHelper.multiplierAsPercentTooltip(config.potionDurationMultiplier.get())));
    }

    @Override
    public void onWearerHurt(LivingHurtEvent event, ItemStack stack, LivingEntity player) {
        if (event.getSource().isMagic() && !ChromaCurioHelper.shouldIgnoreDamageEvent(event)) {
            if (!config.damageSourceBlacklist.get().isEmpty()) {
                for (String blacklisted : config.damageSourceBlacklist.get()) {
                    if (event.getSource().getMsgId().equals(blacklisted))
                        return;
                }
            }
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierIncoming.get()));
        }
    }

    @Override
    public void onWearerAttack(LivingHurtEvent event, ItemStack stack, LivingEntity player, LivingEntity target) {
        if (event.getSource().isMagic() && !ChromaCurioHelper.shouldIgnoreDamageEvent(event)) {
            event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierOutgoing.get()));
        }
    }

    @Override
    public void onPotionApplied(MobEffectEvent.Added event) {
        // why am i like this
        if (!config.effectBlacklist.get().isEmpty()) {
            for (String blacklisted : config.effectBlacklist.get()) { // yes, DO THIS WHILE THE SERVER IS RUNNING. i am very sane thank you very much
                ResourceLocation blacklistedRL = ResourceLocation.tryParse(blacklisted); // null if this doesn't parse as ResourceLocation
                if (blacklistedRL != null) {
                    MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(blacklistedRL); // null if this doesn't match a MobEffect
                    if (effect != null) {
                        if (event.getEffectInstance().getEffect() == effect) {
                            return;
                        }
                    } else {
                        ChromaticArsenal.LOGGER.error("CONFIG PARSE ERROR: The resource location \"" + blacklisted + "\" was not recognized as a potion effect, skipping");
                    }
                } else {
                    ChromaticArsenal.LOGGER.error("CONFIG PARSE ERROR: Failed to parse \"" + blacklisted + "\" as ResourceLocation, skipping");
                }
            }
        }
        event.getEffectInstance().duration *= config.potionDurationMultiplier.get(); // because why should forge let you set the duration of a potion effect without an access transformer?
    }
}
