package com.chromanyan.chromaticarsenal.util;

import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class EventClassInstance {
	Random rand = new Random();
	Common config = ModConfig.COMMON;
	@SubscribeEvent
	public void playerAttackedEvent(LivingHurtEvent event) {
		LivingEntity player = event.getEntityLiving();
		if (!player.getCommandSenderWorld().isClientSide()) {
			// glass shield: block hits
			Optional<ImmutableTriple<String, Integer, ItemStack>> shield = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.GLASS_SHIELD.get(), player);
			if (shield.isPresent() && event.getAmount() != 0 && !event.getSource().isBypassInvul()) {
				ItemStack stack = shield.get().getRight();
				CompoundNBT nbt = stack.getOrCreateTag();
				if (!(nbt.contains("counter") && nbt.getInt("counter") > 0)) {
					nbt.putInt("counter", config.cooldownDuration.get());
					event.setAmount(0);
					player.getCommandSenderWorld().playSound((PlayerEntity)null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundCategory.PLAYERS, 0.5F, 1.0F);
					
				} else {
					// ChromaticArsenal.LOGGER.info("DEBUG: Attempted to block damage, but player had " + nbt.getInt("counter") + "ticks of cooldown remaining");
				}
			}
			
			// dispel gel: reduce incoming damage
			Optional<ImmutableTriple<String, Integer, ItemStack>> crystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.WARD_CRYSTAL.get(), player);
			if (event.getSource().isMagic() && crystal.isPresent() && !event.getSource().isBypassInvul()) {
				event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierIncoming.get()));
			}
			Entity possibleAttacker = event.getSource().getEntity();
			if (possibleAttacker != null && possibleAttacker instanceof LivingEntity) { // you can never be too safe
				// dispel gel: reduce outgoing damage
				Optional<ImmutableTriple<String, Integer, ItemStack>> attackerCrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.WARD_CRYSTAL.get(), (LivingEntity) possibleAttacker);
				if (event.getSource().isMagic() && attackerCrystal.isPresent()) {
					event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierOutgoing.get()));
				}
				// duality rings: increase projectile damage
				Optional<ImmutableTriple<String, Integer, ItemStack>> attackerRings = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.DUALITY_RINGS.get(), (LivingEntity) possibleAttacker);
				if (event.getSource().isProjectile() && attackerRings.isPresent()) {
					event.setAmount((float) (event.getAmount() * config.aroOfClubsMultiplier.get()));
				}
				// lunar crystal: apply levitation
				Optional<ImmutableTriple<String, Integer, ItemStack>> attackerLCrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.LUNAR_CRYSTAL.get(), (LivingEntity) possibleAttacker);
				int randresult = rand.nextInt(config.levitationChance.get() - 1);
				if (attackerLCrystal.isPresent() && randresult == 0) {
					player.addEffect(new EffectInstance(Effects.LEVITATION, config.levitationDuration.get(), config.levitationPotency.get()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void potionImmunityEvent(PotionApplicableEvent event) {
		LivingEntity player = event.getEntityLiving();
		if (!player.getCommandSenderWorld().isClientSide()) {
			Optional<ImmutableTriple<String, Integer, ItemStack>> treads = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.SHADOW_TREADS.get(), player);
			if (treads.isPresent() && event.getPotionEffect().getEffect() == Effects.MOVEMENT_SLOWDOWN) {
				event.setResult(Result.DENY);
			}
			Optional<ImmutableTriple<String, Integer, ItemStack>> lcrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.LUNAR_CRYSTAL.get(), player);
			if (lcrystal.isPresent() && event.getPotionEffect().getEffect() == Effects.LEVITATION) {
				event.setResult(Result.DENY);
			}
		}
	}
	
}
